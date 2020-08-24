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
import com.aeternity.aecan.databinding.FragmentInitializedLotsBinding;
import com.aeternity.aecan.interfaces.GoToLotDetailInterface;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.InitializedLotsViewModel;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InitializedLotsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private InitializedLotsViewModel viewModel;
    private FragmentInitializedLotsBinding binding;
    private LotsAdapter lotsAdapter;
    private SingleLiveEvent<Lot> lot = new SingleLiveEvent<>();
    private String TAG = "InitializedLotsFragment";

    private InitializedLotsViewModel getViewModel() {
        if (isAdded())
            if (viewModel == null) {
                viewModel = ViewModelProviders.of(this).get(InitializedLotsViewModel.class);
                binding.setInitializedLotViewModel(viewModel);
            }
        return viewModel;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.fragment_initialized_lots, container, false);
        viewModel = getViewModel();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefresh.setOnRefreshListener(this);
        observerInitializedLots();
        getInitializedLotsFromServer();

    }

    private void observerInitializedLots() {
        getViewModel().getOnGetLotsSuccessData().observe(this, onGetLotsSuccess -> {
            hideActivityOverlay();
            binding.swipeRefresh.setRefreshing(false);
            setUpRecycler(onGetLotsSuccess.getLostResponse().getLots());
        });
        getViewModel().getOnGetLotsFailData().observe(this, errorResponse -> {
            binding.swipeRefresh.setRefreshing(false);
            hideActivityOverlay();
        });
    }


    public void getInitializedLotsFromServer() {
        getViewModel().getInitializedLots();
        showActivityOverlay();
    }

    public InitializedLotsFragment() {
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

    private void observerRecycler() {
        if (lotsAdapter != null) {
            lotsAdapter.getItemSelected().observe(this, it -> {
                if (getActivity() instanceof GoToLotDetailInterface)
                    ((GoToLotDetailInterface) getActivity()).goToLotDetail(it);
                else
                    lot.setValue(it);
            });
        }
    }


    public SingleLiveEvent<Lot> getLot() {
        return lot;
    }

    @Override
    public void onRefresh() {
        getInitializedLotsFromServer();
    }
}
