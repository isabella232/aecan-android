package com.aeternity.aecan.viewModels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Observable;

public class StageDetailModalViewModel extends ViewModel {
    private ObservableField<String> content = new ObservableField<>();
    private ObservableField<ArrayList<String>> greenTextArray = new ObservableField<>();
    private MutableLiveData<Boolean> rightButtonClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> dismiss = new MutableLiveData<>();
    private MutableLiveData<Boolean> centerButtonClicked = new MutableLiveData<>();

    public MutableLiveData<Boolean> getDismiss() {
        return dismiss;
    }

    public MutableLiveData<Boolean> getRightButtonClicked() {
        return rightButtonClicked;
    }

    public void onRightButtonClicked() {
        getRightButtonClicked().postValue(true);
    }

    public MutableLiveData<Boolean> getCenterButtonClicked() {
        return centerButtonClicked;
    }

    public void onCenterButtonClicked() {
        getCenterButtonClicked().setValue(true);
    }

    public ObservableField<String> getContent() {
        return content;
    }

    public ObservableField<ArrayList<String>> getGreenTextArray() {
        return greenTextArray;
    }

    public void onDismiss() {
        dismiss.setValue(true);
    }

    public void setArguments(String text, ArrayList<String> greenTextArray) {
        getContent().set(text);
        getGreenTextArray().set(greenTextArray);
    }
}
