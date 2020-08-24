package com.aeternity.aecan.models.dynamic;

import com.aeternity.aecan.models.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class DynamicItem implements Serializable, Item {

    private String name; //title of item
    private String value; //body of item
    private String title;
    private String key; //used as unique identifier
    private ItemAction itemAction;
    private ArrayList<String> description;
    private String state;
    private String greenMessage;
    private String buttonText;
    private Boolean editable;

    private boolean isSelected = false;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getGreenMessage() {
        return greenMessage;
    }

    public String getKey() {
        return key;
    }

    public ItemAction getItemAction() {
        return itemAction;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public String getState() {
        return state;
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
        try {
            return Integer.valueOf(value);

        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public Boolean getEditable() {
        return editable;
    }


    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public DynamicItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getButtonText() {
        return buttonText;
    }
}
