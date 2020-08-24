package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.customComponents.CustomDisposableSingleObserver;
import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.AskFinishedStageResponse;
import com.aeternity.aecan.network.responses.StageDetailResponse;
import com.aeternity.aecan.network.responses.base.BaseResponse;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class StageRepository {
    private PublishSubject<OnGetStageDetailSuccess> onGetStageDetailSuccess = PublishSubject.create();
    private PublishSubject<OnGetStageDetailFail> onGetStageDetailFail = PublishSubject.create();
    private PublishSubject<OnFinishedStageSuccess> onFinishedStageSuccess = PublishSubject.create();
    private PublishSubject<OnFinishedStageFail> onFinishedStageFail = PublishSubject.create();
    private PublishSubject<OnAskedFinishedStageSuccess> onAskedFinishedStageSuccess = PublishSubject.create();
    private PublishSubject<OnAskedFinishedStageFail> onAskedFinishedStageFail = PublishSubject.create();

    public PublishSubject<OnGetStageDetailSuccess> getOnGetStageDetailSuccess() {
        return onGetStageDetailSuccess;
    }

    public PublishSubject<OnGetStageDetailFail> getOnGetStageDetailFail() {
        return onGetStageDetailFail;
    }

    public PublishSubject<OnFinishedStageSuccess> getOnFinishedStageSuccess() {
        return onFinishedStageSuccess;
    }

    public PublishSubject<OnFinishedStageFail> getOnFinishedStageFail() {
        return onFinishedStageFail;
    }

    public PublishSubject<OnAskedFinishedStageSuccess> getOnAskedFinishedStageSuccess() {
        return onAskedFinishedStageSuccess;
    }

    public PublishSubject<OnAskedFinishedStageFail> getOnAskedFinishedStageFail() {
        return onAskedFinishedStageFail;
    }

    public CustomDisposableSingleObserver<StageDetailResponse> getStageDetailFromServer(String id) {
        return RestClient.getInstance().getStageDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getStageDetailObserver());
    }

    private CustomDisposableSingleObserver<StageDetailResponse> getStageDetailObserver() {
        return new CustomDisposableSingleObserver<StageDetailResponse>() {
            @Override
            public void onError(ErrorResponse error) {
                onGetStageDetailFail.onNext(new OnGetStageDetailFail(error));
            }

            @Override
            public void onSuccess(StageDetailResponse response) {
                onGetStageDetailSuccess.onNext(new OnGetStageDetailSuccess(response));
            }
        };
    }

    public CustomDisposableSingleObserver<AskFinishedStageResponse> askedFinishStage(String id) {
        return RestClient.getInstance().checkFinishStage(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(askedFinishStageObserver());
    }

    private CustomDisposableSingleObserver<AskFinishedStageResponse> askedFinishStageObserver() {
        return new CustomDisposableSingleObserver<AskFinishedStageResponse>() {
            @Override
            public void onError(ErrorResponse error) {
                onAskedFinishedStageFail.onNext(new OnAskedFinishedStageFail(error));
            }

            @Override
            public void onSuccess(AskFinishedStageResponse response) {
                onAskedFinishedStageSuccess.onNext(new OnAskedFinishedStageSuccess(response));
            }
        };
    }

    public CustomDisposableSingleObserver<BaseResponse> finishStage(String id) {
        return RestClient.getInstance().finishStage(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(finishStageObserver());
    }

    private CustomDisposableSingleObserver<BaseResponse> finishStageObserver() {
        return new CustomDisposableSingleObserver<BaseResponse>() {
            @Override
            public void onError(ErrorResponse error) {
                onFinishedStageFail.onNext(new OnFinishedStageFail(error));
            }

            @Override
            public void onSuccess(BaseResponse response) {
                onFinishedStageSuccess.onNext(new OnFinishedStageSuccess(response));
            }
        };
    }

    public class OnGetStageDetailSuccess {
        private StageDetailResponse response;

        public OnGetStageDetailSuccess(StageDetailResponse response) {
            this.response = response;
        }

        public StageDetailResponse getResponse() {
            return response;
        }
    }

    public class OnGetStageDetailFail extends ErrorWrapper {

        public OnGetStageDetailFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }


    public class OnFinishedStageSuccess {
        private BaseResponse baseResponse;

        public OnFinishedStageSuccess(BaseResponse baseResponse) {
            this.baseResponse = baseResponse;
        }

        public BaseResponse getBaseResponse() {
            return baseResponse;
        }
    }

    public class OnFinishedStageFail extends ErrorWrapper {

        public OnFinishedStageFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class OnAskedFinishedStageSuccess {
       private AskFinishedStageResponse askFinishedStageResponse;

        public OnAskedFinishedStageSuccess(AskFinishedStageResponse askFinishedStageResponse) {
            this.askFinishedStageResponse = askFinishedStageResponse;
        }

        public AskFinishedStageResponse getAskFinishedStageResponse() {
            return askFinishedStageResponse;
        }
    }

    public class OnAskedFinishedStageFail extends ErrorWrapper {

        public OnAskedFinishedStageFail(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }
}
