package com.aeternity.aecan.views.fragments.components;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ExpandableItemAdapter;
import com.aeternity.aecan.databinding.FragmentExpandableItemBinding;
import com.aeternity.aecan.models.ExpandableItem;
import com.aeternity.aecan.models.dynamic.DynamicItem;
import com.aeternity.aecan.viewModels.ExpandableItemViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass
 */
public class ExpandableItemFragment extends Fragment {
    private FragmentExpandableItemBinding binding;
    private ExpandableItemViewModel viewModel;
    private ExpandableItemAdapter adapter;
    public static final String ITEMS = "items";
    public static final String TITLE = "title";

    public ExpandableItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_expandable_item, container, false);

        binding.title.setText(getArguments().getString(TITLE));

        viewModel = ViewModelProviders.of(this).get(ExpandableItemViewModel.class);
        return binding.getRoot();
    }

    public static ExpandableItemFragment newInstance(ArrayList<DynamicItem> items, String title) {
        ExpandableItemFragment fragment = new ExpandableItemFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ITEMS, items);
        bundle.putString(TITLE, title);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            viewModel.setArguments(getArguments());
        }
        setUpRecycler(generateItems(viewModel.getItems()));
    }

    private ArrayList<ExpandableItem> generateItems(ArrayList<DynamicItem> dynamicItems) {
        ArrayList<ExpandableItem> items = new ArrayList<>();
        for (DynamicItem item : dynamicItems) {
            items.add(new ExpandableItem(item));
        }
        return items;
    }


    private void setUpRecycler(ArrayList<ExpandableItem> expandableItems) {
        adapter = new ExpandableItemAdapter(expandableItems, BR.expandableItem, R.layout.item_expandable);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerExpandableItems.setLayoutManager(linearLayoutManager);
        binding.recyclerExpandableItems.setAdapter(adapter);
        adapter.getButtonPressed().observe(this, expandableItem -> {
            if (expandableItem.getItemAction() != null) {
                DialogFragment dialogFragment = expandableItem.getItemAction().getFragment(this);
                if (dialogFragment != null)
                    dialogFragment.show(getChildFragmentManager(), expandableItem.toString());
            }
        });
    }
}
