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
import com.aeternity.aecan.databinding.DatePickerDialogBinding;
import com.aeternity.aecan.databinding.InputDialogBinding;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.DateUtils;
import com.aeternity.aecan.viewModels.DatePickerDialogViewModel;
import com.aeternity.aecan.viewModels.InputDialogViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import static com.aeternity.aecan.util.EditTextValidationUtils.getEditTextTextSafe;
import static com.aeternity.aecan.util.EditTextValidationUtils.validateTextNonEmpty;

public class DatePickerDialogFragment extends CustomDialogFragment {
    private DatePickerDialogBinding binding;
    private DatePickerDialogViewModel viewModel;
    private DatePickerDialogDataCallback dataCallback;
    private DatePickerDialogNetworkCallback networkCallback;
    public static final String TITLE_KEY = "title";
    public static final String BUTTON_TEXT_KEY = "buttonText";
    public static final String USER_INPUT_KEY = "userInput";
    public static final String URL_POST_KEY = "urlKey";

    public DatePickerDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.date_picker_dialog, container, false);
        viewModel = ViewModelProviders.of(this).get(DatePickerDialogViewModel.class);

        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (getArguments() != null) {
            viewModel.setParams(getArguments());
        } else {
            dismiss();
        }
        Calendar calendar = Calendar.getInstance();
        binding.setDatePickerViewModel(viewModel);
        viewModel.setUserInput(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        binding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), (datePicker, year, month, dayOfMonth)
                -> viewModel.setUserInput(year + "-" + month + "-" + dayOfMonth));

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
                    if (this.dataCallback != null) {
                        this.dismiss();
                        dataCallback.onDataSet(getDateAsYYYYMMDD(), getTag());
                    } else {
                        this.setCancelable(false);
                        binding.networkOverlay.setVisibility(View.VISIBLE);
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

    public static DatePickerDialogFragment newInstance(String title, String buttonText, String userInput, String url, Fragment context) {
        DatePickerDialogFragment frag = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(BUTTON_TEXT_KEY, buttonText);
        args.putString(USER_INPUT_KEY, userInput);
        args.putString(URL_POST_KEY, url);
        frag.setArguments(args);

        if (context instanceof DatePickerDialogDataCallback) {
            frag.dataCallback = (DatePickerDialogDataCallback) context;
        }

        if (context instanceof DatePickerDialogNetworkCallback) {
            frag.networkCallback = (DatePickerDialogNetworkCallback) context;
        }
        return frag;
    }


    public interface DatePickerDialogDataCallback {

        void onDataSet(String value, String tag);

    }

    public interface DatePickerDialogNetworkCallback {

        void onError(ErrorResponse errorResponse);

        void onSuccess();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof DatePickerDialogDataCallback) {
            this.dataCallback = (DatePickerDialogDataCallback) context;
        }

        if (context instanceof DatePickerDialogNetworkCallback) {
            this.networkCallback = (DatePickerDialogNetworkCallback) context;
        }

    }

    private String getDateAsYYYYMMDD() {
        return
                binding.datePicker.getYear() + "-" +
                        binding.datePicker.getMonth() + 1 + "-" +
                        binding.datePicker.getDayOfMonth();
    }


}
