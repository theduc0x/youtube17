package com.example.youtubeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeapp.R;
import com.example.youtubeapp.adapter.HintAdapter;
import com.example.youtubeapp.api.ApiServiceHintSearch;
import com.example.youtubeapp.my_interface.IItemOnClickHintListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchFragment extends Fragment {
    private ImageButton ibBack;
    EditText etSearch;
    RecyclerView rvListHint;
    ArrayList<String> listHint;
    HintAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        backHome();
        listHint = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rvListHint.setLayoutManager(linearLayoutManager);
        adapter = new HintAdapter(new IItemOnClickHintListener() {
            @Override
            public void onClickListener(String s) {
                etSearch.setText(s);
            }
        });
        rvListHint.setAdapter(adapter);

        adapter.setData(listHint);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = etSearch.getText().toString();
                callApiHintSearch(ss);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String ss = etSearch.getText().toString();
//                callApiHintSearch(ss);
            }
        });
//        callApiHintSearch("thích");
        return view;
    }

    private void initView(View view) {
        ibBack = view.findViewById(R.id.ib_back_search);
        etSearch = view.findViewById(R.id.et_search);
        rvListHint = view.findViewById(R.id.rv_list_hint_search);
    }
    // Quay trở lại trước đó
    private void backHome() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void callApiHintSearch(String hint) {
        listHint.clear();
        String url = "http://suggestqueries.google.com/complete/search?client=youtube&ds=yt&client=firefox&q="  + hint + "";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("duc123", response.toString());
                try {
                    Object string = response.get(1);
                    Log.d("duc11", string.toString());
                    String s = string.toString();
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String string1 = (String) jsonArray.get(i);
                        listHint.add(string1);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}