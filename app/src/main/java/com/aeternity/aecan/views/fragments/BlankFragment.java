package com.aeternity.aecan.views.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aeternity.aecan.R;

public class BlankFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String badge;
    private String title;


    public BlankFragment() {
        // Required empty public constructor
    }

    public BlankFragment(String badge, String title) {
        this.badge = badge;
        this.title = title;
    }

    public String getBadge() {
        return badge;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

}
