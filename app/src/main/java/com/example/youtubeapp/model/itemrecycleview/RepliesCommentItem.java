package com.example.youtubeapp.model.itemrecycleview;

import com.example.youtubeapp.model.listcomment.RepliesComment;

public class RepliesCommentItem {
    private String textDisplay;
    private String authorName;
    private String authorLogoUrl;
    private String authorChannelI;
    private int likeCount;
    private String publishedAt;
    private String updateAt;

    public RepliesCommentItem(String textDisplay, String authorName, String authorLogoUrl, String authorChannelI, int likeCount, String publishedAt, String updateAt) {
        this.textDisplay = textDisplay;
        this.authorName = authorName;
        this.authorLogoUrl = authorLogoUrl;
        this.authorChannelI = authorChannelI;
        this.likeCount = likeCount;
        this.publishedAt = publishedAt;
        this.updateAt = updateAt;
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

    public String getAuthorChannelI() {
        return authorChannelI;
    }

    public void setAuthorChannelI(String authorChannelI) {
        this.authorChannelI = authorChannelI;
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
}
