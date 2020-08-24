package com.aeternity.aecan.models;

import com.aeternity.aecan.util.gson.ExcludeSerialization;

import java.io.Serializable;

public class Beacon implements Item , Serializable {
    private Integer id;
    private String identifier;
    @ExcludeSerialization
    private Boolean isSelected;

    public Beacon(Integer id, String identifier, Boolean isSelected) {
        this.id = id;
        this.identifier = identifier;
        this.isSelected = isSelected;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getText() {
        return identifier;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    @Override
    public Integer getId() {
        return id;
    }
}


