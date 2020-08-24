package com.aeternity.aecan.views.modal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.FragmentDobleButtonModalV2Binding;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.viewModels.ConfirmDialogViewModel;

import org.jetbrains.annotations.NotNull;

public class ConfirmDialogFragment extends CustomDialogFragment {

    private ConfirmDialogViewModel viewModel;
    private FragmentDobleButtonModalV2Binding binding;
    private ConfirmDialogNetworkCallback networkCallback;

    public static final String TITLE_KEY = "title";
    public static final String BODY_KEY = "body";
    public static final String URL_POST_KEY = "urlKey";
    public static final String POSITIVE_BUTTON_TEXT = "positiveButtonText";

    public ConfirmDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doble_button_modal_v2, container, false);
        viewModel = ViewModelProviders.of(this).get(ConfirmDialogViewModel.class);

        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.setConfirmationDialogInterface(viewModel);

        if (getArguments() != null) {
            viewModel.setParams(getArguments());
            setUpObservers();
        } else {
            dismiss();
        }

        return binding.getRoot();
    }

    private void setUpObservers() {

        viewModel.getNetworkError().observe(this, error -> {
            binding.networkOverlay.setVisibility(View.GONE);
            this.setCancelable(true);
            if (networkCallback != null) networkCallback.onError(error);
            this.dismiss();
        });


        viewModel.getAction().observe(this, action -> {
            switch (action) {
                case ACCEPT_BUTTON_PRESSED:
                    this.setCancelable(false);
                    binding.networkOverlay.setVisibility(View.VISIBLE);
                    break;

                case CANCEL_BUTTON_PRESSED:
                    this.dismiss();
                    break;

                case REQUEST_SUCCESS:
                    this.setCancelable(true);
                    binding.networkOverlay.setVisibility(View.GONE);
                    if (networkCallback != null) networkCallback.onSuccess();
                    this.dismiss();
                    break;
            }

        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public static ConfirmDialogFragment newInstance(String title, String body, String url, String positiveButtonText, Fragment context) {
        ConfirmDialogFragment frag = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(URL_POST_KEY, url);
        args.putString(BODY_KEY, body);
        args.putString(POSITIVE_BUTTON_TEXT, positiveButtonText);
        frag.setArguments(args);

        if (context instanceof ConfirmDialogNetworkCallback) {
            frag.networkCallback = (ConfirmDialogNetworkCallback) context;
        }

        return frag;
    }

    public interface ConfirmDialogNetworkCallback {

        void onError(ErrorResponse errorResponse);

        void onSuccess();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ConfirmDialogNetworkCallback) {
            this.networkCallback = (ConfirmDialogNetworkCallback) context;
        }

    }


}
