package com.aeternity.aecan.viewModels;

import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    private SingleLiveEvent<ViewModelAction> viewModelAction = new SingleLiveEvent<>();

    public enum ViewModelAction {
        BUTTON_CREATE_MODAL_PRESSED
    }

    public SingleLiveEvent<ViewModelAction> getViewModelAction() {
        return viewModelAction;
    }

    public void onButtonCreateModalPressed() {
        viewModelAction.setValue(ViewModelAction.BUTTON_CREATE_MODAL_PRESSED);
    }
}
