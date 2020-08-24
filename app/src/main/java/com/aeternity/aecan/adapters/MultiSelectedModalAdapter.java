package com.aeternity.aecan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.models.Item;

import java.util.ArrayList;

public class MultiSelectedModalAdapter extends RecyclerView.Adapter<MultiSelectedModalAdapter.ListViewHolder> {

    private ArrayList<Item> items;
    private int resource;

    public MultiSelectedModalAdapter(ArrayList<Item> items, int resource) {
        this.items = items;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Item item = items.get(position);
        if (item.isSelected() == null)
            item.setSelected(false);

        if (item.isSelected()) {
            holder.textViewItem.setBackground(holder.itemView.getContext().getDrawable(R.drawable.variety_textview));
        } else {
            holder.textViewItem.setBackground(holder.itemView.getContext().getDrawable(R.color.transparent));
        }
        holder.textViewItem.setText(item.getText());
        holder.textViewItem.setOnClickListener(view -> {
            if (item.isSelected()) {
                holder.textViewItem.setBackground(holder.itemView.getContext().getDrawable(R.color.transparent));
                item.setSelected(false);
            } else {
                holder.textViewItem.setBackground(holder.itemView.getContext().getDrawable(R.drawable.variety_textview));
                item.setSelected(true);
            }
        });
    }

    public ArrayList<Integer> getPositionSelectedItems() {
        ArrayList<Integer> selectedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isSelected()) selectedItems.add(items.indexOf(item));
        }
        return selectedItems;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<Integer> getPositionSelected() {
        return getPositionSelectedItems();
    }

    public ArrayList<String> getIDsSelected() {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isSelected()) selectedItems.add(item.getId().toString());
        }
        return selectedItems;
    }

    public int getItemIdAt(int index) {
        try {
            return items.get(index).getId();

        } catch (Exception ex) {
            return -1;
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
        }
    }

    public void setItems(ArrayList<Item> items) {

        this.items = items;
        notifyDataSetChanged();

    }
}
