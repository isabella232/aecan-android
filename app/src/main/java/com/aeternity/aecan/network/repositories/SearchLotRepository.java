package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.LotResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchLotRepository {
    private PublishSubject<OnSearchLotSuccess> onSearchLotSuccess = PublishSubject.create();
    private PublishSubject<OnSearchLotFail> onSearchLotFail = PublishSubject.create();


    public CustomDisposableSingleObserver<LotResponse> searchLot(String searchRequest) {
       return RestClient.getInstance().searchLot(searchRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getSearchLotObserver());
    }

    public PublishSubject<OnSearchLotSuccess> getOnSearchLotSuccess() {
        return onSearchLotSuccess;
    }

    public PublishSubject<OnSearchLotFail> getOnSearchLotFail() {
        return onSearchLotFail;
    }

    private CustomDisposableSingleObserver<LotResponse> getSearchLotObserver() {
        return new CustomDisposableSingleObserver<LotResponse>() {
            @Override
            public void onError(ErrorResponse error) {
                onSearchLotFail.onNext(new OnSearchLotFail(error));
            }

            @Override
            public void onSuccess(LotResponse response) {
                onSearchLotSuccess.onNext(new OnSearchLotSuccess(response));
            }
        };
    }


    public class OnSearchLotFail extends ErrorWrapper{
        public OnSearchLotFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnSearchLotSuccess {
        private LotResponse lotResponse;

        public OnSearchLotSuccess(LotResponse lotResponse) {
            this.lotResponse = lotResponse;
        }

        public LotResponse getLotResponse() {
            return lotResponse;
        }
    }
}
