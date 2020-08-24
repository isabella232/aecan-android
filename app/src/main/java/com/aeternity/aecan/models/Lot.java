package com.aeternity.aecan.models;

import com.aeternity.aecan.BuildConfig;
import com.aeternity.aecan.util.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class Lot implements Serializable {
    private Integer id;
    private String identifier;
    private String varietyShortName;
    private ArrayList<Stage> stages;
    private Integer varietyId;
    private String startDate;
    private String endDate;
    private Integer plantsQuantity;
    private Integer irrigationTypeId;
    private String createdAt;
    private String updatedAt;
    private String irrigationType;
    private Variety variety = new Variety(1, "CW", false);
    private String weight = "00";
    private String status;
    private Stage currentStage;
    private ArrayList<Beacon> beacons;
    private String aeternityLink;

    private String pdfUrl;
    private boolean hasPdf;



    public Lot(Integer id, String startDate, String endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public String getIdAsString() {
        return String.valueOf(id);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVarietyShortName() {
        return varietyShortName;
    }

    public void setVarietyShortName(String varietyShortName) {
        this.varietyShortName = varietyShortName;
    }


    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public void setHasPdf(boolean hasPdf) {
        this.hasPdf = hasPdf;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }

    public Integer getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(Integer varietyId) {
        this.varietyId = varietyId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getParseStartDate() {
        return startDate != null ? DateUtils.stringToString(startDate, DateUtils.FULL_SERVER_DATE, DateUtils.STANDARD_DATE_FORMAT) : "";
    }

    public String getParseEndDate() {
        return endDate != null ? DateUtils.stringToString(endDate, DateUtils.FULL_SERVER_DATE, DateUtils.STANDARD_DATE_FORMAT) : "";
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPlantsQuantity() {
        return plantsQuantity;
    }

    public void setPlantsQuantity(Integer plantsQuantity) {
        this.plantsQuantity = plantsQuantity;
    }

    public Integer getIrrigationTypeId() {
        return irrigationTypeId;
    }

    public void setIrrigationTypeId(Integer irrigationTypeId) {
        this.irrigationTypeId = irrigationTypeId;
    }

    public String getIrrigationType() {
        return irrigationType;
    }

    public void setIrrigationType(String irrigationType) {
        this.irrigationType = irrigationType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Variety getVariety() {
        return variety;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean hasPdf() {
        return hasPdf;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAtPased() {
        return "Inicio: " + DateUtils.parseToString(createdAt);
    }

    public String getPlantsQuantityAsString() {
        return "Plantas/m2: " + plantsQuantity;
    }

    public String getStartDateParsed() {
        return "Inicio: " + DateUtils.parseToString(startDate);
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public String getIrrigationTypeAsString() {
        return "Riego: " + irrigationType;
    }

    public String getEndDateAsString() {
        return "Finalización: " + endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    public boolean isFinished() {
        return getStatus().equals("finished");
    }

    public String getAeternityLink() {
        return aeternityLink;
    }

    public void setAeternityLink(String aeternityLink) {
        this.aeternityLink = aeternityLink;
    }

    public boolean isAeternityLinkNotNullOrEmpty() {
        return (aeternityLink != null && !aeternityLink.isEmpty() && BuildConfig.app_mode.equals("final"));
    }

    public ArrayList<String> getInformationToString() {
        ArrayList<String> information = new ArrayList<>();
        if (getCreatedAt() != null && !getCreatedAt().isEmpty())
            information.add(getCreatedAtPased());
        if (getStartDate() != null && !getStartDate().isEmpty())
            information.add(getStartDateParsed());
        if (getEndDate() != null && !getEndDate().isEmpty())
            information.add(getEndDateAsString());
        if (getPlantsQuantity() != null) information.add(getPlantsQuantityAsString());
        if (getIrrigationType() != null && !getIrrigationType().isEmpty())
            information.add(getIrrigationTypeAsString());
        return information;
    }

    public boolean isHasPdf() {
        return hasPdf;
    }
}
