package com.aeternity.aecan.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayoutMediator;
import com.aeternity.aecan.BuildConfig;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.HomeViewPagerAdapter;
import com.aeternity.aecan.databinding.ActivitySearchBinding;
import com.aeternity.aecan.helpers.DialogHelper;
import com.aeternity.aecan.interfaces.GoToLotDetailInterface;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.viewModels.HomeViewModel;
import com.aeternity.aecan.views.base.ToolbarActivity;
import com.aeternity.aecan.views.fragments.InitializedLotsFragment;
import com.aeternity.aecan.views.fragments.LotsFragment;
import com.aeternity.aecan.views.fragments.SearchFragment;

public class HomeActivity extends ToolbarActivity implements GoToLotDetailInterface {

    private ActivitySearchBinding binding;
    private HomeViewModel viewModel;
    private SearchFragment searchFragment;
    private LotsFragment lotsFragment;
    private InitializedLotsFragment initializedLotsFragment;
    private int MY_PERMISSIONS_REQUEST = 421;
    private int ACTIVITY_NEW_LOT = 523;
    private String OPERATOR = "operator";
    private String TAG = "HomeActivity";
    private HomeViewPagerAdapter homeViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = super.putContentView(R.layout.activity_search);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        binding.setHomeViewModel(viewModel);
        setLogoutVisible();
        initFragments();

        test();
        super.hideBackArrow();

        if (BuildConfig.app_mode.equals(OPERATOR)) {
            setToolbarTitle(getResources().getString(R.string.list_of_lots));
            homeViewPagerAdapter = new HomeViewPagerAdapter(this, initializedLotsFragment, lotsFragment);
            binding.buttonCreateLot.setVisibility(View.VISIBLE);


        } else {
            setToolbarTitle(getResources().getString(R.string.search));
            homeViewPagerAdapter = new HomeViewPagerAdapter(this, searchFragment, lotsFragment);
            binding.buttonCreateLot.setVisibility(View.GONE);

        }
        binding.viewPagerSearch.setAdapter(homeViewPagerAdapter);
        setTabsTitles();
        observeFragment();
    }


    private void observeFragments() {
        if (initializedLotsFragment != null) {
            initializedLotsFragment.getLot().observe(this, it -> {
                goToLotDetail(it);
                Log.w(TAG, "initializedLotsFragment active");
            });
        }

        if (searchFragment != null) {

            searchFragment.getLot().observe(this, it -> {
                goToLotDetail(it);
            });
            searchFragment.getError().observe(this, this::showErrorMessage);
            searchFragment.getPermissionGrant().observe(this, this::askForPermissions);
        }

        if (lotsFragment != null) {
            lotsFragment.getLot().observe(this, it -> {
                Log.w(TAG, "lotsFragment active");
                goToLotDetail(it);
            });
        }

    }

    private void initFragments() {
        if (BuildConfig.app_mode.equals(OPERATOR)) {
            initializedLotsFragment = new InitializedLotsFragment();
        } else {
            searchFragment = new SearchFragment();
        }
        lotsFragment = new LotsFragment();

        observeFragments();
    }

    private void showErrorMessage(ErrorResponse response) {

        DialogHelper.showStaticErrorDialog(this, response.getMessage(), "Aceptar", searchFragment::tryToInitCamera).show();
    }

    public void goToLotDetail(Lot lot) {
        startActivity(new Intent(this, NewLotDetailActivity.class).putExtra("lot", lot));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        Log.w(TAG, "onCreate");
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    public void onAttachedToWindow() {
        Log.w(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
        observeAndRefresh();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.w(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "onDestroy");
        super.onDestroy();
    }

    private void observeAndRefresh() {
        try {
            if (initializedLotsFragment.isAdded()) {
                initializedLotsFragment.onRefresh();
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        try {
            lotsFragment.onRefresh();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        observeFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(TAG, "onResume");

        observeAndRefresh();
    }

    private void setTabsTitles() {
        new TabLayoutMediator(binding.tabLayout, binding.viewPagerSearch, (tab, position) -> {
            switch (position) {
                case 0:
                    if (BuildConfig.app_mode.equals(OPERATOR))
                        tab.setText(getString(R.string.in_process));
                    else
                        tab.setText(getString(R.string.search_tab));
                    break;
                case 1:
                    if (BuildConfig.app_mode.equals(OPERATOR))
                        tab.setText(getString(R.string.finish));
                    else
                        tab.setText(getString(R.string.lots_tab));
                    break;
            }
        }).attach();
    }


    private void observeFragment() {
        viewModel.getViewModelAction().observe(this, ignored -> goToNewLotActivity());
    }

    private void goToNewLotActivity() {

        Intent createNewLotIntent = new Intent(this, NewLotActivity.class);

        createNewLotIntent.setFlags(createNewLotIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivityForResult(createNewLotIntent, ACTIVITY_NEW_LOT);
    }

    public void askForPermissions(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            searchFragment.initCameraSource();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_NEW_LOT) {
            if (initializedLotsFragment != null) {
                initializedLotsFragment.onRefresh();
            }
        }
    }


    private void test() {

    }

}
