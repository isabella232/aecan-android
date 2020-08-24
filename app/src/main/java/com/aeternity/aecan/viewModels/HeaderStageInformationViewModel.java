package com.aeternity.aecan.viewModels;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.BuildConfig;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import java.util.ArrayList;

import static com.aeternity.aecan.views.fragments.HeaderStageInformationFragment.*;

public class HeaderStageInformationViewModel extends BaseViewModel {

    private SingleLiveEvent<Actions> fragmentAction = new SingleLiveEvent<>();
    private Boolean isFinished;
    private String endDate;
    private Lot lot;
    private Boolean admitsBeacons;

    public SingleLiveEvent<Actions> getFragmentAction() {
        return fragmentAction;
    }


    public void onFinishStagePressed() {
        getFragmentAction().setValue(Actions.FINISH_STAGE);
    }


    public void setArguments(Bundle params) {
        this.lot = (Lot) params.getSerializable(Constants.LOT_KEY);
        this.isFinished = params.getBoolean(Constants.IS_FINISHED_KEY);
        this.endDate = params.getString(Constants.END_DATE_KEY);
        this.admitsBeacons = params.getBoolean(Constants.ADMITS_BEACONS_KEY);

    }

    public Boolean getFinished() {
        return isFinished;
    }

    public boolean showFinishStageButton() {
        return BuildConfig.app_mode.equals("operator") && !isFinished;
    }

    public String getEndDate() {
        return endDate;
    }

    public Lot getLot() {
        return lot;
    }


    public void selectBeacons() {
        getFragmentAction().setValue(Actions.SELECT_BEACONS);
    }

    public Boolean getAdmitsBeacons() {
        return admitsBeacons;
    }

    public Boolean buttonBeaconsVisible() {
        return admitsBeacons && !isFinished;
    }
}
