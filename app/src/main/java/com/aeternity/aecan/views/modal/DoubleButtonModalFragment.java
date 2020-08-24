package com.aeternity.aecan.views.modal;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.FragmentDobleButtonModalBinding;
import com.aeternity.aecan.viewModels.StageDetailModalViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoubleButtonModalFragment extends CustomDialogFragment {

    private String text;
    private ArrayList<String> greenTextArray;
    private FragmentDobleButtonModalBinding binding;
    private StageDetailModalViewModel viewModel;
    private MutableLiveData<Boolean> rightButtonPressed = new MutableLiveData<>();


    public DoubleButtonModalFragment(String text, ArrayList<String> greenTextArray) {
        this.text = text;
        this.greenTextArray = greenTextArray;
    }

    public MutableLiveData<Boolean> getRightButtonPressed() {
        return rightButtonPressed;
    }

    public void setRightButtonPressed(){
        getRightButtonPressed().postValue(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doble_button_modal, container, false);
        viewModel = ViewModelProviders.of(this).get(StageDetailModalViewModel.class);
        binding.setViewModel(viewModel);
        binding.textViewAlert.setText(text);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.setArguments(text,greenTextArray);
        viewModel.getDismiss().observe(this, aBoolean -> dismiss());
        viewModel.getRightButtonClicked().observe(this,aBoolean -> setRightButtonPressed());
    }



}
