package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.VarietiesResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class VarietiesRepository {
    private PublishSubject<OnGetVarietiesSuccess> onGetVarietiesSuccess = PublishSubject.create();
    private PublishSubject<OnVarietiesFail> onGetVarietiesFail = PublishSubject.create();

    public CustomDisposableSingleObserver<VarietiesResponse> getVarieties() {
        return RestClient.getInstance().getVarieties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getVarietiesObserver());
    }

    private CustomDisposableSingleObserver<VarietiesResponse> getVarietiesObserver() {

        return new CustomDisposableSingleObserver<VarietiesResponse>() {
            @Override
            public void onSuccess(VarietiesResponse RegisterResponse) {
                onGetVarietiesSuccess.onNext(new OnGetVarietiesSuccess(RegisterResponse));
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                onGetVarietiesFail.onNext(new OnVarietiesFail(errorResponse));

            }

        };
    }

    public PublishSubject<OnGetVarietiesSuccess> getOnGetVarietiesSuccess() {
        return onGetVarietiesSuccess;
    }

    public PublishSubject<OnVarietiesFail> getOnGetVarietiesFail() {
        return onGetVarietiesFail;
    }

    public class OnVarietiesFail extends ErrorWrapper {
        public OnVarietiesFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnGetVarietiesSuccess {
        private VarietiesResponse varietiesResponse;

        public OnGetVarietiesSuccess(VarietiesResponse varietiesResponse) {
            this.varietiesResponse = varietiesResponse;
        }

        public VarietiesResponse getVarietiesResponse() {
            return varietiesResponse;
        }
    }

}

