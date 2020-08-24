package com.aeternity.aecan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.dynamic.SelectionOptions;

import java.util.ArrayList;

public class SelectDialogAdapter extends RecyclerView.Adapter<SelectDialogAdapter.ListViewHolder> {
    private ArrayList<SelectionOptions> items;
    private int resource;
    private Integer positionSelected = -1;

    public SelectDialogAdapter(ArrayList<SelectionOptions> items, int resource) {
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
        SelectionOptions item = items.get(position);
        holder.textViewItem.setText(item.getText());
        if (item.isSelected()) {
            positionSelected = position;
            holder.textViewItem.setBackground(holder.textViewItem.getContext().getDrawable(R.drawable.variety_textview));
        } else {
            holder.textViewItem.setBackground(holder.textViewItem.getContext().getDrawable(R.color.transparent));

        }
        holder.textViewItem.setOnClickListener(view -> {
            if (positionSelected > -1 && positionSelected != position) {
                items.get(positionSelected).setSelected(false);
            }
            if (item.isSelected()) {
                positionSelected = -1;
                item.setSelected(false);
            } else {
                positionSelected = position;
                item.setSelected(true);
            }
            notifyDataSetChanged();
        });
    }


    public Integer getPositionSelected() {
        return positionSelected;
    }

    public ArrayList<String> getIDsSelected() {
        ArrayList<String> result = new ArrayList<>();
        result.add(items.get(positionSelected).getId().toString());
        return result;
    }

    public String getIDSelected() {
        return items.get(positionSelected).getId();
    }


    public void setItems(ArrayList<SelectionOptions> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
        }
    }
}
