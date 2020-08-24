package com.aeternity.aecan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.TextSimpleBinding;
import com.aeternity.aecan.databinding.TextSimpleInformationBinding;

import java.util.ArrayList;

public class SimpleBoxInformationAdapter extends RecyclerView.Adapter<SimpleBoxInformationAdapter.ViewHolder> {
    private ArrayList<String> informations;

    public SimpleBoxInformationAdapter(ArrayList<String> informations) {
        this.informations = informations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding v = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                viewType, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.text_simple;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String information = informations.get(position);
        holder.bind(information);
    }

    @Override
    public int getItemCount() {
        return informations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextSimpleBinding binding;


        public ViewHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            this.binding = (TextSimpleBinding) itemView;
            textView = itemView.getRoot().getRootView().findViewById(R.id.textViewSimple);
        }

        public void bind(String info){
            binding.setText(info);
        }
    }
}
