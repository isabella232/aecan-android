package com.aeternity.aecan.viewModels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.helpers.SelectorModalHelper;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.network.repositories.BeaconsRepository;
import com.aeternity.aecan.network.repositories.LotRepository;
import com.aeternity.aecan.network.repositories.VarietiesRepository;
import com.aeternity.aecan.network.requests.CreateLotRequest;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import java.util.ArrayList;

public class NewLotViewModel extends BaseViewModel {

    private LotRepository lotRepository;
    private VarietiesRepository varietiesRepository;
    private BeaconsRepository beaconsRepository;
    private MutableLiveData<Integer> selectedVarietyId = new MutableLiveData<>();
    private Integer selectedVarietyPosition = -1;
    private ObservableField<ArrayList<Integer>> selectedBeaconsIndex = new ObservableField<>(new ArrayList<>());
    private MutableLiveData<ArrayList<Integer>> beaconIds = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<ArrayList<Item>> beacons = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<ArrayList<Item>> varieties = new MutableLiveData<>(new ArrayList<>());
    private ObservableField<String> identifier = new ObservableField<>();
    private MutableLiveData<Lot> lot = new MutableLiveData<>();
    private ObservableField<String> varietyName = new ObservableField<>("-");

    public void onAddSingleSelection(Integer varietyPosition) {
        selectedVarietyPosition = varietyPosition;
        if (varietyPosition == -1) {
            resetVarietySelection();
        } else {
            Item variety = SelectorModalHelper.getSingleItemSelected(getVarieties().getValue(), varietyPosition);
            selectedVarietyId.setValue(variety.getId());
            varietyName.set(variety.getText());
        }
    }

    public ObservableField<String> getVarietyName() {
        return varietyName;
    }

    public void resetVarietySelection() {
        selectedVarietyPosition = -1;
        varietyName.set("-");
        selectedVarietyId.setValue(null);
    }

    public void onAddMultipleSelection(ArrayList<Integer> beaconsPosition) {
        setBeaconsId(beaconsPosition);
    }

    public enum ViewModelAction {
        BATCH_NUMBER_PRESSED,
        BATCH_BEACONS_PRESSED,
        BATCH_VARIETY_PRESSED,
        SAVE_BUTTON_PRESSED,
        HIDE_OVERLAY,
        SHOW_OVERLAY
    }

    public void resetView() {
        resetVarietySelection();
        setBeaconsId(new ArrayList<>());
        setIdentifierNumber("");
    }

    public void setBeaconsId(ArrayList<Integer> beaconsPosition) {
        getBeaconIds().setValue(SelectorModalHelper.getItemsIds(getBeacons().getValue(), beaconsPosition));
        getSelectedBeaconsIndex().set(beaconsPosition);
    }

    private SingleLiveEvent<ViewModelAction> buttonAction = new SingleLiveEvent<>();
    private SingleLiveEvent<ErrorResponse> onGenericNetworkFail = new SingleLiveEvent<>();
    private SingleLiveEvent<LotRepository.OnCreateLotSuccess> onCreateLotSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<LotRepository.OnGetLotInformationSuccess> onGetLotInformationSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<BeaconsRepository.OnGetBeaconsSuccess> onBeaconsSuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<VarietiesRepository.OnGetVarietiesSuccess> onGetVarietiesSuccessData = new SingleLiveEvent<>();


    public MutableLiveData<ArrayList<Item>> getBeacons() {
        return beacons;
    }

    public MutableLiveData<ArrayList<Item>> getVarieties() {
        return varieties;
    }

    public MutableLiveData<Integer> getSelectedVarietyId() {
        return selectedVarietyId;
    }

    public Integer getSelectedVarietyPosition() {
        return selectedVarietyPosition;
    }

    public ObservableField<ArrayList<Integer>> getSelectedBeaconsIndex() {
        return selectedBeaconsIndex;
    }

    public String getSlectedBeaconsIndexAsString() {
        return String.valueOf(getSelectedBeaconsIndex().get().size());
    }

    public MutableLiveData<ArrayList<Integer>> getBeaconIds() {
        return beaconIds;
    }

    public ObservableField<String> getIdentifier() {
        return identifier;
    }

    public SingleLiveEvent<BeaconsRepository.OnGetBeaconsSuccess> getOnBeaconsSuccessData() {
        return onBeaconsSuccessData;
    }

    public SingleLiveEvent<LotRepository.OnCreateLotSuccess> getOnCreateLotSuccessData() {
        return onCreateLotSuccessData;
    }

    public SingleLiveEvent<VarietiesRepository.OnGetVarietiesSuccess> getOnGetVarietiesSuccessData() {
        return onGetVarietiesSuccessData;
    }

    public SingleLiveEvent<LotRepository.OnGetLotInformationSuccess> getOnGetLotInformationSuccessData() {
        return onGetLotInformationSuccessData;
    }

    public void onSaveButtonPressed() {
        buttonAction.setValue(ViewModelAction.SAVE_BUTTON_PRESSED);
    }

