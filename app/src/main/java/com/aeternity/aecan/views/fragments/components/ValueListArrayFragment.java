package com.aeternity.aecan.views.fragments.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ListAdapter;
import com.aeternity.aecan.databinding.ComponentValueListArrayBinding;
import com.aeternity.aecan.models.dynamic.DynamicItem;
import com.aeternity.aecan.models.dynamic.ItemAction;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ValueListArrayFragment extends BaseFragment {

    private static final String ITEMS_KEY = "items";
    private static final String TITLE_KEY = "title";
    private static final String COMPONENT_ACTION = "component_action";
    private static final String DELETE_ACTION = "delete_action";
    private static final String MINIMUM_ITEMS_KEY = "minimumItemsKey";
    private static final String HAS_ACTION = "action";

    private ComponentValueListArrayBinding binding;
    private MutableLiveData<Boolean> editable = new MutableLiveData<>(true);
    private ItemAction componentAction;
    private ItemAction deleteAction;
    private ArrayList<DynamicItem> elements;
    private String title = "";
    private boolean isEditable;
    private int minimumItems = 3;
    private boolean hasMoreItemsThanMinimum = true;

    public ValueListArrayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.component_value_list_array, container, false);

        if (getArguments() != null && getArguments().containsKey(ITEMS_KEY) &&
                getArguments().containsKey(TITLE_KEY) &&
                getArguments().containsKey(MINIMUM_ITEMS_KEY) &&
                getArguments().containsKey(DELETE_ACTION) &&
                getArguments().containsKey(HAS_ACTION)) {

            editable.setValue(getArguments().getBoolean(HAS_ACTION) && isEditable);
            title = (getArguments().getString(TITLE_KEY));

            binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

            elements = (ArrayList<DynamicItem>) getArguments().get(ITEMS_KEY);

            componentAction = (ItemAction) getArguments().getSerializable(COMPONENT_ACTION);
            deleteAction = (ItemAction) getArguments().getSerializable(DELETE_ACTION);

            minimumItems = getArguments().getInt(MINIMUM_ITEMS_KEY);
            binding.recycler.setAdapter(new ListAdapter(
                    subList(elements, minimumItems),
                    BR.keyValueItem,
                    R.layout.component_key_value_array));

        }

        binding.setValueListArrayFragment(this);
        return binding.getRoot();
    }

    public static ValueListArrayFragment newInstance(boolean isEditable, ArrayList<DynamicItem> items, ItemAction componentAction, ItemAction deleteAction, String title, int minimumItems, boolean hasAction) {
        ValueListArrayFragment frag = new ValueListArrayFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS_KEY, items);
        args.putSerializable(COMPONENT_ACTION, componentAction);
        args.putSerializable(DELETE_ACTION, deleteAction);
        args.putBoolean(HAS_ACTION, hasAction);
        args.putString(TITLE_KEY, title);
        args.putInt(MINIMUM_ITEMS_KEY, minimumItems);
        frag.isEditable = isEditable;
        frag.setArguments(args);
        return frag;
    }

    public void buttonAddPressed() {
        if (editable.getValue() != null && editable.getValue() && isEditable) {
            DialogFragment dialogFragment = componentAction.getFragment(this);
            if (dialogFragment != null)
                dialogFragment.show(this.getChildFragmentManager(), componentAction.getActionType());
        }
    }

    public void buttonMinusPressed() {
        if (editable.getValue() != null && editable.getValue() && isEditable) {
            DialogFragment dialogFragment = deleteAction.getFragment(this);
            if (dialogFragment != null)
                dialogFragment.show(this.getChildFragmentManager(), deleteAction.getActionType());
        }
    }

    public void buttonShowMorePressed(View v) {
        if (((ToggleButton) v).isChecked()) {
            binding.recycler.setAdapter(new ListAdapter(
                    subList(elements, minimumItems),
                    BR.keyValueItem,
                    R.layout.component_key_value_array));
        } else {
            binding.recycler.setAdapter(new ListAdapter(
                    elements,
                    BR.keyValueItem,
                    R.layout.component_key_value_array));
        }

    }

    public String getTitle() {
        return title;
    }

    private ArrayList<DynamicItem> subList(ArrayList<DynamicItem> list, int number) {
        if (number >= list.size()) {
            hasMoreItemsThanMinimum = false;
            return list;
        } else {
            hasMoreItemsThanMinimum = true;
            return new ArrayList<>(list.subList(0, number));
        }
    }

    public boolean hasMoreItemsThanMinimum() {
        return hasMoreItemsThanMinimum;
    }

    public MutableLiveData<Boolean> getEditable() {
        return editable;
    }

    public void setEditable(MutableLiveData<Boolean> editable) {
        this.editable = editable;
    }
}
