package com.aeternity.aecan.viewModels;


import androidx.databinding.ObservableField;

import com.aeternity.aecan.network.repositories.LotRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

public class InitializedLotsViewModel extends BaseViewModel {

    private LotRepository lotRepository;
    private SingleLiveEvent<LotRepository.OnGetLotsSuccess> onGetLotsSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<ErrorResponse> onGetLotsFailData = new SingleLiveEvent<>();
    private ObservableField<Boolean> emptyLots = new ObservableField<>();

    public SingleLiveEvent<LotRepository.OnGetLotsSuccess> getOnGetLotsSuccessData() {
        return onGetLotsSuccessData;
    }

    public SingleLiveEvent<ErrorResponse> getOnGetLotsFailData() {
        return onGetLotsFailData;
    }

    private LotRepository getLotRepository() {
        if (lotRepository == null) {
            lotRepository = new LotRepository();
            observeOnCreateLotSuccess();
            observeOnCreateLotFail();
        }
        return lotRepository;
    }

    public ObservableField<Boolean> getEmptyLots() {
        return emptyLots;
    }

    private void observeOnCreateLotFail() {
        getDisposables().add(getLotRepository().getOnCreateLotFail().subscribe(error -> {
            emptyLots.set(false);
            onGetLotsFailData.postValue(error.getErrorResponse());
        }));

    }

    private void observeOnCreateLotSuccess() {
        getDisposables().add(getLotRepository().getOnGetLotsSuccess().subscribe(onGetLotsSuccess -> {
            onGetLotsSuccessData.postValue(onGetLotsSuccess);
            emptyLots.set(onGetLotsSuccess.getLostResponse().getLots().isEmpty());
        }));
    }

    public void getInitializedLots() {
        getLotRepository().getInitializedLot();
    }

}