    public void onBatchNumberPressed() {
        buttonAction.setValue(ViewModelAction.BATCH_NUMBER_PRESSED);
    }

    public void onBatchBeaconsPressed() {
        buttonAction.setValue(ViewModelAction.BATCH_BEACONS_PRESSED);
    }

    public void onBatchVarietyPressed() {
        buttonAction.setValue(ViewModelAction.BATCH_VARIETY_PRESSED);
    }


    public void createLot(String identifier, String startDate, Integer plantsQuantity, ArrayList<Integer> beaconIds, Integer varietyId) {
        CreateLotRequest createLotRequest = new CreateLotRequest();
        createLotRequest.setIdentifier(identifier);
        createLotRequest.setBeaconIds(beaconIds);
        createLotRequest.setPlantsQuantity(plantsQuantity);
        createLotRequest.setStartDate(startDate);
        createLotRequest.setVarietyId(varietyId);
        getLotRepository().createLot(createLotRequest);
    }

    private BeaconsRepository getBeaconsRepository() {
        if (beaconsRepository == null) {
            beaconsRepository = new BeaconsRepository();
            observerGetBeaconsSuccess();
            observeBeaconsRepositoryFail();
        }
        return beaconsRepository;
    }

    private LotRepository getLotRepository() {
        if (lotRepository == null) {
            lotRepository = new LotRepository();
            observerCreateLotRepositorySuccess();
            observeLotRepositoryFail();
            observerGetLotInformationRepositorySuccess();
        }
        return lotRepository;
    }

    private VarietiesRepository getVarietiesRepository() {
        if (varietiesRepository == null) {
            varietiesRepository = new VarietiesRepository();
            observerGetVarietiesSuccess();
            observeVarietiesRepositoryFail();
        }
        return varietiesRepository;
    }

    private void observeBeaconsRepositoryFail() {
        getDisposables().add(getBeaconsRepository().getOnGetBeaconsFail().subscribe(
                error -> onGenericNetworkFail.postValue(error.getErrorResponse())));
    }

    private void observeVarietiesRepositoryFail() {
        getDisposables().add(getVarietiesRepository().getOnGetVarietiesFail().subscribe(
                error -> onGenericNetworkFail.postValue(error.getErrorResponse())));

    }

    private void observeLotRepositoryFail() {
        getDisposables().add(getLotRepository().getOnCreateLotFail().subscribe(
                error -> onGenericNetworkFail.postValue(error.getErrorResponse())));

        getDisposables().add(getLotRepository().getOnGetLotsFail().subscribe(
                error -> onGenericNetworkFail.postValue(error.getErrorResponse())));

        getDisposables().add(getLotRepository().getOnGetLotInformationFail().subscribe(
                error -> onGenericNetworkFail.postValue(error.getErrorResponse())));

    }

    private void observerCreateLotRepositorySuccess() {
        getDisposables().add(getLotRepository().getOnCreateLotSuccess().subscribe(
                onCreateLotSuccess -> {
                    getOnCreateLotSuccessData().setValue(onCreateLotSuccess);
                    resetView();
                }));
    }

    private void observerGetLotInformationRepositorySuccess() {
        getDisposables().add(getLotRepository().getOnGetLotInformationSuccess().subscribe(
                onGetLotInformationSuccess -> {
                    getLot().setValue(onGetLotInformationSuccess.getGetLotInformation().getLot());
                    getBeaconsFromServer();
                }));
    }

    private void observerGetBeaconsSuccess() {
        getDisposables().add(getBeaconsRepository().getOnGetBeaconsSuccess().subscribe(
                onGetBeaconsSuccess -> {
                    getBeacons().setValue(new ArrayList<>(onGetBeaconsSuccess.getBeaconsResponse().getBeacons()));
                    getVarietiesFromServer();
                }));
    }

    private void observerGetVarietiesSuccess() {
        getDisposables().add(getVarietiesRepository().getOnGetVarietiesSuccess().subscribe(
                onVarietiesRepositorySuccess -> getOnGetVarietiesSuccessData().setValue(onVarietiesRepositorySuccess)));
        getDisposables().add(getVarietiesRepository().getOnGetVarietiesSuccess().subscribe(onGetVarietiesSuccess -> {
            getVarieties().setValue(new ArrayList<>(onGetVarietiesSuccess.getVarietiesResponse().getVarieties()));

        }));
    }

    public void getVarietiesFromServer() {
        getVarietiesRepository().getVarieties();
    }

    public void getBeaconsFromServer() {
        getBeaconsRepository().getBeacons();
    }

    public MutableLiveData<Lot> getLot() {
        return lot;
    }

    public void getLotInformationFromServer() {
        getLotRepository().getLotInformation();
    }

    public SingleLiveEvent<ViewModelAction> getButtonAction() {
        return buttonAction;
    }

    public SingleLiveEvent<ErrorResponse> getOnGenericNetworkFail() {
        return onGenericNetworkFail;
    }

    public void setIdentifierNumber(String identifier) {
        getIdentifier().set(identifier);
    }
}
