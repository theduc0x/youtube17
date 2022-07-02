package com.example.youtubeapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.my_interface.IItemOnClickCommentListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentYoutubeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int VIEW_TYPE_ITEM = 0,
            VIEW_TYPE_LOADING = 1;
    private boolean isLoadingAdd;

    private ArrayList<CommentItem> listItemCmt;
    private IItemOnClickCommentListener clickCommentListener;

    public CommentYoutubeAdapter(IItemOnClickCommentListener clickCommentListener) {
        this.clickCommentListener = clickCommentListener;
    }

    public void setData(ArrayList<CommentItem> listItemCmt) {
        this.listItemCmt = listItemCmt;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        if (listItemCmt != null && position == listItemCmt.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_TYPE_ITEM == viewType) {
            View view =  LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_comment_video, parent, false);
            return new CommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingCommentViewHolder(view);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            CommentItem item = listItemCmt.get(position);
            if (item == null) {
                return;
            }
            CommentViewHolder viewHolder = (CommentViewHolder) holder;
            viewHolder.setData(item);
            viewHolder.rlOpenReplies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCommentListener.onClickItemComment(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listItemCmt != null) {
            return listItemCmt.size();
        }
        return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civLogoAuthor;
        TextView tvAuthorName, tvDateDiff, tvCommentContent,
                tvLikeCountCmt, tvRepliesCount, tvEditor;
        ImageView ivMoreSelect;
        AppCompatButton btListReplies;
        RelativeLayout rlOpenReplies;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            civLogoAuthor = itemView.findViewById(R.id.civ_logo_author);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvDateDiff = itemView.findViewById(R.id.tv_date_diff);
            tvCommentContent = itemView.findViewById(R.id.tv_cmt_content);
            tvLikeCountCmt = itemView.findViewById(R.id.tv_like_count_cmt);
            tvRepliesCount = itemView.findViewById(R.id.tv_replies_count);
            ivMoreSelect = itemView.findViewById(R.id.iv_more_select);
            btListReplies = itemView.findViewById(R.id.bt_list_replies);
            rlOpenReplies = itemView.findViewById(R.id.rl_open_replies);
            tvEditor = itemView.findViewById(R.id.tv_editor);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)

        void setData(CommentItem item) {
//            if (item == null) {
//                return ;
//            }
            String authorLogoUrl = item.getAuthorLogoUrl();
            String authorName = item.getAuthorName();
            String publishedAt = item.getPublishedAt();
            String updateAt = item.getUpdateAt();
            String dateDiff = Util.getTime(publishedAt);
            String commentContent = item.getTextDisplay();
            String likeCountCmt = item.getLikeCount();
            String repliesCountCmt = item.getTotalReplyCount();

            Picasso.get().load(authorLogoUrl).into(civLogoAuthor);
            tvAuthorName.setText(authorName);
            tvCommentContent.setText(commentContent);
            tvDateDiff.setText(" • " + dateDiff);
            tvLikeCountCmt.setText(Util.convertViewCount(Double.parseDouble(likeCountCmt)));
            tvRepliesCount.setText(String.valueOf(repliesCountCmt));
            if (!publishedAt.equals(updateAt)) {
                tvEditor.setVisibility(View.VISIBLE);
            }
            if (Integer.valueOf(repliesCountCmt) > 0) {
                btListReplies.setVisibility(View.VISIBLE);
                btListReplies.setText(repliesCountCmt + " REPLIES");
            } else {
                btListReplies.setVisibility(View.GONE);
            }
        }
    }

    class LoadingCommentViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;
        public LoadingCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listItemCmt.add(null);
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listItemCmt.size() - 1;
        CommentItem item = listItemCmt.get(pos);
        if (item != null) {
            listItemCmt.remove(pos);
            notifyItemRemoved(pos);
        }

    }


}
