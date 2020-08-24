package com.aeternity.aecan.models;

import com.aeternity.aecan.R;
import com.aeternity.aecan.util.DateUtils;

import java.io.Serializable;

public class Stage implements Serializable {
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private String state = STATE_PENDING;

    public final static String STATE_FINISHED = "finished";
    public final static String STATE_STARTED = "started";
    public final static String STATE_PENDING = "not_started";



    private Positions position;

    public boolean isActive() {
        return state.equals(STATE_STARTED);
    }

    public boolean isFinished(){
        return state.equals(STATE_FINISHED);
    }

    public Stage() {
    }

    public Stage(String name) {
        this.name = name;
    }

    public boolean isPending() {
        return state.equals(STATE_PENDING);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String parsedStartDate() {
        return getStartDate() != null ? DateUtils.stringToString(getStartDate(), DateUtils.FULL_SERVER_DATE, DateUtils.STANDARD_DATE_FORMAT) : "";
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String parsedEndDate() {
        return getEndDate() != null ? DateUtils.stringToString(getEndDate(), DateUtils.FULL_SERVER_DATE, DateUtils.STANDARD_DATE_FORMAT) : "";
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Positions position() {
        return position;
    }

    public void setPosition(Positions position) {
        this.position = position;
    }

    public int getPointResource() {
        switch (state) {
            case STATE_FINISHED:
                return R.drawable.stage_circle_primary_dark;
            case STATE_STARTED:
                return R.drawable.stage_circle_primary_color;
            case STATE_PENDING:
                return R.drawable.stage_circle_gray;
        }

        return R.drawable.stage_circle_gray;
    }

    public int getTextColor() {
        switch (state) {
            case STATE_FINISHED:
                return R.color.colorPrimaryDark;
            case STATE_STARTED:
                return R.color.colorPrimary;
            case STATE_PENDING:
                return R.color.lighterGray;
        }

        return R.color.lighterGray;
    }


}
