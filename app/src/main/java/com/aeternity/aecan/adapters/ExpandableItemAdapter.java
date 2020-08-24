package com.aeternity.aecan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.models.ExpandableItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExpandableItemAdapter extends RecyclerView.Adapter<ExpandableItemAdapter.ViewHolder> {
    private ArrayList<ExpandableItem> items;
    private MutableLiveData<ExpandableItem> buttonPressed = new MutableLiveData<>();
    private final int variableName;
    private final int layout;

    public ExpandableItemAdapter(ArrayList<ExpandableItem> items, int variableName, int layout) {
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

    private ArrayList<String> cleanEmptyStrings(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();
        for (String string : input) {
            if (!string.isEmpty()) {
                output.add(string);
            }
        }
        return output;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpandableItem item = items.get(position);

        SimpleBoxInformationWithColorAdapter adapter = new SimpleBoxInformationWithColorAdapter(cleanEmptyStrings(item.getSubTitles()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemRowBinding.getRoot().getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        holder.recyclerViewSubtitleExpandable.setLayoutManager(linearLayoutManager);
        holder.recyclerViewSubtitleExpandable.setAdapter(adapter);
        holder.titlecontainer.setOnClickListener(view -> {
            if (item.stateSubtitle) {
                item.setStateSubtitle(false);
            } else {
                for (ExpandableItem itemExpandable : items) {
                    if (itemExpandable.stateSubtitle) {
                        itemExpandable.setStateSubtitle(false);
                    }
                }
                item.setStateSubtitle(true);
            }
            notifyDataSetChanged();
        });
        holder.button.setOnClickListener(v ->
                getButtonPressed().setValue(item)
        );
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
        private ConstraintLayout titlecontainer;
        private RecyclerView recyclerViewSubtitleExpandable;
        private Button button;

        ViewHolder(ViewDataBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
            titlecontainer = itemRowBinding.getRoot().findViewById(R.id.containerExpandableTitle);
            recyclerViewSubtitleExpandable = itemRowBinding.getRoot().findViewById(R.id.recyclerViewSubtitleExpandable);
            button = itemRowBinding.getRoot().findViewById(R.id.buttonExpandableItem);
        }

        void bind(ExpandableItem obj, int variableName) {
            itemRowBinding.setVariable(variableName, obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public MutableLiveData<ExpandableItem> getButtonPressed() {
        return buttonPressed;
    }


}