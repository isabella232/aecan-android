package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.requests.RecoveryPasswordRequest;
import com.aeternity.aecan.network.responses.base.BaseResponse;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RecoveryPasswordRepository {
    private PublishSubject<OnRecoveryPasswordSuccess> onRecoveryPasswordSuccess = PublishSubject.create();
    private PublishSubject<OnRecoveryPasswordFail> onRecoveryPasswordFail = PublishSubject.create();

    public CustomDisposableSingleObserver<BaseResponse> recoveryPassword(RecoveryPasswordRequest recoveryPasswordRequest) {
        return RestClient.getInstance().recoveryPassword(recoveryPasswordRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRecoveryPasswordObserver());
    }

    private CustomDisposableSingleObserver<BaseResponse> getRecoveryPasswordObserver() {
        return new CustomDisposableSingleObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse RegisterResponse) {
                onRecoveryPasswordSuccess.onNext(new OnRecoveryPasswordSuccess(RegisterResponse));
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                onRecoveryPasswordFail.onNext(new OnRecoveryPasswordFail(errorResponse));
            }
        };
    }

    public class OnRecoveryPasswordFail extends ErrorWrapper {
        public OnRecoveryPasswordFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnRecoveryPasswordSuccess {
        private BaseResponse recoveryPasswordResponse;

        public OnRecoveryPasswordSuccess(BaseResponse recoveryPasswordResponse) {
            this.recoveryPasswordResponse = recoveryPasswordResponse;
        }

        public BaseResponse getBaseResponse() {
            return recoveryPasswordResponse;
        }
    }

    public PublishSubject<OnRecoveryPasswordSuccess> getOnRecoveryPasswordSuccess() {
        return onRecoveryPasswordSuccess;
    }

    public PublishSubject<OnRecoveryPasswordFail> getOnRecoveryPasswordFail() {
        return onRecoveryPasswordFail;
    }
}
