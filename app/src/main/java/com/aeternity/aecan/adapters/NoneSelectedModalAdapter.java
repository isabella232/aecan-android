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

public class NoneSelectedModalAdapter extends RecyclerView.Adapter<NoneSelectedModalAdapter.ListViewHolder> {

    private ArrayList<Item> items;
    private int resource;

    public NoneSelectedModalAdapter(ArrayList<Item> items, int resource) {
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
        holder.textViewBeacon.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<Integer> getPositionSelected() {
        return new ArrayList<Integer>();
    }

    public ArrayList<String> getIDsSelected() {
        return new ArrayList<String>();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewBeacon;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBeacon = itemView.findViewById(R.id.textViewItem);
        }
    }

    public void setItems(ArrayList<Item> items) {

        this.items = items;
        notifyDataSetChanged();

    }
}
