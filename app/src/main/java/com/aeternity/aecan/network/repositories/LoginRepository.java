package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.requests.LoginRequest;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.LoginResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;
import com.aeternity.aecan.persistence.SessionPersistence;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LoginRepository {
    private PublishSubject<OnLoginRepositorySuccess> onLoginSuccess = PublishSubject.create();
    private PublishSubject<OnLoginFail> onLoginFail = PublishSubject.create();

    public CustomDisposableSingleObserver<LoginResponse> makeLogin(LoginRequest loginRequest) {
        return RestClient.getInstance().makeLogin(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLoginObserverForDriver());
    }

    private CustomDisposableSingleObserver<LoginResponse> getLoginObserverForDriver() {
        return new CustomDisposableSingleObserver<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {

                SessionPersistence.saveSessionInPersistence(loginResponse);
                onLoginSuccess.onNext(new OnLoginRepositorySuccess(loginResponse));
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                onLoginFail.onNext(new OnLoginFail(errorResponse));
            }

        };
    }

    public PublishSubject<OnLoginRepositorySuccess> getOnLoginSuccess() {
        return onLoginSuccess;
    }

    public PublishSubject<OnLoginFail> getOnLoginFail() {
        return onLoginFail;
    }


    public class OnLoginFail extends ErrorWrapper {
        public OnLoginFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnLoginRepositorySuccess {
        private LoginResponse loginResponse;

        public OnLoginRepositorySuccess(LoginResponse loginResponse) {
            this.loginResponse = loginResponse;
        }

        public LoginResponse getLoginResponse() {
            return loginResponse;
        }
    }

}

