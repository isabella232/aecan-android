package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.requests.RegisterRequest;
import com.aeternity.aecan.network.responses.RegisterResponse;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RegisterRepository {
    private PublishSubject<OnRegisterSuccess> onRegisterSuccess = PublishSubject.create();
    private PublishSubject<OnRegisterFail> onRegisterFail = PublishSubject.create();

    public CustomDisposableSingleObserver<RegisterResponse> makeRegister(RegisterRequest registerRequest) {
        return RestClient.getInstance().makeRegister(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRegisterObserver());
    }

    private CustomDisposableSingleObserver<RegisterResponse> getRegisterObserver() {
        return new CustomDisposableSingleObserver<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse RegisterResponse) {
                onRegisterSuccess.onNext(new OnRegisterSuccess(RegisterResponse));
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                onRegisterFail.onNext(new OnRegisterFail(errorResponse));
            }
        };
    }

    public PublishSubject<OnRegisterSuccess> getOnRegisterSuccess() {
        return onRegisterSuccess;
    }

    public PublishSubject<OnRegisterFail> getOnRegisterFail() {
        return onRegisterFail;
    }

    public class OnRegisterSuccess {
        private RegisterResponse registerResponse;

        public OnRegisterSuccess(RegisterResponse registerResponse) {
            this.registerResponse = registerResponse;
        }

        public RegisterResponse getRegisterResponse() {
            return registerResponse;
        }
    }

    public class OnRegisterFail extends ErrorWrapper {
        public OnRegisterFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }
}

