package com.aeternity.aecan.models.dynamic;

import androidx.fragment.app.Fragment;

import com.aeternity.aecan.views.fragments.BlankFragment;
import com.aeternity.aecan.views.fragments.components.ChartFragment;
import com.aeternity.aecan.views.fragments.components.ExpandableItemFragment;
import com.aeternity.aecan.views.fragments.components.NameValueFragment;
import com.aeternity.aecan.views.fragments.components.ValueEditFragment;
import com.aeternity.aecan.views.fragments.components.ValueListArrayFragment;

import java.util.ArrayList;
import java.util.List;

public class Component {
    private String componentType;

    private int order;
    private String title;
    private int minAmountOfItemsToShow;

    private ItemAction componentAction;
    private ItemAction deleteAction;

    private ArrayList<DynamicItem> items;
    private Boolean editable;

    private String unit;
    private ArrayList<ChartSeries> chartSeries = new ArrayList<>();
    private ArrayList<ChartEntry> entriesList = new ArrayList<>();

    public String getTag() {
        return componentType + order;
    }

    public Fragment makeFragment(boolean isEditable) {
        if (componentType == null)
            return new BlankFragment();

        if (editable == null)
            editable = true;

        if (items != null)
            for (DynamicItem dynamicItem : items) {
                if (dynamicItem.getEditable() == null) {
                    dynamicItem.setEditable(editable);
                }
            }

        switch (componentType) {
            case "component_key_value_array":
                return NameValueFragment.newInstance(items);

            case "component_value_edit_array":
                return ValueEditFragment.newInstance(isEditable, items);

            case "component_value_list_array":
                return ValueListArrayFragment.newInstance(isEditable, items, componentAction, deleteAction, title, minAmountOfItemsToShow, editable);

            case "component_procedures_list":
                return ExpandableItemFragment.newInstance(items,title);

            case "component_chart":
                return ChartFragment.newInstance(title, unit, chartSeries, entriesList, componentAction, editable);
        }

        return null;

    }

    public String getComponentType() {
        return componentType;
    }

    public int getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public int getMinAmountOfItemsToShow() {
        return minAmountOfItemsToShow;
    }

    public ItemAction getComponentAction() {
        return componentAction;
    }

    public List<DynamicItem> getItems() {
        return items;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMinAmountOfItemsToShow(int minAmountOfItemsToShow) {
        this.minAmountOfItemsToShow = minAmountOfItemsToShow;
    }

    public void setComponentAction(ItemAction componentAction) {
        this.componentAction = componentAction;
    }

    public void setItems(ArrayList<DynamicItem> items) {
        this.items = items;
    }

    public Component() {
    }

    public Boolean isHasAction() {
        return editable;
    }
}
