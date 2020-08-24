package com.aeternity.aecan.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.TextSimpleBinding;
import com.aeternity.aecan.databinding.TextSimpleWithColorBinding;

import java.util.ArrayList;

public class SimpleBoxInformationWithColorAdapter extends RecyclerView.Adapter<SimpleBoxInformationWithColorAdapter.ViewHolder> {
    private ArrayList<String> informations;

    public SimpleBoxInformationWithColorAdapter(ArrayList<String> informations) {
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
        return R.layout.text_simple_with_color;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String information = informations.get(position);
        float margin = 0f;

        if(information.isEmpty())
            holder.textView.setVisibility(View.GONE);
        else
            holder.textView.setVisibility(View.VISIBLE);

        while (information.startsWith("\t")) {
            margin = margin + holder.getResources().getDimension(R.dimen.margin_30);
            information = information.substring(1);
        }

        holder.binding.setVariable(BR.marginText, margin);
        holder.bind(information);
    }

    @Override
    public int getItemCount() {
        return informations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextSimpleWithColorBinding binding;


        public ViewHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            this.binding = (TextSimpleWithColorBinding) itemView;
            textView = itemView.getRoot().getRootView().findViewById(R.id.textViewSimple);

        }

        public Resources getResources() {
            return textView.getContext().getResources();
        }

        public void bind(String info) {
            binding.setText(info);
        }
    }
}
