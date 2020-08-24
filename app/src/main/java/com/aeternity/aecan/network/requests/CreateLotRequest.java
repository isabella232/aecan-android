package com.aeternity.aecan.network.requests;

import java.util.ArrayList;

public class CreateLotRequest {
    private String identifier;
    private String startDate;
    private Integer plantsQuantity;
    private ArrayList<Integer> beaconIds;
    private Integer varietyId;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getPlantsQuantity() {
        return plantsQuantity;
    }

    public void setPlantsQuantity(Integer plantsQuantity) {
        this.plantsQuantity = plantsQuantity;
    }

    public ArrayList<Integer> getBeaconIds() {
        return beaconIds;
    }

    public void setBeaconIds(ArrayList<Integer> beaconIds) {
        this.beaconIds = beaconIds;
    }

    public Integer getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(Integer varietyId) {
        this.varietyId = varietyId;
    }
}
