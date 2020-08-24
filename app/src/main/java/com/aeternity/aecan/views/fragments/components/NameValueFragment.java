package com.aeternity.aecan.views.fragments.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ListAdapter;
import com.aeternity.aecan.databinding.ComponentCardWithBackgroundBinding;
import com.aeternity.aecan.models.dynamic.DynamicItem;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NameValueFragment extends BaseFragment {

    public static final String ITEMS_KEY = "items";

    ComponentCardWithBackgroundBinding binding;
    ListAdapter adapter;

    public NameValueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.component_card_with_background, container, false);

        if (getArguments() != null && getArguments().containsKey(ITEMS_KEY)) {
            binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

            ArrayList<?> elements = (ArrayList<?>) getArguments().get(ITEMS_KEY);

            adapter = new ListAdapter(elements, com.aeternity.aecan.BR.keyValueItem, R.layout.component_key_value_array);

            binding.recycler.setAdapter(adapter);

        }
        return binding.getRoot();
    }

    public static NameValueFragment newInstance(ArrayList<DynamicItem> items) {
        NameValueFragment frag = new NameValueFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS_KEY, items);
        frag.setArguments(args);
        return frag;
    }


}
