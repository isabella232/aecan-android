package com.aeternity.aecan.viewModels;

import androidx.lifecycle.ViewModel;

import com.aeternity.aecan.util.SingleLiveEvent;

public class NumberLotModalViewModel extends ViewModel {
    private SingleLiveEvent<Boolean> dismissModalPressed = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> addButtonPressed = new SingleLiveEvent<>();


    public SingleLiveEvent<Boolean> getDismissModalPressed() {
        return dismissModalPressed;
    }

    public SingleLiveEvent<Boolean> getAddButtonPressed() {
        return addButtonPressed;
    }

    public void onDismissModalPressed(){
        dismissModalPressed.setValue(true);
    }

    public void onAddButtonPressed(){
        addButtonPressed.setValue(true);
    }


}
