package com.example.youtubeapp.model.itemrecycleview;

import com.example.youtubeapp.model.listcomment.RepliesComment;

import java.io.Serializable;

public class CommentItem implements Serializable {
    private String idComment;
    private String textDisplay;
    private String authorName;
    private String authorLogoUrl;
    private String authorChannelId;
    private int likeCount;
    private String publishedAt;
    private String updateAt;
    private int totalReplyCount;
    private RepliesComment repliesComent;

    public CommentItem(String idComment, String textDisplay, String authorName,
                       String authorLogoUrl, String authorChannelId,
                       int likeCount, String publishedAt,
                       String updateAt, int totalReplyCount,
                       RepliesComment repliesComent) {
        this.idComment = idComment;
        this.textDisplay = textDisplay;
        this.authorName = authorName;
        this.authorLogoUrl = authorLogoUrl;
        this.authorChannelId = authorChannelId;
        this.likeCount = likeCount;
        this.publishedAt = publishedAt;
        this.updateAt = updateAt;
        this.totalReplyCount = totalReplyCount;
        this.repliesComent = repliesComent;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getTextDisplay() {
        return textDisplay;
    }

    public void setTextDisplay(String textDisplay) {
        this.textDisplay = textDisplay;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorLogoUrl() {
        return authorLogoUrl;
    }

    public void setAuthorLogoUrl(String authorLogoUrl) {
        this.authorLogoUrl = authorLogoUrl;
    }

    public String getAuthorChannelId() {
        return authorChannelId;
    }

    public void setAuthorChannelId(String authorChannelId) {
        this.authorChannelId = authorChannelId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(int totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public RepliesComment getRepliesComent() {
        return repliesComent;
    }

    public void setRepliesComent(RepliesComment repliesComent) {
        this.repliesComent = repliesComent;
    }
}
