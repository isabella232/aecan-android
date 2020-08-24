package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.requests.CreateLotRequest;
import com.aeternity.aecan.network.responses.LotResponse;
import com.aeternity.aecan.network.responses.LotsResponse;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LotRepository {

    private PublishSubject<OnCreateLotSuccess> onCreateLotSuccess = PublishSubject.create();
    private PublishSubject<OnCreateLotFail> onCreateLotFail = PublishSubject.create();
    private PublishSubject<OnGetLotInformationSuccess> onGetLotInformationSuccess = PublishSubject.create();
    private PublishSubject<OnGetLotInformationFail> onGetLotInformationFail = PublishSubject.create();
    private PublishSubject<OnGetLotsSuccess> onGetLotsSuccess = PublishSubject.create();
    private PublishSubject<OnGetLotsFail> onGetLotsFail = PublishSubject.create();
    private PublishSubject<OnGetLotDetailSuccess> onGetLotDetailSuccess = PublishSubject.create();
    private PublishSubject<OnGetLotDetailFail> onGetLotDetailFail = PublishSubject.create();


    public CustomDisposableSingleObserver<LotResponse> createLot(CreateLotRequest createLotRequest) {
        return RestClient.getInstance().createLot(createLotRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(CreateLotObserver());
    }


    private CustomDisposableSingleObserver<LotResponse> CreateLotObserver() {
        return new CustomDisposableSingleObserver<LotResponse>() {
            @Override
            public void onError(ErrorResponse error) {
                onCreateLotFail.onNext(new OnCreateLotFail(error));
            }

            @Override
            public void onSuccess(LotResponse response) {
                onCreateLotSuccess.onNext(new OnCreateLotSuccess(response));
            }
        };
    }


    public CustomDisposableSingleObserver<LotResponse> getLotInformation() {
        return RestClient.getInstance().getLotInformation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLotInformationObserver());
    }

    private CustomDisposableSingleObserver<LotResponse> getLotInformationObserver() {
        return new CustomDisposableSingleObserver<LotResponse>() {
            @Override
            public void onError(ErrorResponse errorResponse) {
                onGetLotInformationFail.onNext(new OnGetLotInformationFail(errorResponse));
            }

            @Override
            public void onSuccess(LotResponse lotsResponse) {
                onGetLotInformationSuccess.onNext(new OnGetLotInformationSuccess(lotsResponse));
            }
        };
    }




    public CustomDisposableSingleObserver<LotsResponse> getInitializedLot() {
        return RestClient.getInstance().getInitializedLots()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLotsObserver());
    }

    public CustomDisposableSingleObserver<LotsResponse> getFinishedLots() {
        return RestClient.getInstance().getFinishedLots()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLotsObserver());
    }

    public CustomDisposableSingleObserver<LotResponse> getLotDetail(int id ) {
        return RestClient.getInstance().getLotDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLotDetailObserver());
    }


    public CustomDisposableSingleObserver<LotsResponse> getLotForUser() {
        //TODO cambiar url
        return RestClient.getInstance().getAssignedLots()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getLotsObserver());
    }


    private CustomDisposableSingleObserver<LotsResponse> getLotsObserver() {
        return new CustomDisposableSingleObserver<LotsResponse>() {
            @Override
            public void onError(ErrorResponse errorResponse) {
                onGetLotsFail.onNext(new OnGetLotsFail(errorResponse));
            }

            @Override
            public void onSuccess(LotsResponse lotsResponse) {
                onGetLotsSuccess.onNext(new OnGetLotsSuccess(lotsResponse));
            }
        };
    }


    private CustomDisposableSingleObserver<LotResponse> getLotDetailObserver() {
        return new CustomDisposableSingleObserver<LotResponse>() {
            @Override
            public void onError(ErrorResponse error) {
                onGetLotDetailFail.onNext(new OnGetLotDetailFail(error));
            }

            @Override
            public void onSuccess(LotResponse response) {
                onGetLotDetailSuccess.onNext(new OnGetLotDetailSuccess(response));
            }
        };
    }


    public PublishSubject<OnCreateLotSuccess> getOnCreateLotSuccess() {
        return onCreateLotSuccess;
    }

    public PublishSubject<OnCreateLotFail> getOnCreateLotFail() {
        return onCreateLotFail;
    }

    public PublishSubject<OnGetLotInformationSuccess> getOnGetLotInformationSuccess() {
        return onGetLotInformationSuccess;
    }

    public PublishSubject<OnGetLotDetailSuccess> getOnGetLotDetailSuccess() {
        return onGetLotDetailSuccess;
    }

    public PublishSubject<OnGetLotDetailFail> getOnGetLotDetailFail() {
        return onGetLotDetailFail;
    }

    public PublishSubject<OnGetLotInformationFail> getOnGetLotInformationFail() {
        return onGetLotInformationFail;
    }

    public PublishSubject<OnGetLotsSuccess> getOnGetLotsSuccess() {
        return onGetLotsSuccess;
    }

    public PublishSubject<OnGetLotsFail> getOnGetLotsFail() {
        return onGetLotsFail;
    }


    public class OnCreateLotSuccess {
        private LotResponse createLotResponse;

        public OnCreateLotSuccess(LotResponse createLotResponse) {
            this.createLotResponse = createLotResponse;
        }

        public LotResponse getCreateLotResponse() {
            return createLotResponse;
        }
    }

    public class OnGetLotInformationSuccess {
        private LotResponse getLotInformation;

        public OnGetLotInformationSuccess(LotResponse getLotInformation) {
            this.getLotInformation = getLotInformation;
        }

        public LotResponse getGetLotInformation() {
            return getLotInformation;
        }
    }

    public class OnGetLotsSuccess {
        private LotsResponse lostResponse;

        public OnGetLotsSuccess(LotsResponse lostResponse) {
            this.lostResponse = lostResponse;
        }

        public LotsResponse getLostResponse() {
            return lostResponse;
        }
    }

    public class OnGetLotDetailSuccess{
        private LotResponse lotDetailResponse;

        public OnGetLotDetailSuccess(LotResponse lotDetailResponse) {
            this.lotDetailResponse = lotDetailResponse;
        }

        public LotResponse getLotDetailResponse() {
            return lotDetailResponse;
        }
    }

    public class OnGetLotDetailFail extends ErrorWrapper {
        OnGetLotDetailFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }
    public class OnGetLotsFail extends ErrorWrapper {
        OnGetLotsFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnCreateLotFail extends ErrorWrapper {
        OnCreateLotFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnGetLotInformationFail extends ErrorWrapper {
        OnGetLotInformationFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

}
