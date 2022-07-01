package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.adapter.RepliesCommentAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.model.listreplies.ItemsR;
import com.example.youtubeapp.model.listreplies.Replies;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialogRepliesFragment extends BottomSheetDialogFragment {
    CommentItem itemR;
    RelativeLayout rlOpenKeyboard;
    CircleImageView civReceive;
    TextView tvNameReceive, tvDateDiffReceive, tvEditor;
    TextView tvContentReceive, likeCountReceive, repliesCountReceive;
    RecyclerView rvListReplies;
    RepliesCommentAdapter adapter;
    Toolbar tbReplies;
    String parentId;
    ImageButton ibBackCmt;
//    String nextPageToken = "";


    // Khởi tạo fragment dialog với dữ liệu truyền vào là 1 CommentItem
    public static BottomSheetDialogRepliesFragment newInstance(CommentItem item) {
        BottomSheetDialogRepliesFragment bsdRepliesFragment =
                new BottomSheetDialogRepliesFragment();

        // Khởi tạo dialog fragment và gửi dữ liệu đi tới bước sau bằng bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE, item);
        bsdRepliesFragment.setArguments(bundle);
        return bsdRepliesFragment;
    }

    // Khi khởi tạo thì bắt đầu nhận
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            itemR = (CommentItem) bundleReceive
                    .getSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE);
        }
    }

    // Chuyển fagment thành dialog
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_detail_replies, null);
        bottomSheetDialog.setContentView(viewDialog);
