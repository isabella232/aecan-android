package com.aeternity.aecan.viewModels;

import androidx.lifecycle.MutableLiveData;

public interface ConfirmationDialogInterface {

    MutableLiveData<String> getTitle();

    MutableLiveData<String> getBody();

    MutableLiveData<String> getPositiveButton();

    void onAcceptPressed();

    void onCancelPressed();

}
