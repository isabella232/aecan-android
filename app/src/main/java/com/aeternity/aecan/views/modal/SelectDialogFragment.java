package com.aeternity.aecan.views.modal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.SelectDialogAdapter;
import com.aeternity.aecan.databinding.SelectModalLayoutBinding;
import com.aeternity.aecan.models.dynamic.SelectionOptions;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.viewModels.SelectModalViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SelectDialogFragment extends CustomDialogFragment {

    private SelectModalViewModel viewModel;

    private SelectDialogAdapter adapter;

    public enum Parameters {
        TITLE_KEY("title"),
        BUTTON_TEXT_KEY("buttonText"),
        USER_SELECTION_KEY("userInput"),
        URL_POST_KEY("urlKey"),
        ITEMS_KEY("itemsKey");

        private String key;

        Parameters(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SelectModalLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.select_modal_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(SelectModalViewModel.class);

        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (getArguments() != null) {
            viewModel.setArguments(getArguments());
            if (getArguments().containsKey(Parameters.ITEMS_KEY.getKey())) {

                binding.recyclerViewSelect.setLayoutManager(new LinearLayoutManager(this.getContext()));

                adapter = new SelectDialogAdapter((ArrayList<SelectionOptions>) getArguments().getSerializable(Parameters.ITEMS_KEY.getKey()), R.layout.item_modal_select_view);

                binding.recyclerViewSelect.setAdapter(adapter);
            }
        } else {
            dismiss();
        }

        binding.setSelectModalViewModel(viewModel);

        viewModel.getNetworkError().observe(this, message -> dismiss());

        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
    }

    private void setUpObservers() {

        viewModel.getAction().observe(this, action -> {
            switch (action) {
                case REQUEST_SUCCESS:
                    this.dismiss();
                case DISMISS_PRESSED:
                    this.dismiss();
                    break;
                case BUTTON_PRESSED:
                    if (adapter.getItemCount() > 0 && adapter.getPositionSelected() != -1)
                        viewModel.sendSelected(adapter.getIDSelected());
                    break;
            }
        });


    }

    public static SelectDialogFragment newInstance(String title, String
            buttonText, ArrayList<Integer> userSelection, ArrayList<SelectionOptions> items, String
                                                           url) {
        SelectDialogFragment frag = new SelectDialogFragment();
        Bundle args = new Bundle();
        args.putString(Parameters.TITLE_KEY.getKey(), title);
        args.putString(Parameters.BUTTON_TEXT_KEY.getKey(), buttonText);
        args.putSerializable(Parameters.USER_SELECTION_KEY.getKey(), userSelection);
        args.putString(Parameters.URL_POST_KEY.getKey(), url);
        args.putSerializable(Parameters.ITEMS_KEY.getKey(), items);
        frag.setArguments(args);

        return frag;
    }


}
