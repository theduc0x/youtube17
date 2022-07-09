package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.activitys.ChannelActivity;
import com.example.youtubeapp.activitys.MainActivity;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.activitys.VideoPlayListActivity;
import com.example.youtubeapp.adapter.SearchResultsAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.model.listplaylistvideochannel.Items;
import com.example.youtubeapp.model.listplaylistvideochannel.PlayList;
import com.example.youtubeapp.model.searchyoutube.ItemsSearch;
import com.example.youtubeapp.model.searchyoutube.Search;
import com.example.youtubeapp.my_interface.IItemOnClickChannelListener;
import com.example.youtubeapp.my_interface.IItemOnClickPlayListSearchListener;
import com.example.youtubeapp.my_interface.IItemOnClickVideoSearchListener;
import com.example.youtubeapp.my_interface.PaginationScrollListener;
import com.example.youtubeapp.utiliti.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsFragment extends Fragment {
    String q;
    String pageToken = "";
    ArrayList<SearchItem> listItems;
    ArrayList<SearchItem> listAdd;
    RecyclerView rvListSearch;
    SearchResultsAdapter adapter;
    TextView tvSearch;
    MainActivity mainActivity;
    ImageView ivOpenSearch;
    ImageButton ibBack;

    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        removeSearchFragment();
        getBundle();
        ibBack = view.findViewById(R.id.ib_back_search_results);
        tvSearch = view.findViewById(R.id.tv_search_results);
        mainActivity = (MainActivity) getActivity();
        ivOpenSearch = view.findViewById(R.id.iv_open_search_new);
        tvSearch.setText(q);
        rvListSearch = view.findViewById(R.id.rv_list_search_results);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListSearch.setLayoutManager(linearLayoutManager);
        listItems = new ArrayList<>();
        adapter = new SearchResultsAdapter(new IItemOnClickChannelListener() {
            @Override
            public void onClickOpenChannel(String idChannel, String titleChannel) {
                Intent openToChannel = new Intent(getActivity(), ChannelActivity.class);
                openToChannel.putExtra(Util.EXTRA_ID_CHANNEL_TO_CHANNEL, idChannel);
                openToChannel.putExtra(Util.EXTRA_TITLE_CHANNEL_TO_CHANNEL, titleChannel);
                startActivity(openToChannel);
            }
        }, new IItemOnClickPlayListSearchListener() {
            @Override
            public void onCLickItemPlayListS(SearchItem item) {
                Intent openToChannel = new Intent(getActivity(), VideoPlayListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_PLAY_LIST_TO_VIDEO_PLAY_LIST, item);
                bundle.putString(Util.EXTRA_KEY_ITEM_PLAYLIST, "Search");
                openToChannel.putExtras(bundle);
                startActivity(openToChannel);
            }
        }, new IItemOnClickVideoSearchListener() {
            @Override
            public void OnClickItemVideoS(SearchItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                bundle.putString(Util.EXTRA_KEY_ITEM_VIDEO, "Search");
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        });
        rvListSearch.setAdapter(adapter);
        setFirstData();
        // sự kiện cuốn recycleview
        rvListSearch.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

        // Mở search (new)
        ivOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addFragmentSearch("");
            }
        });
        // Quay trở lại phần search lúc đầu
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addFragmentSearch(tvSearch.getText().toString());
            }
        });
        // Nút quay trở lại
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        return view;
    }

    //  Xóa fragment search để khi quay trở lại thì trở lại trang main
    public void removeSearchFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        SearchFragment searchFragment
                = (SearchFragment) getChildFragmentManager().findFragmentByTag("fragSearch");
        if (searchFragment != null) {
            transaction.remove(this);
            transaction.commit();
        }
    }

    ;


    private void setFirstData() {
        listItems = null;
        callApiSearchResults(pageToken, q, "20");
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
                callApiSearchResults(pageToken, q, "10");
                isLoading = false;
            }
        }, 1000);
    }

    private void callApiSearchResults(String nextPageToken, String q, String maxResults) {
        ApiServicePlayList.apiServicePlayList.searchList(
                nextPageToken,
                "snippet",
                q,
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                listAdd = new ArrayList<>();
                String kindType = "", idChannel = "", titleChannel = "", subCount = "",
                        urlLogoChannel = "", tvTitleVideo = "", publishAt = "",
                        viewCountVideo = "", idVideo = "", urlThumbnailsVideo = "",
                        videoCountPlayList = "", idPlayList = "", videoCountChannel = "";
                Log.d("hahi", response.toString());

                Search search = response.body();
                if (search != null) {
                    pageToken = search.getNextPageToken();
                    ArrayList<ItemsSearch> listItem = search.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        kindType = listItem.get(i).getId().getKind();
                        if (kindType.equals("youtube#channel")) {
                            idChannel = listItem.get(i).getId().getChannelId();
                            callApiChannelFull(idChannel, listAdd, i);
                        } else if (kindType.equals("youtube#playlist")) {
                            idPlayList = listItem.get(i).getId().getPlaylistId();
                            idChannel = listItem.get(i).getSnippet().getChannelId();
                            callApiPlayList(idPlayList, listAdd, i);
                        } else if (kindType.equals("youtube#video")) {
                            idVideo = listItem.get(i).getId().getVideoId();
                            idChannel = listItem.get(i).getSnippet().getChannelId();
                            callApiViewCountVideo(idVideo, listAdd, i);
                            callApiChannelId(idChannel, listAdd, i);
                            Log.d("duc1123", idVideo);
                        }

                        titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                        urlLogoChannel = listItem.get(i).getSnippet().getThumbnails().getHigh().getUrl();
                        if (listItem.get(i).getSnippet().getThumbnails().getMaxres() != null) {
                            urlLogoChannel = listItem.get(i).getSnippet().getThumbnails()
                                    .getMaxres().getUrl();
                            urlThumbnailsVideo = listItem.get(i).getSnippet().getThumbnails()
                                    .getMaxres().getUrl();
                        } else if (listItem.get(i).getSnippet().getThumbnails().getStandard() != null) {
                            urlLogoChannel = listItem.get(i).getSnippet().getThumbnails()
                                    .getStandard().getUrl();
                            urlThumbnailsVideo = listItem.get(i).getSnippet().getThumbnails()
                                    .getStandard().getUrl();
                        } else {
                            urlLogoChannel = listItem.get(i).getSnippet().getThumbnails()
                                    .getHigh().getUrl();
                            urlThumbnailsVideo = listItem.get(i).getSnippet().getThumbnails()
                                    .getHigh().getUrl();
                        }
                        tvTitleVideo = listItem.get(i).getSnippet().getTitle();
                        publishAt = listItem.get(i).getSnippet().getPublishedAt();

                        listAdd.add(new SearchItem(
                                kindType, idChannel, titleChannel, subCount, videoCountChannel,
                                urlLogoChannel, tvTitleVideo, publishAt, viewCountVideo, idVideo,
                                "", "", "", urlThumbnailsVideo,
                                videoCountPlayList, idPlayList
                        ));
                    }
                    if (listItems == null) {
                        listItems = listAdd;
                        adapter.setData(listItems);
                        setProgressBar();
                    } else {
                        adapter.removeFooterLoading();
                        listItems.addAll(listAdd);
                        adapter.notifyDataSetChanged();
                        setProgressBar();
                    }

                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });

    }

    private void callApiViewCountVideo(String idVideo, ArrayList<SearchItem> item, int pos) {
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
                    for (int i = 0; i < listItem.size(); i++) {
                        viewCount = listItem.get(0).getStatistics().getViewCount();
                        String likeCount = listItem.get(0).getStatistics().getLikeCount();
                        String commentCount = listItem.get(0).getStatistics().getCommentCount();
                        String descVideo = listItem.get(0).getSnippet().getDescription();
                        item.get(pos).setViewCountVideo(viewCount);
                        item.get(pos).setCommentCount(commentCount);
                        item.get(pos).setLikeCountVideo(likeCount);
                        item.get(pos).setDescVideo(descVideo);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });
    }

    public void callApiChannelId(String idChannel, ArrayList<SearchItem> listItemS, int pos) {
        ApiServicePlayList.apiServicePlayList.infoChannel(
                "snippet",
                null,
                null,
                idChannel,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                String urlLogoChannel = "";
                Channel channel = response.body();
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    Itemss item = listItem.get(0);
                    if (item.getSnippet().getThumbnails().getHigh() == null) {
                        urlLogoChannel = item.getSnippet().getThumbnails().getMedium().getUrl();
                    } else {
                        urlLogoChannel = item.getSnippet().getThumbnails().getHigh().getUrl();
                    }
                    listItemS.get(pos).setUrlLogoChannel(urlLogoChannel);
//                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(pos);
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {

            }
        });
    }

    //           Api thông tin channel;
    public void callApiChannelFull(String idChannel1, ArrayList<SearchItem> listItemS, int pos) {
        ApiServicePlayList.apiServicePlayList.infoChannelFull(
                "contentDetails",
                "snippet",
                "statistics",
                "topicDetails",
                "brandingSettings",
                idChannel1,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                String subCount = "",
                        videoCount = "";
                boolean isCheckHideSub = false;
                Channel channel = response.body();
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    Itemss item = listItem.get(0);
                    isCheckHideSub = item.getStatistics().isHiddenSubscriberCount();
                    if (isCheckHideSub) {
                        subCount = "";
                    } else {
                        subCount = item.getStatistics().getSubscriberCount();
                    }
                    videoCount = item.getStatistics().getVideoCount();

                    listItemS.get(pos).setSubCount(subCount);
                    listItemS.get(pos).setVideoCount(videoCount);


                    adapter.notifyItemChanged(pos);
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ducak", t.toString());
            }
        });
    }

    //  Api của play list
    private void callApiPlayList(String idPlayList, ArrayList<SearchItem> listItemS, int pos) {
        ApiServicePlayList.apiServicePlayList.playListChannel(
                "",
                "contentDetails,id,localizations,player,snippet,status",
                null,
                idPlayList,
                Util.API_KEY,
                "5"
        ).enqueue(new Callback<PlayList>() {
            @Override
            public void onResponse(Call<PlayList> call, Response<PlayList> response) {
                String videoCount = "";

                PlayList playList = response.body();

                if (playList != null) {
                    ArrayList<Items> listItem = playList.getItems();
                    videoCount = String.valueOf(listItem.get(0).getContentDetails().getItemCount());
                    listItemS.get(pos).setVideoCountPlayList(videoCount);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PlayList> call, Throwable t) {

            }
        });
    }

    private void getBundle() {
        Bundle bundleRe = getArguments();
        if (bundleRe != null) {
            q = bundleRe.getString(Util.BUNDLE_EXTRA_Q, "");
        }
    }
}