package com.aeternity.aecan.viewModels;

import com.aeternity.aecan.network.repositories.LoginRepository;
import com.aeternity.aecan.network.requests.LoginRequest;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.persistence.SessionPersistence;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import static com.aeternity.aecan.util.Constants.GrantType.PASSWORD;


public class LoginActivityViewModel extends BaseViewModel {
    private LoginRepository loginRepository;
    private SingleLiveEvent<LoginRepository.OnLoginRepositorySuccess> onLoginRepositorySuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<ErrorResponse> onLoginRepositoryFailData = new SingleLiveEvent<>();

    private SingleLiveEvent<ViewModelAction> buttonAction = new SingleLiveEvent<>();

    public enum ViewModelAction {
        LOGIN_PRESSED,
        SIGN_UP_PRESSED,
        FORGOT_PASSWORD_PRESSED,
    }

    public void makeLogin(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        loginRequest.setGrant_type(PASSWORD.getText());
        getLoginRepository().makeLogin(loginRequest);
    }


    public SingleLiveEvent<LoginRepository.OnLoginRepositorySuccess> getOnLoginSuccessData() {
        return onLoginRepositorySuccessData;
    }

    public SingleLiveEvent<ErrorResponse> getOnLoginFailData() {
        return onLoginRepositoryFailData;
    }


    private void observeLoginRepositorySuccess() {
        getDisposables().add(getLoginRepository().getOnLoginSuccess().subscribe(onLoginRepositorySuccess ->
                getOnLoginSuccessData().postValue(onLoginRepositorySuccess)));

    }

    private void observeLoginRepositoryFail() {
        getDisposables().add(getLoginRepository().getOnLoginFail().subscribe(error ->
                getOnLoginFailData().postValue(error.getErrorResponse())));
    }


    private LoginRepository getLoginRepository() {
        if (loginRepository == null) {
            loginRepository = new LoginRepository();
            observeLoginRepositorySuccess();
            observeLoginRepositoryFail();
        }
        return loginRepository;
    }

    public boolean isUserSignedIn() {
        return SessionPersistence.newOrReadFromPaper().checkIfIsSignedIn();
    }


    public void onLoginButtonPressed() {
        buttonAction.postValue(ViewModelAction.LOGIN_PRESSED);
    }

    public void onForgotPasswordButtonPressed() {
        buttonAction.postValue(ViewModelAction.FORGOT_PASSWORD_PRESSED);
    }

    public void onSignUpButtonPressed() {
        buttonAction.postValue(ViewModelAction.SIGN_UP_PRESSED);
    }

    public SingleLiveEvent<ViewModelAction> getButtonAction() {
        return buttonAction;
    }
}
