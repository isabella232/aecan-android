package com.aeternity.aecan.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.models.Positions;
import com.aeternity.aecan.models.Stage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<?> items;
    private MutableLiveData<Object> itemSelected = new MutableLiveData<>();
    private final int variableName;
    private final int layout;

    public void setItems(ArrayList<?> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public ListAdapter(ArrayList<?> items, int variableName, int layout) {
        this.items = items;
        this.variableName = variableName;
        this.layout = layout;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding v = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                viewType, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = items.get(position);

        if (item instanceof Stage) {
            if (position == 0) {
                ((Stage) item).setPosition(Positions.FIRST);
            } else if (position == getItemCount() - 1) {
                ((Stage) item).setPosition(Positions.LAST);
            }
            if (position != 0 && position != getItemCount() - 1) {
                ((Stage) item).setPosition(Positions.MIDDLE);
            }
        }

        try {
            if (position == getItemCount() - 1) {
                holder.itemRowBinding.setVariable(BR.isLastValueEditItem, true);
            } else {
                holder.itemRowBinding.setVariable(BR.isLastValueEditItem, false);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        holder.itemRowBinding.getRoot().setOnClickListener(v -> getItemSelected().postValue(item));
        holder.bind(item, variableName);
    }


    @Override
    public int getItemViewType(int position) {
        return layout;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding itemRowBinding;

        ViewHolder(ViewDataBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        void bind(Object obj, int variableName) {
            itemRowBinding.setVariable(variableName, obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public MutableLiveData<Object> getItemSelected() {
        return itemSelected;
    }

    public ArrayList<?> getItems() {
        return items;
    }
}