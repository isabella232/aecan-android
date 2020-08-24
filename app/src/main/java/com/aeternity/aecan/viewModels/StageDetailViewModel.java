package com.aeternity.aecan.viewModels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.models.Stage;
import com.aeternity.aecan.network.repositories.AssignBeaconsRepository;
import com.aeternity.aecan.network.repositories.StageRepository;
import com.aeternity.aecan.network.responses.StageDetailResponse;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import java.util.ArrayList;

public class StageDetailViewModel extends BaseViewModel {
    private StageRepository stageRepository;
    private AssignBeaconsRepository assignBeaconsRepository;
    private MutableLiveData<StageRepository.OnFinishedStageSuccess> onFinishedStageSuccessData = new MutableLiveData<>();
    private MutableLiveData<StageRepository.OnFinishedStageFail> onFinishedStageFailData = new MutableLiveData<>();
    private MutableLiveData<StageRepository.OnAskedFinishedStageSuccess> onAskedFinishedStageSuccessData = new MutableLiveData<>();
    private MutableLiveData<StageRepository.OnAskedFinishedStageFail> onAskedFinishedStageFailData = new MutableLiveData<>();
    private MutableLiveData<StageRepository.OnGetStageDetailSuccess> onGetStageDetailSuccessData = new MutableLiveData<>();
    private MutableLiveData<StageRepository.OnGetStageDetailFail> onGetStageDetailFailData = new MutableLiveData<>();

    private MutableLiveData<AssignBeaconsRepository.OnAssignBeaconsSuccess> onAssignBeaconsSuccess = new MutableLiveData<>();
    private MutableLiveData<AssignBeaconsRepository.OnAssignBeaconsFail> onAssignBeaconsFail = new MutableLiveData<>();

    private ObservableField<StageDetailResponse> stageDetailresponse = new ObservableField<>();
    private Stage stage;
    private Lot lot;

    public Stage getStage() {
        return stage;
    }

    public Lot getLot() {
        return lot;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public StageRepository getStageRepository() {
        if (stageRepository == null) {
            stageRepository = new StageRepository();
            observerGetStageDetailSuccess();
            observerGetStageDetailFail();
            setupSuccessFinishedStageObserver();
            setupFailFinishedStageObserver();
            setupAskedSuccessFinishedStageObserver();
            setupAskedFailFinishedStageObserver();
        }
        return stageRepository;
    }

    public AssignBeaconsRepository getAssignBeaconsRepository() {
        if (assignBeaconsRepository == null) {
            assignBeaconsRepository = new AssignBeaconsRepository();
            ObserveAssignRepository();
        }
        return assignBeaconsRepository;
    }

    private void ObserveAssignRepository() {
        getDisposables().add(getAssignBeaconsRepository().getOnAssignBeaconsFail().subscribe(response -> {
            onAssignBeaconsFail.postValue(response);
        }));

        getDisposables().add(getAssignBeaconsRepository().getOnAssignBeaconsSuccess().subscribe(response -> {
            onAssignBeaconsSuccess.postValue(response);
        }));
    }


    public MutableLiveData<StageRepository.OnFinishedStageSuccess> getOnFinishedStageSuccessData() {
        return onFinishedStageSuccessData;
    }

    public MutableLiveData<StageRepository.OnFinishedStageFail> getOnFinishedStageFailData() {
        return onFinishedStageFailData;
    }

    public MutableLiveData<StageRepository.OnAskedFinishedStageSuccess> getOnAskedFinishedStageSuccessData() {
        return onAskedFinishedStageSuccessData;
    }

    public MutableLiveData<StageRepository.OnAskedFinishedStageFail> getOnAskedFinishedStageFailData() {
        return onAskedFinishedStageFailData;
    }

    private void setupAskedFailFinishedStageObserver() {
        getDisposables().add(getStageRepository().getOnAskedFinishedStageFail().subscribe(onAskedFinishedStageFail -> {
            getOnAskedFinishedStageFailData().postValue(onAskedFinishedStageFail);
        }));
    }

    private void setupAskedSuccessFinishedStageObserver() {
        getDisposables().add(getStageRepository().getOnAskedFinishedStageSuccess().subscribe(onAskedFinishedStageSuccess -> {
            getOnAskedFinishedStageSuccessData().postValue(onAskedFinishedStageSuccess);
        }));
    }

    private void setupFailFinishedStageObserver() {
        getDisposables().add(getStageRepository().getOnFinishedStageSuccess().subscribe(onFinishedStageSuccess -> {
            getOnFinishedStageSuccessData().postValue(onFinishedStageSuccess);
        }));
    }

    private void setupSuccessFinishedStageObserver() {
        getDisposables().add(getStageRepository().getOnFinishedStageFail().subscribe(onFinishedStageFail -> {
            getOnFinishedStageFailData().postValue(onFinishedStageFail);
        }));
    }

    private void observerGetStageDetailFail() {
        getDisposables().add(getStageRepository().getOnGetStageDetailFail().subscribe(
                onGetStageDetailFail -> getOnGetStageDetailFailData().setValue(onGetStageDetailFail)));
    }

    public MutableLiveData<StageRepository.OnGetStageDetailSuccess> getOnGetStageDetailSuccessData() {
        return onGetStageDetailSuccessData;
    }

    public MutableLiveData<StageRepository.OnGetStageDetailFail> getOnGetStageDetailFailData() {
        return onGetStageDetailFailData;
    }

    private void observerGetStageDetailSuccess() {
        getDisposables().add(getStageRepository().getOnGetStageDetailSuccess().subscribe(
                onGetStageDetailSuccess -> {
                    getStageDetailresponse().set(onGetStageDetailSuccess.getResponse());
                    getOnGetStageDetailSuccessData().setValue(onGetStageDetailSuccess);
                }));
    }

    public void getStageDetailFromServer() {
        getStageRepository().getStageDetailFromServer(stage.getId());
    }

    public void finishStage() {
        getStageRepository().finishStage(stage.getId());
    }

    public void checkFinishStage() {
        getStageRepository().askedFinishStage(stage.getId());
    }

    public ObservableField<StageDetailResponse> getStageDetailresponse() {
        return stageDetailresponse;
    }

    public void updateBeacons(ArrayList<Integer> beaconIds) {

        getAssignBeaconsRepository().assignBeacons(stage.getId(), beaconIds);

    }

    public MutableLiveData<AssignBeaconsRepository.OnAssignBeaconsSuccess> getOnAssignBeaconsSuccess() {
        return onAssignBeaconsSuccess;
    }

    public MutableLiveData<AssignBeaconsRepository.OnAssignBeaconsFail> getOnAssignBeaconsFail() {
        return onAssignBeaconsFail;
    }




}
