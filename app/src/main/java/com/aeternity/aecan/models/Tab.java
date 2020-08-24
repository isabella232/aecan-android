package com.aeternity.aecan.models;

import com.aeternity.aecan.models.dynamic.Component;

import java.io.Serializable;
import java.util.ArrayList;

public class Tab implements Serializable {
    private String title;
    private String badge;
    private String emptyMessage;

    private ArrayList<Component> components;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }
}
