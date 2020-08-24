package com.aeternity.aecan.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.FragmentHeaderNewLotDetailBinding;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.viewModels.HeaderNewLotDetailViewModel;

public class HeaderNewLotDetailFragment extends Fragment {
    private FragmentHeaderNewLotDetailBinding binding;
    private HeaderNewLotDetailViewModel viewModel;
    private Lot lot;
    private HeaderNewLotDetailListener listener;

    public interface HeaderNewLotDetailListener{
        void onBeaconsPressed();
    }

    public HeaderNewLotDetailFragment() {
        // Required empty public constructor
    }

    public HeaderNewLotDetailFragment(Lot lot,HeaderNewLotDetailListener listener) {
        this.lot = lot;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_header_new_lot_detail, container, false);
        viewModel = ViewModelProviders.of(this).get(HeaderNewLotDetailViewModel.class);
        binding.setHeaderNewLotDetailViewModel(viewModel);
        viewModel.setLot(lot);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observerViewModel();
    }

    private void observerViewModel() {
        viewModel.getBeaconsButton().observe(this,aBoolean -> {
            listener.onBeaconsPressed();
        });
    }
}
