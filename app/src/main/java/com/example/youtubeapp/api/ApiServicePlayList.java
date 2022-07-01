package com.example.youtubeapp.api;

import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.listchannelsfromchannel.ChannelsList;
import com.example.youtubeapp.model.listcomment.Comment;
import com.example.youtubeapp.model.listplaylistvideochannel.PlayList;
import com.example.youtubeapp.model.listreplies.Replies;
import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.example.youtubeapp.model.listvideorelated.RelatedVideo;
import com.example.youtubeapp.model.playlistitem.PlayListItemVideo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServicePlayList {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiServicePlayList apiServicePlayList = new Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServicePlayList.class);

//    // Nhận list video max = 50;
//    @GET("youtube/v3/videos?part" +
//            "=snippet&part=statistics&chart=mostPopular&" +
//            "locale=vn&regionCode=vn&" +
//            "key=AIzaSyDkEdU_hnItFhVO0yDBS758w4FFDIWDuzg&maxResults=50")
//    Call<ListVideo> listVideo();


    // List video
    @GET("youtube/v3/videos")
    Call<ListVideo> listVideoNext(
            @Query("pageToken") String pageToken,
            @Query("part") String partSnippet,
            @Query("part") String partStatic,
            @Query("chart") String chart,
            @Query("locale") String locale,
            @Query("regionCode") String regionCode,
            @Query("key") String key,
            @Query("maxResults") String maxResults);

    // detail Video
    @GET("youtube/v3/videos")
    Call<DetailVideo> detailVideo(
            @Query("part") String partSnippet,
            @Query("part") String partStatic,
            @Query("id") String idVideo,
            @Query("key") String key);

    // Channel
    @GET("youtube/v3/channels")
    Call<Channel> infoChannel(
            @Query("part") String partSnippet,
            @Query("part") String partContent,
            @Query("part") String partStatic,
            @Query("id") String idChannel,
            @Query("key") String key);

    // Full thông tin channel
    @GET("youtube/v3/channels")
    Call<Channel> infoChannelFull(
            @Query("part") String partContent,
            @Query("part") String partSnippet,
            @Query("part") String partStatic,
            @Query("part") String partTopicDetails,
            @Query("part") String partBrand,
            @Query("id") String idChannel,
            @Query("key") String key);

    // Top comment
    @GET("youtube/v3/commentThreads")
    Call<Comment> comment(
            @Query("pageToken") String pageToken,
            @Query("part") String partSnippet,
            @Query("part") String partReplies,
            @Query("order") String order,
            @Query("part") String id,
            @Query("textFormat") String planText,
            @Query("videoId") String videoId,
            @Query("key") String key,
            @Query("maxResults") String maxResults);

    // Replies
    @GET("youtube/v3/comments")
    Call<Replies> replies(
            @Query("pageToken") String pageToken,
            @Query("part") String partSnippet,
            @Query("maxResults") String maxResults,
            @Query("parentId") String parentId,
            @Query("textFormat") String textFomat,
            @Query("key") String key);

    // Related Video Id
    @GET("youtube/v3/search")
    Call<RelatedVideo> relatedCall(
            @Query("pageToken") String pageToken,
            @Query("part") String partSnippet,
            @Query("relatedToVideoId") String relatedId,
            @Query("type") String typeVideo,
            @Query("key") String key,
            @Query("maxResults") String maxResults);

    // Video mới nhất update từ channel
    @GET("youtube/v3/search")
    Call<RelatedVideo> videoUpdateNews(
            @Query("pageToken") String pageToken,
            @Query("part") String partSnippet,
            @Query("part") String partId,
            @Query("channelId") String channelId,
            @Query("order") String order,
            @Query("type") String typeVideo,
            @Query("key") String key,
            @Query("maxResults") String maxResults);

    // PlayList từ id channel
    @GET("youtube/v3/playlists")
    Call<PlayList> playListChannel(
            @Query("pageToken") String pageToken,
            @Query("part") String part,
            @Query("channelId") String channelId,
            @Query("key") String key,
            @Query("maxResults") String maxResults);

    // ItemVideo trong PlayList ở trên
    @GET("youtube/v3/playlistItems")
    Call<PlayListItemVideo> listInPlayList(
            @Query("pageToken") String pageToken,
            @Query("part") String part,
            @Query("playlistId") String playListId,
            @Query("key") String key,
            @Query("maxResults") String maxResults);

    // list dữ liệu mà kênh muốn giới thiệu, có list channels
    @GET("youtube/v3/channelSections")
    Call<ChannelsList> channelsList(
            @Query("part") String part,
            @Query("channelId") String channelId,
            @Query("key") String key);

}
