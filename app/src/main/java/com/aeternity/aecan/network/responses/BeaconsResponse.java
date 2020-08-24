package com.aeternity.aecan.network.responses;

import com.aeternity.aecan.models.Beacon;
import com.aeternity.aecan.network.responses.base.BaseResponse;

import java.util.ArrayList;

public class BeaconsResponse extends BaseResponse {
    private ArrayList<Beacon> beacons = new ArrayList<>();

    public ArrayList<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }
}
