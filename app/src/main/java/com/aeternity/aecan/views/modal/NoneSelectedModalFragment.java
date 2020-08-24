package com.aeternity.aecan.views.modal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.NoneSelectedModalAdapter;
import com.aeternity.aecan.databinding.SelectModalLayoutBinding;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.viewModels.SelectModalViewModel;

import java.util.ArrayList;

public class NoneSelectedModalFragment extends CustomDialogFragment {
    private SelectModalLayoutBinding binding;
    private SelectModalViewModel viewModel;
    private ArrayList<Item> items = new ArrayList<>();
    private NoneSelectedModalAdapter noneSelectedModalAdapter;

    private String title;
    private String buttonText;
    public static final String TITLE_KEY = "title";
    public static final String BUTTON_TEXT_KEY = "buttonText";
    public static final String ITEMS_KEY = "itemsKey";


    public static NoneSelectedModalFragment newInstance(String title, String buttonText, ArrayList<Item> items) {
        NoneSelectedModalFragment noneSelectedModal = new NoneSelectedModalFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(BUTTON_TEXT_KEY, buttonText);
        args.putSerializable(ITEMS_KEY, items);
        noneSelectedModal.setArguments(args);
        return noneSelectedModal;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.select_modal_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(SelectModalViewModel.class);
        binding.setSelectModalViewModel(viewModel);
        return binding.getRoot();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
        createdRecyclerView();
    }

    private void setUpObservers() {
        viewModel.setArguments(title, buttonText, items, "");
        binding.textViewModalTitle.setText(title);
        binding.buttonModalSelect.setText(buttonText);
        viewModel.getAction().observe(this, action -> {
            dismiss();
        });
    }


    public void createdRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewSelect.setLayoutManager(linearLayoutManager);
        noneSelectedModalAdapter = new NoneSelectedModalAdapter(viewModel.getItems().getValue(), R.layout.item_modal_select_view);
        binding.recyclerViewSelect.setAdapter(noneSelectedModalAdapter);
    }


}
