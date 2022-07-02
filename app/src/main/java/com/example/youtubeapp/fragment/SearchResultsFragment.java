package com.example.youtubeapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.adapter.SearchResultsAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.model.searchyoutube.ItemsSearch;
import com.example.youtubeapp.model.searchyoutube.Search;
import com.example.youtubeapp.utiliti.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsFragment extends Fragment {
    String q ;
    String pageToken = "";
    ArrayList<SearchItem> listItems;
    ArrayList<SearchItem> listAdd;
    RecyclerView rvListSearch;
    SearchResultsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        getBundle();
        rvListSearch = view.findViewById(R.id.rv_list_search_results);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListSearch.setLayoutManager(linearLayoutManager);
        listItems = new ArrayList<>();
        adapter = new SearchResultsAdapter();
        rvListSearch.setAdapter(adapter);


        return view;
    }

    private void setFirstData() {
        listItems = null;
        callApiSearchResults(pageToken, q, "20");
    }

    private void callApiSearchResults(String nextPageToken, String q, String maxResults) {
        ApiServicePlayList.apiServicePlayList.searchList(
                "snippet",
                q,
                Util.API_KEY
        ).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                String kindType = "", idChannel = "", titleChannel = "", subCount = "",
                urlLogoChannel = "", tvTitleVideo = "", publishAt = "",
                        viewCountVideo = "", idVideo = "", urlThumbnailsVideo = "",
                        videoCountPlayList = "", idPlayList = "";


                Search search = response.body();
                if (search != null) {
                    pageToken = search.getNextPageToken();
                    ArrayList<ItemsSearch> listItem = search.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        kindType = listItem.get(i).getId().getKind();
                        idChannel = listItem.get(i).getId().getChannelId();
                        idPlayList = listItem.get(i).getId().getPlaylistId();
                        idVideo = listItem.get(i).getId().getVideoId();
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
                    }
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

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