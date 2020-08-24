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
import com.aeternity.aecan.adapters.OneSelectedModalAdapter;
import com.aeternity.aecan.databinding.SelectModalLayoutBinding;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.viewModels.SelectModalViewModel;

import java.util.ArrayList;

public class SingleSelectModalFragment extends CustomDialogFragment {

    private SelectModalLayoutBinding binding;
    private SelectModalViewModel viewModel;
    private OneSelectedModalAdapter oneSelectedModalAdapter;
    private ArrayList<Item> items;
    private String title;
    private String buttonText;
    private MutableLiveData<Integer> selectedIndex = new MutableLiveData<>();
    public static final String TITLE_KEY = "title";
    public static final String BUTTON_TEXT_KEY = "buttonText";
    public static final String ITEMS_KEY = "itemsKey";


    public MutableLiveData<Integer> getSelectedIndex() {
        return selectedIndex;
    }


    public static SingleSelectModalFragment newInstance(String title, String buttonText, ArrayList<Item> items) {
        SingleSelectModalFragment singleSelectModalFragment = new SingleSelectModalFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(BUTTON_TEXT_KEY, buttonText);
        args.putSerializable(ITEMS_KEY, items);
        singleSelectModalFragment.setArguments(args);
        return singleSelectModalFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE_KEY);
            buttonText = getArguments().getString(BUTTON_TEXT_KEY);
            items = (ArrayList<Item>) getArguments().getSerializable(ITEMS_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.select_modal_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(SelectModalViewModel.class);
        binding.setSelectModalViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
        createdRecyclerView();
    }

    private void setupObservers() {
        viewModel.setArguments(title, buttonText, items, "");
        binding.textViewModalTitle.setText(title);
        binding.buttonModalSelect.setText(buttonText);
        viewModel.getAction().observe(this, action -> {
            switch (action) {
                case DISMISS_PRESSED:
                    dismiss();
                    break;
                case BUTTON_PRESSED:
                    addVariety();
                    break;
            }
        });

        viewModel.getNetworkError().observe(this, message -> dismiss());
    }

    private void addVariety() {
        getSelectedIndex().setValue(oneSelectedModalAdapter.getPositionSelected());
        dismiss();
    }

    private void createdRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewSelect.setLayoutManager(linearLayoutManager);
        oneSelectedModalAdapter = new OneSelectedModalAdapter(viewModel.getItems().getValue(), R.layout.item_modal_select_view);
        binding.recyclerViewSelect.setAdapter(oneSelectedModalAdapter);
    }


}
