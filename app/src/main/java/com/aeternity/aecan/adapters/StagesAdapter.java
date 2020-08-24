package com.aeternity.aecan.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.models.Positions;
import com.aeternity.aecan.models.Stage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StagesAdapter extends RecyclerView.Adapter<StagesAdapter.ViewHolder> {
    private ArrayList<Stage> items;
    private MutableLiveData<Stage> itemSelected = new MutableLiveData<>();


    public StagesAdapter(ArrayList<Stage> items) {
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
        Stage item = items.get(position);
        if (position == 0) {
            item.setPosition(Positions.FIRST);
        } else if (position == getItemCount() - 1) {
            item.setPosition(Positions.LAST);
        }
        if (position != 0 && position != getItemCount() - 1) {
            item.setPosition(Positions.MIDDLE);
        }
        holder.itemRowBinding.getRoot().setOnClickListener(v -> getItemSelected().postValue(item));
        holder.bind(item);

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.stage_view;
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

        void bind(Stage obj) {
            itemRowBinding.setVariable(BR.stage, obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public MutableLiveData<Stage> getItemSelected() {
        return itemSelected;
    }


}