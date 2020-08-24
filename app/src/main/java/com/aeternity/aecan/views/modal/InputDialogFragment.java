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
import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.InputDialogBinding;
import com.aeternity.aecan.helpers.DialogHelper;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.viewModels.InputDialogViewModel;

import org.jetbrains.annotations.NotNull;

import static com.aeternity.aecan.util.EditTextValidationUtils.getEditTextTextSafe;
import static com.aeternity.aecan.util.EditTextValidationUtils.validateTextNonEmpty;

public class InputDialogFragment extends CustomDialogFragment {
    private InputDialogBinding binding;
    private InputDialogViewModel viewModel;
    private InputDialogDataCallback dataCallback;
    private InputDialogNetworkCallback networkCallback;

    public static final String TITLE_KEY = "title";
    public static final String INPUT_TYPE_KEY = "inputTypeKey";
    public static final String PREFIX_KEY = "prefix";
    public static final String SUFFIX_KEY = "suffix";
    public static final String BUTTON_TEXT_KEY = "buttonText";
    public static final String USER_INPUT_KEY = "userInput";
    public static final String URL_POST_KEY = "urlKey";

    public InputDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.input_dialog, container, false);
        viewModel = ViewModelProviders.of(this).get(InputDialogViewModel.class);

        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (getArguments() != null) {
            viewModel.setParams(getArguments());
        } else {
            dismiss();
        }

        binding.setInputDialogViewModel(viewModel);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObservers();
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

                case BUTTON_PRESSED:
                    if (validateFields()) {
                        this.setCancelable(false);

                        if (dataCallback != null)
                            dataCallback.onDataSet(getEditTextTextSafe(binding.editTextInput), this.getTag());

                        if (viewModel.hasRepository()) {
                            binding.networkOverlay.setVisibility(View.VISIBLE);
                            viewModel.makePost(getEditTextTextSafe(binding.editTextInput));
                            return;
                        }
                        this.dismiss();
                    } else {

                        Toast.makeText(getContext(), R.string.input_not_valid, Toast.LENGTH_SHORT).show();
                        this.setCancelable(true);
                    }
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


    private boolean validateFields() {
        return validateTextNonEmpty(binding.editTextInput);
    }


    public static InputDialogFragment newInstance(String title, String prefix, String suffix, String buttonText, String userInput, int inputType, String url, InputDialogDataCallback callback, boolean cancelable) {
        InputDialogFragment frag = new InputDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(PREFIX_KEY, prefix);
        args.putString(SUFFIX_KEY, suffix);
        args.putString(BUTTON_TEXT_KEY, buttonText);
        args.putString(USER_INPUT_KEY, userInput);
        args.putInt(INPUT_TYPE_KEY, inputType);
        args.putString(URL_POST_KEY, url);
        frag.setArguments(args);

        frag.dataCallback = callback;
        return frag;
    }

    public static InputDialogFragment newInstance(String title, String prefix, String suffix, String buttonText, String userInput, int inputType, String url, Fragment context) {
        InputDialogFragment frag = new InputDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(PREFIX_KEY, prefix);
        args.putString(SUFFIX_KEY, suffix);
        args.putString(BUTTON_TEXT_KEY, buttonText);
        args.putString(USER_INPUT_KEY, userInput);
        args.putInt(INPUT_TYPE_KEY, inputType);
        args.putString(URL_POST_KEY, url);
        frag.setArguments(args);

        if (context instanceof InputDialogDataCallback) {
            frag.dataCallback = (InputDialogDataCallback) context;
        }

        if (context instanceof InputDialogNetworkCallback) {
            frag.networkCallback = (InputDialogNetworkCallback) context;
        }
        return frag;
    }


    public interface InputDialogDataCallback {

        void onDataSet(String value, String tag);

    }

    public interface InputDialogNetworkCallback {

        void onError(ErrorResponse errorResponse);

        void onSuccess();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof InputDialogDataCallback) {
            this.dataCallback = (InputDialogDataCallback) context;
        }

        if (context instanceof InputDialogNetworkCallback) {
            this.networkCallback = (InputDialogNetworkCallback) context;
        }

    }


}
