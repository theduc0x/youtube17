package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.RelatedVideoAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listvideorelated.ItemsRelated;
import com.example.youtubeapp.model.listvideorelated.RelatedVideo;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedToVideoFragment extends Fragment {
    ArrayList<VideoItem> listItems;
    RelatedVideoAdapter adapter;
    RecyclerView rvRelatedVideo;
    VideoItem itemVideo;
    String idVideoRe = "";
    String pageTokenTo = "";
    RelatedVideo relatedVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_related_to_video, container, false);
        initView(view);
        listItems = new ArrayList<>();
        Bundle bundleRe = getArguments();
            itemVideo =
                    (VideoItem) bundleRe.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
            idVideoRe = itemVideo.getIdVideo();
        callApiRelatedVideo("", idVideoRe, "10");


        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvRelatedVideo.setLayoutManager(linearLayoutManager);
        adapter = new RelatedVideoAdapter(rvRelatedVideo, listItems, new IItemOnClickVideoListener() {
            @Override
            public void OnClickItemVideo(VideoItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        });
        rvRelatedVideo.addItemDecoration(decoration);
        rvRelatedVideo.setAdapter(adapter);

        return view;
    }

    private void initView(View view) {
        rvRelatedVideo = view.findViewById(R.id.rv_list_related_video);
    }


    // Get dữ liệu về
    private void callApiRelatedVideo(String pageToken, String relatedIdVideo, String maxResults) {
        ApiServicePlayList.apiServicePlayList.relatedCall(
                pageToken,
                "snippet",
                relatedIdVideo,
                "video",
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<RelatedVideo>() {
            @Override
            public void onResponse(Call<RelatedVideo> call, Response<RelatedVideo> response) {
                Log.d("ssss", "Success");
                Log.d("ssss", relatedIdVideo);
                String urlThumbnailVideo = "", titleVideo = "", titleChannel = "",
                        timeVideo = "", viewCountVideo = "", commentCount = "",
                        idVideo = "", likeCountVideo = "", descVideo = "",
                        nextPageToken = "", idChannel = "", urlLogoChannel = "";
                relatedVideo = response.body();
                if (relatedVideo != null) {
                    nextPageToken = relatedVideo.getNextPageToken();
                    if (nextPageToken.equals(pageTokenTo)) {
                        return;
                    }
                    pageTokenTo = nextPageToken;
                    int n = 0;
                    ArrayList<ItemsRelated> listItem = relatedVideo.getItems();
                    for (int i = 0; i < listItem.size(); i++) {

                        if (listItem.get(i).getSnippet() == null) {
                            i++;
                        } else {

                            urlThumbnailVideo = listItem.get(i).getSnippet().getThumbnails().getHigh().getUrl();

                            titleVideo = listItem.get(i).getSnippet().getTitle();
                            titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                            idChannel = listItem.get(i).getSnippet().getChannelId();

                            urlLogoChannel = "";

                            idVideo = listItem.get(i).getId().getVideoId();
                            // Lấy ảnh logo Channel
                            callApiChannel(idChannel , listItems, n);


                            timeVideo = listItem.get(i).getSnippet().getPublishedAt();

                            descVideo = listItem.get(i).getSnippet().getDescription();

                            listItems.add(new VideoItem(urlThumbnailVideo,
                                    urlLogoChannel, titleVideo, timeVideo,
                                    titleChannel, viewCountVideo, idVideo,
                                    likeCountVideo, descVideo, idChannel, commentCount));
                            n++;
//                        adapter.notifyItemInserted(i);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RelatedVideo> call, Throwable t) {

            }
        });
    }

    private void callApiChannel(String id, ArrayList<VideoItem> video, int pos) {
        ApiServicePlayList.apiServicePlayList.infoChannel(
                "snippet",
                "contentDetails",
                "statistics",
                id,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                ArrayList<Itemss> listItem = new ArrayList<>();
                Channel channel = response.body();
                if (channel != null) {
                    listItem = channel.getItems();
                    String urlLogooo = listItem.get(0).getSnippet().getThumbnails().getHigh().getUrl();
                    video.get(pos).setUrlLogoChannel(urlLogooo);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ab", t.toString());
            }
        });
    }

}