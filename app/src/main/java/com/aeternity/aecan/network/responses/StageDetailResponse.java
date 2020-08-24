package com.aeternity.aecan.network.responses;

import com.aeternity.aecan.models.Beacon;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.Tab;

import java.util.ArrayList;

public class StageDetailResponse {
    private String lotNumber;
    private ArrayList<Tab> tabs = new ArrayList<>();
    private String stageName="";
    private String endDate="";

    private Boolean admitsBeacons;
    private ArrayList<Beacon> beaconsOptions;
    private ArrayList<Beacon> beacons;

    private boolean finished;

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public ArrayList<Tab> getTabs() {
        return tabs;
    }


    public void setTabs(ArrayList<Tab> tags) {
        this.tabs = tags;
    }

    public String getStageName() {
        return stageName;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public Boolean getAdmitsBeacons() {
        return admitsBeacons;
    }

    public ArrayList<Beacon> getBeaconsOptions() {
        return beaconsOptions;
    }

    public ArrayList<Beacon> getBeacons() {
        return beacons;
    }
}
