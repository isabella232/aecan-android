package com.aeternity.aecan.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.LotsAdapter;
import com.aeternity.aecan.databinding.FragmentLotsBinding;
import com.aeternity.aecan.interfaces.GoToLotDetailInterface;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.viewModels.FinishedLotsViewModel;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LotsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private FinishedLotsViewModel viewModel;
    private FragmentLotsBinding binding;
    private LotsAdapter lotsAdapter;
    private MutableLiveData<Lot> lot = new MutableLiveData<>();

    public LotsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this).get(FinishedLotsViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.fragment_lots, container, false);
        viewModel = ViewModelProviders.of(this).get(FinishedLotsViewModel.class);
        binding.setFinishedLotsViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefresh.setOnRefreshListener(this);

        observerFinishedLots();
        getFinishedLotsFromServer();
        try {
            observerRecycler();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void getFinishedLotsFromServer() {
        try {
            if (Constants.isOperator()) {
                viewModel.getFinishedLots();
            } else {
                viewModel.getLotsForUser();
            }
            showActivityOverlay();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    private void observerFinishedLots() {
        viewModel.getOnGetLotsSuccessData().observe(this, onGetLotsSuccess -> {
            setUpRecycler(onGetLotsSuccess.getLostResponse().getLots());
            binding.swipeRefresh.setRefreshing(false);
            hideActivityOverlay();
        });
        viewModel.getOnGetLotsFailData().observe(this, errorResponse -> {
            binding.swipeRefresh.setRefreshing(false);
            hideActivityOverlay();
        });
    }

    private void setUpRecycler(ArrayList<Lot> lots) {

        if (binding.recyclerResults.getLayoutManager() == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            binding.recyclerResults.setLayoutManager(linearLayoutManager);

            //Stops items doing a default flashing animation on individual refresh
            RecyclerView.ItemAnimator animator = binding.recyclerResults.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
        }

        if (lotsAdapter == null) {
            lotsAdapter = new LotsAdapter(lots);
            binding.recyclerResults.setAdapter(lotsAdapter);
            binding.recyclerResults.setHasFixedSize(true);
            observerRecycler();
        }

        lotsAdapter.setItems(lots);
        lotsAdapter.notifyDataSetChanged();
    }

    public MutableLiveData<Lot> getLot() {
        return lot;
    }

    private void observerRecycler() {
        lotsAdapter.getItemSelected().observe(this, it -> {
            if (getActivity() instanceof GoToLotDetailInterface)
                ((GoToLotDetailInterface) getActivity()).goToLotDetail(it);
            else
                lot.setValue(it);
        });
        //lotsAdapter.getItemSelected().observe(this, it -> getLot().setValue(it));
    }

    @Override
    public void onRefresh() {
        getFinishedLotsFromServer();
    }
}
