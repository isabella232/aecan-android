package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.requests.AssignBeaconsIdsRequest;
import com.aeternity.aecan.network.responses.base.BaseResponse;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class AssignBeaconsRepository {

    private PublishSubject<OnAssignBeaconsSuccess> onAssignBeaconsSuccess = PublishSubject.create();
    private PublishSubject<OnAssignBeaconsFail> onAssignBeaconsFail = PublishSubject.create();

    public CustomDisposableSingleObserver<BaseResponse> assignBeacons(String id, ArrayList<Integer> beaconsIds) {
        return RestClient.getInstance().assignBeacons(id, new AssignBeaconsIdsRequest(beaconsIds))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getBeaconsObserver());
    }

    private CustomDisposableSingleObserver<BaseResponse> getBeaconsObserver() {
        return new CustomDisposableSingleObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse beaconsResponse) {
                onAssignBeaconsSuccess.onNext(new OnAssignBeaconsSuccess(beaconsResponse));
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                onAssignBeaconsFail.onNext(new OnAssignBeaconsFail(errorResponse));
            }
        };
    }


    public class OnAssignBeaconsSuccess {
        private BaseResponse response;

        OnAssignBeaconsSuccess(BaseResponse beaconsResponse) {
            this.response = beaconsResponse;
        }

        public BaseResponse getBeaconsResponse() {
            return response;
        }
    }

    public class OnAssignBeaconsFail extends ErrorWrapper {
        OnAssignBeaconsFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public PublishSubject<OnAssignBeaconsSuccess> getOnAssignBeaconsSuccess() {
        return onAssignBeaconsSuccess;
    }

    public PublishSubject<OnAssignBeaconsFail> getOnAssignBeaconsFail() {
        return onAssignBeaconsFail;
    }
}
