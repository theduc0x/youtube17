package com.example.youtubeapp.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.my_interface.ILoadMore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// View Holder khi loading
class LoadingViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.pb_loading);
    }
}

//View Holder khi loading xong
class RepliesViewHolder extends RecyclerView.ViewHolder {
    CircleImageView civLogoAuthor;
    TextView tvAuthorName, tvDateDiff, tvCommentContent,
            tvLikeCountCmt, tvEditor;
    ImageView ivMoreSelect;

    public RepliesViewHolder(@NonNull View itemView) {
        super(itemView);
        civLogoAuthor = itemView.findViewById(R.id.civ_logo_author_replies_item);
        tvAuthorName = itemView.findViewById(R.id.tv_author_name_replies_item);
        tvDateDiff = itemView.findViewById(R.id.tv_date_diff_replies_item);
        tvCommentContent = itemView.findViewById(R.id.tv_cmt_content_replies_item);
        tvLikeCountCmt = itemView.findViewById(R.id.tv_like_count_cmt_replies_item);
        tvEditor = itemView.findViewById(R.id.tv_editor_replies_item);
        ivMoreSelect = itemView.findViewById(R.id.iv_more_select_replies_item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(RepliesCommentItem replies) {
        if (replies == null) {
            return;
        }

        String authorLogoUrl = replies.getAuthorLogoUrl();
        String authorName = replies.getAuthorName();
        String publishedAt = replies.getPublishedAt();
        String updateAt = replies.getUpdateAt();
        String dateDiff = Util.getTime(publishedAt);
        String commentContent = replies.getTextDisplay();
        int likeCountCmt = replies.getLikeCount();

        // Đưa dữ đổ vào view
        Picasso.get().load(authorLogoUrl).into(civLogoAuthor);
        tvAuthorName.setText(authorName);
        tvCommentContent.setText(commentContent);
        tvDateDiff.setText(" • " + dateDiff);
        tvLikeCountCmt.setText(Util.convertViewCount(likeCountCmt));
        if (!publishedAt.equals(updateAt)) {
            tvEditor.setVisibility(View.VISIBLE);
        }

    }
}

public class RepliesCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RepliesCommentItem> listReplies;
    ILoadMore loadMore;
    Activity activity;
    boolean isLoading;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;


    public RepliesCommentAdapter(
            RecyclerView recyclerView,
            Activity activity,
            ArrayList<RepliesCommentItem> listReplies) {
        this.activity = activity;
        this.listReplies = listReplies;

        final LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Util.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_replies_comment_video, parent, false);
            return new RepliesViewHolder(view);
        } else if (viewType == Util.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RepliesViewHolder) {
            RepliesCommentItem item = listReplies.get(position);
            if (item == null) {
                return ;
            }
            RepliesViewHolder viewHolder = (RepliesViewHolder) holder;
            viewHolder.setData(item);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listReplies.get(position) == null ? Util.VIEW_TYPE_LOADING : Util.VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
//        if (listReplies != null) {
        return listReplies.size();
//        }
//        return 0;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void setLoaded() {
        isLoading = false;
    }

//    @NonNull
//    @Override
//    public RepliesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == Util.VIEW_TYPE_ITEM) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.item_replies_comment_video, parent, false);
//            return new RepliesViewHolder(view);
//        } else if (viewType == Util.VIEW_TYPE_LOADING){
//            View view = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.item_loading, parent, false);
//            return new LoadingViewHolder(view);
//        }
//        return null;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onBindViewHolder(@NonNull RepliesViewHolder holder, int position) {
//        RepliesCommentItem replies = listReplies.get(position);
//        holder.setData(replies);
//    }
//
//    @Override
//    public int getItemCount() {
//            return listReplies.size();
//
//    }

}
