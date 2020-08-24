package com.aeternity.aecan.viewModels;

import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.network.repositories.RegisterRepository;
import com.aeternity.aecan.network.requests.RegisterRequest;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.viewModels.base.BaseViewModel;


public class RegisterActivityViewModel extends BaseViewModel {
    private RegisterRepository registerRepository;
    private MutableLiveData<RegisterRepository.OnRegisterSuccess> onRegisterSuccessData = new MutableLiveData<>();
    private MutableLiveData<ErrorResponse> onRegisterFailData = new MutableLiveData<>();
    private MutableLiveData<ViewModelAction> action = new MutableLiveData<>();

    public enum ViewModelAction {
        REGISTER_PRESSED
    }

    public void makeRegister(RegisterRequest requestData) {
        getRegisterRepository().makeRegister(requestData);
    }


    public MutableLiveData<RegisterRepository.OnRegisterSuccess> getOnRegisterSuccessData() {
        return onRegisterSuccessData;
    }

    public MutableLiveData<ErrorResponse> getOnRegisterFailData() {
        return onRegisterFailData;
    }


    private void observeRegisterRepository() {
        getDisposables().add(getRegisterRepository().getOnRegisterSuccess().subscribe(
                onRegisterSuccess -> getOnRegisterSuccessData().postValue(onRegisterSuccess)));

        getDisposables().add(getRegisterRepository().getOnRegisterFail().subscribe(
                error -> onRegisterFailData.setValue(error.getErrorResponse())));

    }

    private RegisterRepository getRegisterRepository() {
        if (registerRepository == null) {
            registerRepository = new RegisterRepository();
            observeRegisterRepository();
        }
        return registerRepository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public MutableLiveData<ViewModelAction> getAction() {
        return action;
    }

    public void onRegisterButtonPressed() {
        action.setValue(ViewModelAction.REGISTER_PRESSED);
    }

}
