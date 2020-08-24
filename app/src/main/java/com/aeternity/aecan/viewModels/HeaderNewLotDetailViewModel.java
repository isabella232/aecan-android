package com.aeternity.aecan.viewModels;

import androidx.lifecycle.ViewModel;

import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.util.SingleLiveEvent;

public class HeaderNewLotDetailViewModel extends ViewModel {
    private SingleLiveEvent<Lot> lot = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> beaconsButton = new SingleLiveEvent<>();

    public SingleLiveEvent<Lot> getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot.setValue(lot);
    }

    public SingleLiveEvent<Boolean> getBeaconsButton() {
        return beaconsButton;
    }

    public void onBeaconsPressed(){
        beaconsButton.setValue(true);
    }
}
