package com.aeternity.aecan.viewModels;

import com.aeternity.aecan.network.repositories.RecoveryPasswordRepository;
import com.aeternity.aecan.network.requests.RecoveryPasswordRequest;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

public class RecoveryPasswordViewModel extends BaseViewModel {
    private RecoveryPasswordRepository recoveryPasswordRepository;
    private SingleLiveEvent<RecoveryPasswordRepository.OnRecoveryPasswordSuccess> onRecoveryPasswordSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<ErrorResponse> onRecoveryPasswordFailData = new SingleLiveEvent<>();
    private SingleLiveEvent<ViewModelAction> action = new SingleLiveEvent<>();

    public enum ViewModelAction {
        RECOVERY_PRESSED
    }


    public void makeRecoveryPassword(String email) {
        RecoveryPasswordRequest recoveryPasswordRequest = new RecoveryPasswordRequest(email);
        getRecoveryPasswordRepository().recoveryPassword(recoveryPasswordRequest);
    }

    public SingleLiveEvent<RecoveryPasswordRepository.OnRecoveryPasswordSuccess> getOnRecoveryPasswordSuccessData() {
        return onRecoveryPasswordSuccessData;
    }

    public SingleLiveEvent<ErrorResponse> getOnRecoveryPasswordFailData() {
        return onRecoveryPasswordFailData;
    }

    private RecoveryPasswordRepository getRecoveryPasswordRepository() {
        if (recoveryPasswordRepository == null) {
            recoveryPasswordRepository = new RecoveryPasswordRepository();
            observerOnRecoveryPasswordSuccess();
            observerOnRecoveryPasswordFail();

        }
        return recoveryPasswordRepository;
    }

    private void observerOnRecoveryPasswordFail() {
        getDisposables().add(getRecoveryPasswordRepository().getOnRecoveryPasswordFail().subscribe(
                error -> onRecoveryPasswordFailData.setValue(error.getErrorResponse())));

    }

    private void observerOnRecoveryPasswordSuccess() {
        getDisposables().add(getRecoveryPasswordRepository().getOnRecoveryPasswordSuccess().subscribe(
                onRecoveryPasswordSuccess -> onRecoveryPasswordSuccessData.setValue(onRecoveryPasswordSuccess)));
    }

    public SingleLiveEvent<ViewModelAction> getAction() {
        return action;
    }

    public void onRecoveryPasswordPressed() {
        action.setValue(ViewModelAction.RECOVERY_PRESSED);
    }
}
