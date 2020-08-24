package com.aeternity.aecan.views.fragments;

import android.os.Bundle;
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

import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.LotsAdapter;
import com.aeternity.aecan.databinding.FragmentSearchBinding;
import com.aeternity.aecan.interfaces.GoToLotDetailInterface;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.EditTextValidationUtils;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.SearchViewModel;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchFragment extends BaseFragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;
    private ScannerFragment scannerFragment;
    private MutableLiveData<String> permissionGrant = new MutableLiveData<>();
    private LotsAdapter lotsAdapter;

    private SingleLiveEvent<Lot> lot = new SingleLiveEvent<>();
    private MutableLiveData<ErrorResponse> error = new MutableLiveData<>();

    public SearchFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.fragment_search, container, false);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        binding.setSearchViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scannerFragment = (ScannerFragment) getChildFragmentManager().findFragmentById(R.id.fragmentScanner);
        observeFragment();
    }

    private void observeFragment() {
        viewModel.getAction().observe(this, aBoolean -> validateField());
        viewModel.getOnSearchLotRepositorySuccessData().observe(this, onSearchLotRepositorySuccess -> {
            hideActivityOverlay();
            binding.editTextSearchBatch.setText(null);
            getLot().setValue(onSearchLotRepositorySuccess.getLotResponse().getLot());
            //setUpRecycler(onSearchLotRepositorySuccess.getLotResponse().getLot());
            binding.recyclerResults.requestFocus();
            scannerFragment.initCameraSource();
        });
        viewModel.getOnSearchLotRepositoryFailData().observe(this, errorResponse -> {
            binding.editTextSearchBatch.setText(null);
            error.postValue(errorResponse);
            setUpRecycler(null);
            hideActivityOverlay();
            //scannerFragment.initCameraSource();
        });
        scannerFragment.getQrCodeScanned().observe(this, firebaseVisionBarcode -> {
            binding.editTextSearchBatch.setText(firebaseVisionBarcode.getDisplayValue());
            validateField();
        });
        scannerFragment.getPermissionGrant().observe(this, permissionGrant -> {
            this.permissionGrant.postValue(permissionGrant);
        });
    }

    public void validateField() {
        if (EditTextValidationUtils.validateTextNonEmpty(binding.editTextSearchBatch)) {
            showActivityOverlay();
            viewModel.searchLot(binding.editTextSearchBatch.getText().toString());
        }
    }

    public void initCameraSource() {
        scannerFragment.initCameraSource();
    }

    public MutableLiveData<String> getPermissionGrant() {
        return permissionGrant;
    }

    public void setUpRecycler(Lot lot) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerResults.setLayoutManager(linearLayoutManager);
        ArrayList<Lot> batches = new ArrayList<>();
        if (lot != null) batches.add(lot);
        lotsAdapter = new LotsAdapter(batches);
        binding.recyclerResults.setAdapter(lotsAdapter);
        observerRecycler();
    }

    private void observerRecycler() {
        lotsAdapter.getItemSelected().observe(this, it -> {
            if (getActivity() instanceof GoToLotDetailInterface)
                ((GoToLotDetailInterface) getActivity()).goToLotDetail(it);
            else
                lot.setValue(it);
        });
    }

    public SingleLiveEvent<Lot> getLot() {
        return lot;
    }

    public MutableLiveData<ErrorResponse> getError() {
        return error;
    }

    public void tryToInitCamera() {
        try {
            scannerFragment.initCameraSource();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
