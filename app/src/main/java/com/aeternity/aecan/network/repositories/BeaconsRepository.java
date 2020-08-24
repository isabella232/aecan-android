package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.BeaconsResponse;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class BeaconsRepository {
    private PublishSubject<OnGetBeaconsSuccess> onGetBeaconsSuccess = PublishSubject.create();
    private PublishSubject<OnGetBeaconsFail> onGetBeaconsFail = PublishSubject.create();

    public CustomDisposableSingleObserver<BeaconsResponse> getBeacons() {
        return RestClient.getInstance().getBeacons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getBeaconsObserver());
    }



    private CustomDisposableSingleObserver<BeaconsResponse> getBeaconsObserver() {
        return new CustomDisposableSingleObserver<BeaconsResponse>() {
            @Override
            public void onSuccess(BeaconsResponse beaconsResponse) {
                onGetBeaconsSuccess.onNext(new OnGetBeaconsSuccess(beaconsResponse));
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                onGetBeaconsFail.onNext(new OnGetBeaconsFail(errorResponse));
            }
        };
    }

    public class OnGetBeaconsFail extends ErrorWrapper {
        OnGetBeaconsFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnGetBeaconsSuccess {
        private BeaconsResponse beaconsResponse;

        OnGetBeaconsSuccess(BeaconsResponse beaconsResponse) {
            this.beaconsResponse = beaconsResponse;
        }

        public BeaconsResponse getBeaconsResponse() {
            return beaconsResponse;
        }
    }

    public PublishSubject<OnGetBeaconsSuccess> getOnGetBeaconsSuccess() {
        return onGetBeaconsSuccess;
    }

    public PublishSubject<OnGetBeaconsFail> getOnGetBeaconsFail() {
        return onGetBeaconsFail;
    }


}
