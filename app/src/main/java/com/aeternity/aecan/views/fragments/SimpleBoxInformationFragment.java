package com.aeternity.aecan.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.MultiSelectedModalAdapter;
import com.aeternity.aecan.adapters.SimpleBoxInformationAdapter;
import com.aeternity.aecan.databinding.BoxSimpleInformationFragmentBinding;

import java.util.ArrayList;

public class SimpleBoxInformationFragment extends Fragment {
    private BoxSimpleInformationFragmentBinding binding;
    private SimpleBoxInformationAdapter adapter;
    private ArrayList<String> informations = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public SimpleBoxInformationFragment() {
    }

    public SimpleBoxInformationFragment(ArrayList<String> informations) {
        this.informations = informations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.box_simple_information_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpTexts();
    }

    private void setUpTexts() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewSimpleInformation.setLayoutManager(linearLayoutManager);
        adapter = new SimpleBoxInformationAdapter(informations);
        binding.recyclerViewSimpleInformation.setAdapter(adapter);
    }
}
