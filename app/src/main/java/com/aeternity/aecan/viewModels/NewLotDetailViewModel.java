package com.aeternity.aecan.viewModels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.network.repositories.LotRepository;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

public class NewLotDetailViewModel extends BaseViewModel {
    private MutableLiveData<Lot> lot = new MutableLiveData<>();
    private ObservableField<Boolean> hasPdf = new ObservableField<>();
    private MutableLiveData<Boolean> buttonLaboratoryAnalysis = new MutableLiveData<>();
    private SingleLiveEvent<LotRepository.OnGetLotDetailSuccess> lotDetailSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<LotRepository.OnGetLotDetailFail> lotDetailFailData = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> buttonAeternityLink = new MutableLiveData<>();
    private LotRepository lotRepository;

    public MutableLiveData<Boolean> getButtonLaboratoryAnalysis() {
        return buttonLaboratoryAnalysis;
    }

    public MutableLiveData<Boolean> getButtonAeternityLink() {
        return buttonAeternityLink;
    }

    public void onButtonAeternityLinkPressed(){
        getButtonAeternityLink().setValue(true);
    }
    public MutableLiveData<LotRepository.OnGetLotDetailSuccess> getLotDetailSuccessData() {
        return lotDetailSuccessData;
    }

    public MutableLiveData<LotRepository.OnGetLotDetailFail> getLotDetailFailData() {
        return lotDetailFailData;
    }

    public ObservableField<Boolean> getHasPdf() {
        return hasPdf;
    }

    public void getLotDetail(int id) {
        getLotRepository().getLotDetail(id);
    }

    public LotRepository getLotRepository() {
        if (lotRepository == null) {
            lotRepository = new LotRepository();
            observerGetLotDetailSuccess();
            observerGetLotDetailFail();
        }
        return lotRepository;
    }

    private void observerGetLotDetailFail() {
        getDisposables().add(getLotRepository().getOnGetLotDetailFail().subscribe(error ->
                getLotDetailFailData().postValue(error)));
    }

    private void observerGetLotDetailSuccess() {
        getDisposables().add(getLotRepository().getOnGetLotDetailSuccess().subscribe(onGetLotDetailSuccess -> {
            getHasPdf().set(onGetLotDetailSuccess.getLotDetailResponse().getLot().hasPdf());
            setLot(onGetLotDetailSuccess.getLotDetailResponse().getLot());
            getLotDetailSuccessData().postValue(onGetLotDetailSuccess);
        }));
    }

    public void onButtonLaboratoryPressed() {
        getButtonLaboratoryAnalysis().setValue(true);
    }

    public MutableLiveData<Lot> getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        getLot().setValue(lot);
    }
}
