package com.aeternity.aecan.network.requests;

import java.util.ArrayList;

public class AssignBeaconsIdsRequest {

    ArrayList<Integer> beaconIds;

    public AssignBeaconsIdsRequest(ArrayList<Integer> beaconIds) {
        this.beaconIds = beaconIds;
    }
}
