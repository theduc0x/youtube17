package com.example.youtubeapp.utiliti;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listcomment.Comment;
import com.example.youtubeapp.model.listcomment.ItemsComment;
import com.example.youtubeapp.model.listcomment.RepliesComment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Util {
    final public static String API_KEY = "AIzaSyDZgG1KiZuVdzIFKRhKcZiVXBQKxfcEkAo";
    final public static String ID_PLAYLIST = "PL8A83124F1D79BD4F";
    final public static String urlListVideoMostPopular = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&part=statistics&chart=mostPopular&locale=vn&regionCode=vn&key=AIzaSyDkEdU_hnItFhVO0yDBS758w4FFDIWDuzg&maxResults=50";
    public static int REQUEST_CODE_VIDEO = 123;
    public static String BUNDLE_EXTRA_OBJECT_ITEM_VIDEO = "extra item video";
    public static String BUNDLE_EXTRA_OBJECT_ITEM_VIDEO_FROM_RELATED = "extra item video related";
    public static String BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES = "extra item video replies";
    public static String BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE = "extra item video repliess";
    public static String BUNDLE_EXTRA_ITEM_VIDEO = "extra item v video";
    public static String BUNDLE_EXTRA_ID_VIDEO = "extra id video";
    public static String BUNDLE_EXTRA_CMT_COUNT_VIDEO = "extra comment count video";
    public static String EXTRA_ID_CHANNEL_TO_CHANNEL = "to channel";
    public static String EXTRA_TITLE_CHANNEL_TO_CHANNEL = "to title channel h";
    public static String EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER = "to channel";
    public static String BUNDLE_EXTRA_PLAY_LIST_TO_VIDEO_PLAY_LIST = "to play list";
//    public static ArrayList<VideoItem> listVideoItem = null;
//    public static ArrayList<CommentItem> listCmtItem = null;
    public static ArrayList<RepliesCommentItem> listReplies = null;
    public static String nextPageToken = "";

    public static final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;

    public static String convertViewCount(double viewCount) {
        double view;
        if (viewCount < 1000) {
            return (int)viewCount + "";
        } else if (viewCount < 1000000) {
            view = (double) viewCount / 1000;
            return (double) Math.round(view * 10) / 10 + "K";
        } else if (viewCount >= 1000000) {
            view = (double) viewCount / 1000000;
            return (double) Math.round(view * 10) / 10 + "M";
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTime(String dateOne) {
        String days = "";
        Date currentDate = new Date();
        Date date1 = null;
        int s = 0;
        // chuyển đổi định dạng ISO 8601 sang date
        OffsetDateTime lastAvailableDateTime = OffsetDateTime.parse(dateOne);
        Long a = lastAvailableDateTime.toInstant().toEpochMilli();
        date1 = new Date(a);

        // số khoảng cách là số giây
        long getDiff = (currentDate.getTime() - date1.getTime()) / 1000;

        if (getDiff >= 0 && getDiff < 60) {
            days = getDiff + " second ago";

        } else if (getDiff >= 60 && getDiff < 3600) {
            s = (int) (getDiff / 60);
            days = s + " min ago";

        } else if (getDiff >= 3600 && getDiff < 86400) {
            s = (int) getDiff / (60 * 60);
            days = s + " hour ago";

        } else {
            long getDayDiff = getDiff / (24 * 60 * 60);
            if (getDayDiff >= 0 && getDayDiff <= 13) {
                days = String.valueOf(getDayDiff) + " days ago";

            } else if (getDayDiff < 30) {
                s = (int) getDayDiff / 7;
                days = s + " weeks ago";

            } else if (getDayDiff >= 30 && getDayDiff <365) {
                s = (int) getDayDiff / 30;
                days = s + " months ago";

            } else if (getDayDiff >= 365) {
                s = (int) getDayDiff / 365;
                days = s + " year ago";
            }
        }
        return days;
    }


}
