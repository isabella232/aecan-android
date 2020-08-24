package com.aeternity.aecan.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityNewLotBinding;
import com.aeternity.aecan.helpers.SelectorModalHelper;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.viewModels.NewLotViewModel;
import com.aeternity.aecan.views.base.ToolbarActivity;
import com.aeternity.aecan.views.fragments.SimpleBoxInformationFragment;
import com.aeternity.aecan.views.modal.InputDialogFragment;
import com.aeternity.aecan.views.modal.MultipleSelectModalFragment;
import com.aeternity.aecan.views.modal.SingleSelectModalFragment;

import java.util.ArrayList;


public class NewLotActivity extends ToolbarActivity implements InputDialogFragment.InputDialogDataCallback {
    private ActivityNewLotBinding binding;
    private NewLotViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_new_lot);
        viewModel = ViewModelProviders.of(this).get(NewLotViewModel.class);
        binding.setNewLotViewModel(viewModel);
        setToolbarTitle(getString(R.string.new_lot));
        getLotInformation();
        setUpObserver();
    }


    private void setUpObserver() {

        viewModel.getButtonAction().observe(this, action -> {
            switch (action) {
                case BATCH_NUMBER_PRESSED:
                    showNumberBatch();
                    break;
                case BATCH_BEACONS_PRESSED:
                    showSelectBeacons();
                    break;
                case BATCH_VARIETY_PRESSED:
                    showVarietyModal();
                    break;
                case SAVE_BUTTON_PRESSED:
                    createLot();
                    break;
            }
        });

        viewModel.getLot().observe(this, onGetLotInformationSuccess -> {
            setUpInformation(viewModel.getLot().getValue().getInformationToString());
        });

        viewModel.getOnGenericNetworkFail().observe(this, error -> {
            hideActivityOverlay();
            onGenericError(error);
        });

        viewModel.getVarieties().observe(this, onVarietiesRepositorySuccess -> {
            showOrHideOverlay();
        });

        viewModel.getOnCreateLotSuccessData().observe(this, onCreateLotSuccess -> {
            hideActivityOverlay();
            goToNewLotDetailActivity(onCreateLotSuccess.getCreateLotResponse().getLot());
        });


    }

    private void goToNewLotDetailActivity(Lot lot) {
        startActivity(new Intent(this, NewLotDetailActivity.class).putExtra("lot", lot));
    }


    public void showOrHideOverlay() {
        if (validateDataFromServer())
            hideActivityOverlay();
        else
            showActivityOverlay();
    }

    public boolean validateDataFromServer() {
        return viewModel.getBeacons().getValue() != null && viewModel.getVarieties().getValue() != null && viewModel.getLot().getValue() != null;
    }


    public void createLot() {
        if (validateFields()) {
            showActivityOverlay();
            viewModel.createLot(viewModel.getIdentifier().get(),
                    viewModel.getLot().getValue().getStartDate(),
                    viewModel.getLot().getValue().getPlantsQuantity(),
                    viewModel.getBeaconIds().getValue(),
                    viewModel.getSelectedVarietyId().getValue());
        }
    }


    private boolean validateFields() {
        if ((viewModel.getIdentifier().get()) == null || viewModel.getIdentifier().get().isEmpty()) {
            showToast(getString(R.string.enter_a_number_to_continue)).show();
            return false;
        }
        if (viewModel.getSelectedVarietyId() == null || viewModel.getSelectedVarietyId().getValue() == null) {
            showToast(getString(R.string.select_a_variety_to_continue)).show();
            return false;
        }
        return true;
    }

    public Toast showToast(String content) {
        return Toast.makeText(this, content, Toast.LENGTH_SHORT);
    }

    private void showSelectBeacons() {
        MultipleSelectModalFragment multipleSelectModalFragment = MultipleSelectModalFragment.newInstance(getString(R.string.select_beacons), getString(R.string.add), getGetBeaconsForModal(), "");
        multipleSelectModalFragment.getBeacons().observe(this, this::onAddMultipleSelection);
        multipleSelectModalFragment.show(getSupportFragmentManager(), "multipleBeaconSelect");
    }

    @SuppressLint("RtlHardcoded")
    private void showNumberBatch() {
        InputDialogFragment inputNumber = InputDialogFragment.newInstance(getString(R.string.modal_number_lot), getString(R.string.hashtag), "", getString(R.string.add), viewModel.getIdentifier().get(),
                InputType.TYPE_CLASS_TEXT, "", this, true);
        inputNumber.show(getSupportFragmentManager(), "inputNumberModal");
    }

    private void onDismiss(Boolean aBoolean) {
        hideKeyboard();
    }

    private void showVarietyModal() {
        SingleSelectModalFragment singleSelectModalFragment = SingleSelectModalFragment.newInstance(getString(R.string.select_variety), getString(R.string.add), getVarietiesForModal());
        singleSelectModalFragment.getSelectedIndex().observe(this, this::onAddSingleSelection);
        singleSelectModalFragment.show(getSupportFragmentManager(), "singleModalSelect");
    }

    private ArrayList<Item> getVarietiesForModal() {
        return SelectorModalHelper.setSingleItemSelected(viewModel.getVarieties().getValue(), viewModel.getSelectedVarietyPosition());
    }

    private ArrayList<Item> getGetBeaconsForModal() {
        return SelectorModalHelper.setMultipleItemSelected(viewModel.getBeacons().getValue(), viewModel.getSelectedBeaconsIndex().get());
    }


    public void onAddSingleSelection(Integer varietyPosition) {
        viewModel.onAddSingleSelection(varietyPosition);
    }

    public void onAddMultipleSelection(ArrayList<Integer> beaconsPosition) {
        viewModel.onAddMultipleSelection(beaconsPosition);
    }


    public void setIdentifierNumber(String identifier) {
        viewModel.setIdentifierNumber(identifier);
    }


    private void getLotInformation() {
        viewModel.getLotInformationFromServer();
        showActivityOverlay();
    }

    public void setUpInformation(ArrayList<String> informations) {
        putFragment(new SimpleBoxInformationFragment(informations), R.id.informationContainer);
    }


    @Override
    public void onDataSet(String data, String identifier) {
        setIdentifierNumber(data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