//        bottomSheetDialog.setCanceledOnTouchOutside(false);
        // Xoay khi đang chờ load comment
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading...");

        // chiều cao của màn hình activiy chứa fragment
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height * 0.65);

        BottomSheetBehavior bottomSheetBehavior =
                BottomSheetBehavior.from(((View) viewDialog.getParent()));
        bottomSheetBehavior.setMaxHeight(maxHeight);
        bottomSheetBehavior.setPeekHeight(maxHeight);

        // Ánh xạ view và chạy RecycleVIew
        intMain(viewDialog);
        setData();

        ibBackCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        tbReplies.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_replies:
                        bottomSheetDialog.dismiss();

                        break;
                }
                return false;
            }
        });
        return bottomSheetDialog;
    }

    private void intMain(View view) {
        Util.listReplies = new ArrayList<>();
        civReceive = view.findViewById(R.id.civ_logo_author_replies);
        tvNameReceive = view.findViewById(R.id.tv_author_name_replies);
        tvDateDiffReceive = view.findViewById(R.id.tv_date_diff_replies);
        tvContentReceive = view.findViewById(R.id.tv_cmt_content_replies);
        likeCountReceive = view.findViewById(R.id.tv_like_count_cmt_replies);
        repliesCountReceive = view.findViewById(R.id.tv_replies_count_replies);
        rvListReplies = view.findViewById(R.id.rv_item_replies);
        tbReplies = view.findViewById(R.id.tb_replies_video);
        rlOpenKeyboard = view.findViewById(R.id.rl_open_replies_keyboard);
        tvEditor = view.findViewById(R.id.tv_editor_replies);
        ibBackCmt = view.findViewById(R.id.ib_back_comment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        if (itemR == null) {
            return;
        }
        String publishAt = itemR.getPublishedAt();
        String updateAt = itemR.getUpdateAt();
        parentId = itemR.getIdComment();

        Picasso.get().load(itemR.getAuthorLogoUrl()).into(civReceive);
        tvNameReceive.setText(itemR.getAuthorName());
        tvContentReceive.setText(itemR.getTextDisplay());
        tvDateDiffReceive.setText(" • " + Util.getTime(itemR.getPublishedAt()));
        likeCountReceive.setText(Util.convertViewCount(itemR.getLikeCount()));
        repliesCountReceive.setText(Util.convertViewCount(itemR.getTotalReplyCount()));
        if (publishAt.equals(updateAt)) {
            tvEditor.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);

        rvListReplies.setLayoutManager(linearLayoutManager);

        adapter = new RepliesCommentAdapter(rvListReplies, getActivity(), Util.listReplies);
        callApiReplies(Util.nextPageToken, parentId, "10");
        rvListReplies.addItemDecoration(decoration);
        rvListReplies.setAdapter(adapter);

//        adapter.setLoadMore(new ILoadMore() {
//            @Override
//            public void onLoadMore() {
//                String s = Util.nextPageToken;
//                int totalR = itemR.getTotalReplyCount();
//                Log.d("abccc", itemR.getTotalReplyCount() + "");
//                if (Util.listReplies.size() <= totalR) {
//                    Util.listReplies.add(null);
//                    adapter.notifyItemInserted(Util.listReplies.size() - 1);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Util.listReplies.remove(Util.listReplies.size() - 1);
//                            adapter.notifyItemRemoved(Util.listReplies.size());
//                            callApiRepliess(Util.nextPageToken, parentId, "5");
//                            int index = Util.listReplies.size();
//                            int end = index + 5;
//                            for (int i = index ; i < end; i++) {
//                                adapter.notifyItemInserted(i);
//                            }
//                            Log.d("abcccc", s + "");
//                            adapter.setLoaded();
//                        }
//                    }, 1000);
//                } else {
//                    Log.d("abccc", "Success");
//                }
//            }
//        });
    }

    public void callApiReplies(String nextPageToken, String parentId, String maxResults) {
        ApiServicePlayList.apiServicePlayList.replies(
                nextPageToken,
                "snippet",
                maxResults,
                parentId,
                "plainText",
                Util.API_KEY
        ).enqueue(new Callback<Replies>() {

            @Override
            public void onResponse(Call<Replies> call, Response<Replies> response) {
                Replies replies = null;
                String authorLogoUrl = "", authorName = "", publishAt = "", updateAt = "",
                        displayContentCmt = "", authorIdChannel = "";
                int likeCount = 0;
//                Util.listCmtItem.clear();
                replies = response.body();
                Util.nextPageToken = replies.getNextPageToken();
                if (replies != null) {
                    ArrayList<ItemsR> listItem = replies.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        authorLogoUrl = listItem.get(i).getSnippet().getAuthorProfileImageUrl();
                        authorName = listItem.get(i).getSnippet()
                                .getAuthorDisplayName();
                        publishAt = listItem.get(i).getSnippet()
                                .getPublishedAt();
                        updateAt = listItem.get(i).getSnippet()
                                .getUpdatedAt();
                        authorIdChannel = listItem.get(i).getSnippet()
                                .getAuthorChannelId().getValue();
                        displayContentCmt = listItem.get(i).getSnippet()
                                .getTextDisplay();
                        likeCount = listItem.get(i).getSnippet()
                                .getLikeCount();
                        Util.listReplies.add(new RepliesCommentItem(
                                displayContentCmt, authorName, authorLogoUrl,
                                authorIdChannel, likeCount, publishAt, updateAt
                        ));
                        adapter.notifyItemInserted(i);
                    }
//                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Replies> call, Throwable t) {

            }
        });
    }


    public void callApiRepliess(String nextPageToken, String parentId, String maxResults) {
        ApiServicePlayList.apiServicePlayList.replies(
                nextPageToken,
                "snippet",
                maxResults,
                parentId,
                "plainText",
                Util.API_KEY
        ).enqueue(new Callback<Replies>() {

            @Override
            public void onResponse(Call<Replies> call, Response<Replies> response) {
                Replies replies = null;
                String authorLogoUrl = "", authorName = "", publishAt = "", updateAt = "",
                        displayContentCmt = "", authorIdChannel = "";
                int likeCount = 0;
                replies = response.body();
                Util.nextPageToken = replies.getNextPageToken();
                if (replies != null) {
                    ArrayList<ItemsR> listItem = replies.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        authorLogoUrl = listItem.get(i).getSnippet().getAuthorProfileImageUrl();
                        authorName = listItem.get(i).getSnippet()
                                .getAuthorDisplayName();
                        publishAt = listItem.get(i).getSnippet()
                                .getPublishedAt();
                        updateAt = listItem.get(i).getSnippet()
                                .getUpdatedAt();
                        authorIdChannel = listItem.get(i).getSnippet()
                                .getAuthorChannelId().getValue();
                        displayContentCmt = listItem.get(i).getSnippet()
                                .getTextDisplay();
                        likeCount = listItem.get(i).getSnippet()
                                .getLikeCount();
                        Util.listReplies.add(new RepliesCommentItem(
                                displayContentCmt, authorName, authorLogoUrl,
                                authorIdChannel, likeCount, publishAt, updateAt
                        ));

                    }
//                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Replies> call, Throwable t) {

            }
        });
    }

}
