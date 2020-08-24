package com.aeternity.aecan.views;


import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityNewLotDetailBinding;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.models.Stage;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.viewModels.NewLotDetailViewModel;
import com.aeternity.aecan.views.base.ToolbarActivity;
import com.aeternity.aecan.views.fragments.HeaderNewLotDetailFragment;
import com.aeternity.aecan.views.fragments.StageListFragment;
import com.aeternity.aecan.views.modal.NoneSelectedModalFragment;

import java.util.ArrayList;

public class NewLotDetailActivity extends ToolbarActivity implements HeaderNewLotDetailFragment.HeaderNewLotDetailListener {
    private ActivityNewLotDetailBinding binding;
    private NewLotDetailViewModel viewModel;
    private StageListFragment stageListFragment;
    private Lot lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = putContentView(R.layout.activity_new_lot_detail);
        viewModel = ViewModelProviders.of(this).get(NewLotDetailViewModel.class);
        binding.setNewLotDetailViewModel(viewModel);

        lot = (Lot) getIntent().getExtras().get("lot");
        if (lot == null || lot.getId() == null) return;
        observerLotDetail();
        setToolbarTitle(getString(R.string.lot_detail));
    }

    public void getLotDetailFromServer() {
        showActivityOverlay();
        viewModel.getLotDetail(lot.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLotDetailFromServer();
    }

    @Override
    public void onBeaconsPressed() {
        NoneSelectedModalFragment noneSelectedModalFragment = NoneSelectedModalFragment.newInstance(getString(R.string.associated_beacon), getString(R.string.accept), getBeaconsForModal());
        noneSelectedModalFragment.show(getSupportFragmentManager(), "noneSelectedModal");
    }

    public void observerLotDetail() {
        viewModel.getLotDetailSuccessData().observe(this, onGetLotDetailSuccess -> {
            hideActivityOverlay();
            putFragment(new HeaderNewLotDetailFragment(onGetLotDetailSuccess.getLotDetailResponse().getLot(), this), R.id.containerHeaderNewLotDetail);
            stageListFragment = StageListFragment.newInstance(onGetLotDetailSuccess.getLotDetailResponse().getLot());
            stageListFragment.getStageData().observe(this, this::goToStageDetailActivity);
            putFragment(stageListFragment, R.id.containerStagesDetail);
        });
        viewModel.getLotDetailFailData().observe(this, error -> {
            hideActivityOverlay();
            return;
        });

        viewModel.getButtonLaboratoryAnalysis().observe(this, aBoolean -> {
            if (viewModel.getLot().getValue().getPdfUrl() != null) {
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra("titleToolbar", "")
                        .putExtra("url", viewModel.getLot().getValue().getPdfUrl()));
            }
        });

        viewModel.getButtonAeternityLink().observe(this, aBoolean -> {
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra("titleToolbar", "")
                        .putExtra("url", viewModel.getLot().getValue().getAeternityLink()));
        });
    }

    private void goToStageDetailActivity(Stage stage) {
        startActivity(new Intent(this, StageDetailActivity.class)
                .putExtra(Constants.STAGE_KEY, stage)
                .putExtra(Constants.LOT_KEY, viewModel.getLot().getValue())
        );
    }

    public ArrayList<Item> getBeaconsForModal() {
        return new ArrayList<>(viewModel.getLot().getValue().getBeacons());
    }
}
