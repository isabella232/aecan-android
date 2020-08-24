package com.aeternity.aecan.viewModels;

import android.os.Bundle;

import com.aeternity.aecan.models.dynamic.DynamicItem;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import java.util.ArrayList;

import static com.aeternity.aecan.views.fragments.components.ExpandableItemFragment.*;

public class ExpandableItemViewModel extends BaseViewModel {
    private ArrayList<DynamicItem> items = new ArrayList<>();

    public ArrayList<DynamicItem> getItems() {
        return items;
    }

    public void setArguments(Bundle arguments) {
        items = (ArrayList<DynamicItem>) arguments.getSerializable(ITEMS);
    }
}
