package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.VideoChannelAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listvideorelated.ItemsRelated;
import com.example.youtubeapp.model.listvideorelated.RelatedVideo;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.example.youtubeapp.my_interface.PaginationScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelVideoFragment extends Fragment {
    RecyclerView rvListVideo;
    ArrayList<VideoChannelItem> listItems;
    ArrayList<VideoChannelItem> list;
    VideoChannelAdapter adapter;
    String idChannel;
    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;

    RelatedVideo video;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_video, container, false);
        rvListVideo = view.findViewById(R.id.rv_list_video_channel);
        listItems = new ArrayList<>();
        // lấy idChannel
        getBundle();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListVideo.setLayoutManager(linearLayoutManager);
        adapter = new VideoChannelAdapter(new IItemOnClickVideoListener() {
            @Override
            public void OnClickItemVideo(VideoItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        });
//        rvListVideo.setNestedScrollingEnabled(false);
        rvListVideo.setAdapter(adapter);
        // Gọi page đầu tiên trong recycleview
        setFirstData();
        // Sự kiện load data khi cuộn đến cuối page
        rvListVideo.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currenPage += 1;
                loadNextPage();
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        return view;
    }
//     Load data page one
    private void setFirstData() {
        listItems = null;
        callApiVideoChannel(pageToken, idChannel, "10");
    }
    // Set propress bar load data
    private void setProgressBar() {
        if (currenPage < totalPage) {
            adapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }
    // Load dữ liệu của page tiếp theo
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Load Page" + currenPage, Toast.LENGTH_SHORT).show();
                callApiVideoChannel(pageToken, idChannel, "10");
                isLoading = false;
            }
        },1000);
    }

    private void callApiVideoChannel(String nextPageToken, String channelId, String maxResults) {
        list = new ArrayList<>();
        ApiServicePlayList.apiServicePlayList.videoUpdateNews(
                nextPageToken,
                "snippet",
                "id",
                channelId,
                "date",
                "video",
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<RelatedVideo>() {
            @Override
            public void onResponse(Call<RelatedVideo> call, Response<RelatedVideo> response) {
                String urlThumbnails = "", titleVideo = "", publishAt = "",
                        viewCount = "", idVideo = "";

                video = response.body();
                if (video != null) {
                    totalPage = (int) video.getPageInfo().getTotalResults() / 10;
                    Log.d("page", String.valueOf(totalPage));
                    Log.d("page", String.valueOf(video.getPageInfo().getTotalResults()));
                    pageToken = video.getNextPageToken();
                    ArrayList<ItemsRelated> listItem = video.getItems();

                    for (int i = 0;i <listItem.size(); i++) {
                        idVideo = listItem.get(i).getId().getVideoId();
//                        Log.d("duckaka", idVideo);
                        if (listItem.get(i).getSnippet().getThumbnails().getMaxres() != null) {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getMaxres().getUrl();
                        } else if (listItem.get(i).getSnippet().getThumbnails().getStandard() != null) {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getStandard().getUrl();
                        }else {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getHigh().getUrl();
                        }
                        publishAt = listItem.get(i).getSnippet().getPublishedAt();
                        Log.d("duckakaka", titleVideo);
                        Log.d("duckakaka", publishAt);
                        titleVideo = listItem.get(i).getSnippet().getTitle();

                        callApiViewCountVideo(idVideo, list, i);

                        list.add(new VideoChannelItem(urlThumbnails, titleVideo,
                                publishAt, "", idVideo));
//                        Log.d("duckaka1", list.get(0).getIdVideo());
                    }
                    if (listItems == null) {
                        listItems = list;
                        adapter.setData(listItems);
                        setProgressBar();
                    } else {
                        adapter.removeFooterLoading();
                        listItems.addAll(list);
                        adapter.notifyDataSetChanged();
                        setProgressBar();
                    }

                }
            }
            @Override
            public void onFailure(Call<RelatedVideo> call, Throwable t) {
                        Log.d("error", t.toString());
            }
        });
    }

    private void callApiViewCountVideo(String idVideo, ArrayList<VideoChannelItem> listItemV,
                                       int pos) {
        ApiServicePlayList.apiServicePlayList.detailVideo(
                "snippet",
                "statistics",
                idVideo,
                Util.API_KEY
        ).enqueue(new Callback<DetailVideo>() {
            @Override
            public void onResponse(Call<DetailVideo> call, Response<DetailVideo> response) {
                String viewCount = "";
                DetailVideo video = response.body();
                if (video != null) {
                    ArrayList<ItemVideo> listItem = video.getItems();
                    for (int i = 0; i <listItem.size(); i++ ) {
                        viewCount = listItem.get(0).getStatistics().getViewCount();
                        listItemV.get(pos).setViewCount(viewCount);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idChannel = bundle.getString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER);
        }
    }
}