package com.aeternity.aecan.viewModels;

import androidx.databinding.ObservableField;

import com.aeternity.aecan.network.repositories.LotRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

public class FinishedLotsViewModel extends BaseViewModel {
    private LotRepository lotRepository;
    private ObservableField<Boolean> emptyLots = new ObservableField<>();

    private SingleLiveEvent<LotRepository.OnGetLotsSuccess> onGetLotsSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<ErrorResponse> onGetLotsFailData = new SingleLiveEvent<>();

    private LotRepository getLotRepository() {
        if (lotRepository == null) {
            lotRepository = new LotRepository();
            observerGetFinishedLotsRepositorySuccess();
            observerGetFinishedLotsRepositoryFail();
        }
        return lotRepository;
    }

    public ObservableField<Boolean> getEmptyLots() {
        return emptyLots;
    }

    private void observerGetFinishedLotsRepositoryFail() {
        getDisposables().add(getLotRepository().getOnCreateLotFail().subscribe(error -> {
            emptyLots.set(false);
            getOnGetLotsFailData().postValue(error.getErrorResponse());
        }));

    }

    private void observerGetFinishedLotsRepositorySuccess() {
        getDisposables().add(getLotRepository().getOnGetLotsSuccess().subscribe(onGetLotsSuccess -> {
            getOnGetLotsSuccessData().postValue(onGetLotsSuccess);
            emptyLots.set(onGetLotsSuccess.getLostResponse().getLots().isEmpty());
        }));
    }

    public SingleLiveEvent<LotRepository.OnGetLotsSuccess> getOnGetLotsSuccessData() {
        return onGetLotsSuccessData;
    }

    public SingleLiveEvent<ErrorResponse> getOnGetLotsFailData() {
        return onGetLotsFailData;
    }

    public void getFinishedLots() {
        getLotRepository().getFinishedLots();
    }

    public void getLotsForUser() {
        getLotRepository().getLotForUser();
    }

}
