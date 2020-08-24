package com.aeternity.aecan.views.fragments.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ListAdapter;
import com.aeternity.aecan.databinding.ComponentCardBinding;
import com.aeternity.aecan.models.dynamic.DynamicItem;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.views.fragments.base.BaseFragment;
import com.aeternity.aecan.views.modal.InputDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ValueEditFragment extends BaseFragment implements InputDialogFragment.InputDialogDataCallback, InputDialogFragment.InputDialogNetworkCallback {

    private static final String ITEMS_KEY = "items";
    private ComponentCardBinding binding;
    private ArrayList<DynamicItem> elements;
    private boolean isEditable;

    public ValueEditFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.component_card, container, false);

        if (getArguments() != null && getArguments().containsKey(ITEMS_KEY)) {

            binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

            elements = (ArrayList<DynamicItem>) getArguments().get(ITEMS_KEY);

            ListAdapter adapter = new ListAdapter(elements, BR.valueEditItem, R.layout.component_value_edit_array);

            adapter.getItemSelected().observe(this, object -> {


                if (((DynamicItem) object).getEditable() && isEditable) {
                    try {
                        DynamicItem item = (DynamicItem) object;
                        DialogFragment dialogFragment = item.getItemAction().getFragment(this);
                        if (dialogFragment != null)
                            dialogFragment.show(getChildFragmentManager(), item.getName());
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            binding.recycler.setAdapter(adapter);

        }

        return binding.getRoot();
    }

    public static ValueEditFragment newInstance(boolean isEditable, ArrayList<DynamicItem> items) {
        ValueEditFragment frag = new ValueEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS_KEY, items);
        frag.isEditable = isEditable;
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onDataSet(String data, String identifier) {

        for (DynamicItem keyValueComponent : elements) {
            if (keyValueComponent.equals(identifier)) {
                System.out.println("data callback item: " + keyValueComponent.getItemAction().getInputModal().getUrlPath() + " " + data);
                break;
            }
        }

    }

    @Override
    public void onError(ErrorResponse errorResponse) {
        Toast.makeText(this.getContext(), errorResponse.getFormattedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess() {

    }
}
