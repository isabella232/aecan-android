package com.aeternity.aecan.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.models.Lot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LotsAdapter extends RecyclerView.Adapter<LotsAdapter.ViewHolder> {
    private ArrayList<Lot> items;
    private MutableLiveData<Lot> itemSelected = new MutableLiveData<>();

    public LotsAdapter(ArrayList<Lot> items) {
        this.items = items;
    }

    public void setItems(ArrayList<Lot> items) {
        this.items = items;
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
        Lot item = items.get(position);
        holder.itemRowBinding.getRoot().setOnClickListener(v -> getItemSelected().postValue(item));
        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).isFinished())
            return R.layout.item_lot_finished;
        else
            return R.layout.item_lot_in_process;
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

        void bind(Lot obj) {
            itemRowBinding.setVariable(BR.lot, obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public MutableLiveData<Lot> getItemSelected() {
        return itemSelected;
    }

}