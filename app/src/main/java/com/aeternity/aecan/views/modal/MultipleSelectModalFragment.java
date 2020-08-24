package com.aeternity.aecan.views.modal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.MultiSelectedModalAdapter;
import com.aeternity.aecan.databinding.SelectModalLayoutBinding;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.viewModels.SelectModalViewModel;

import java.util.ArrayList;

public class MultipleSelectModalFragment extends CustomDialogFragment {
    private SelectModalLayoutBinding binding;
    private SelectModalViewModel viewModel;
    private MultiSelectedModalAdapter multiSelectedModalAdapter;
    private MutableLiveData<ArrayList<Integer>> beacons = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Integer>> beaconsIds = new MutableLiveData<>();
    private static final String TITLE_KEY = "title";
    private static final String BUTTON_TEXT_KEY = "buttonText";
    private static final String ITEMS_KEY = "itemsKey";
    private static final String URL_KEY = "url_key";

    public MultipleSelectModalFragment() {
    }

    public MutableLiveData<ArrayList<Integer>> getBeacons() {
        return beacons;
    }

    public MutableLiveData<ArrayList<Integer>> getBeaconsIds() {
        return beaconsIds;
    }

    public static MultipleSelectModalFragment newInstance(String title, String buttonText, ArrayList<Item> items, String url) {
        MultipleSelectModalFragment multipleSelectModalFragment = new MultipleSelectModalFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(BUTTON_TEXT_KEY, buttonText);
        args.putSerializable(ITEMS_KEY, items);
        args.putString(URL_KEY, url);

        multipleSelectModalFragment.setArguments(args);
        return multipleSelectModalFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.select_modal_layout, container, false);
        binding.setSelectModalViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SelectModalViewModel.class);

        if (getArguments() != null) {
            viewModel.setArguments(
                    getArguments().getString(TITLE_KEY),
                    getArguments().getString(BUTTON_TEXT_KEY),
                    (ArrayList<Item>) getArguments().getSerializable(ITEMS_KEY),
                    getArguments().getString(URL_KEY)
            );
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
        createdRecyclerView();
    }

    private void setUpObservers() {
        viewModel.getAction().observe(this, action -> {
            switch (action) {
                case DISMISS_PRESSED:
                    dismiss();
                    break;
                case BUTTON_PRESSED:
                    addBeacon();
                    break;
            }
        });
    }

    private void addBeacon() {

        ArrayList<Integer> ids = new ArrayList<>();

        for (Integer integer : multiSelectedModalAdapter.getPositionSelectedItems()) {
            ids.add(multiSelectedModalAdapter.getItemIdAt(integer));
        }
        beaconsIds.setValue(ids);

        getBeacons().setValue(multiSelectedModalAdapter.getPositionSelectedItems());
        dismiss();
    }


    public void createdRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewSelect.setLayoutManager(linearLayoutManager);
        multiSelectedModalAdapter = new MultiSelectedModalAdapter(viewModel.getItems().getValue(), R.layout.item_modal_select_view);
        multiSelectedModalAdapter.notifyDataSetChanged();
        binding.recyclerViewSelect.setAdapter(multiSelectedModalAdapter);
    }


}
