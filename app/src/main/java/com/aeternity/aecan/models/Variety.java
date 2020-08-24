package com.aeternity.aecan.models;

import com.aeternity.aecan.util.gson.ExcludeSerialization;

import java.io.Serializable;

public class Variety implements Item, Serializable {
    private Integer id;
    private String name;
    private String shortName;
    @ExcludeSerialization
    private Boolean isSelected;

    public Variety(Integer id, String name, Boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public Boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public Integer getId() {
        return id;

    }
}
