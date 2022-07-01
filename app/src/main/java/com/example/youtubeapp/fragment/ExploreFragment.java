package com.example.youtubeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtubeapp.R;

public class ExploreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "Explore Fragment", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}