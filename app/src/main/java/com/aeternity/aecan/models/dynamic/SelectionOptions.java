package com.aeternity.aecan.models.dynamic;

import com.aeternity.aecan.models.Item;

import java.io.Serializable;

public class SelectionOptions {

    private String id;
    private String text;
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SelectionOptions(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
