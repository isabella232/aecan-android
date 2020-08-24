package com.aeternity.aecan.viewModels;


import com.aeternity.aecan.network.repositories.SearchLotRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

public class SearchViewModel extends BaseViewModel {
    private SearchLotRepository searchLotRepository;
    private SingleLiveEvent<ViewModelAction> action = new SingleLiveEvent<>();
    private SingleLiveEvent<SearchLotRepository.OnSearchLotSuccess> onSearchLotRepositorySuccessData = new SingleLiveEvent<>();
    private SingleLiveEvent<ErrorResponse> onSearchLotRepositoryFailData = new SingleLiveEvent<>();

    public enum ViewModelAction {
        SEARCH_PRESSED
    }

    private SearchLotRepository getSearchLotRepository() {
        if (searchLotRepository == null) {
            searchLotRepository = new SearchLotRepository();
            observerSearchRepositorySuccess();
            observerSearchRepositoryFail();
        }
        return searchLotRepository;
    }


    private void observerSearchRepositorySuccess() {
        getDisposables().add(getSearchLotRepository().getOnSearchLotSuccess().subscribe(onSearchLotSuccess ->
                onSearchLotRepositorySuccessData.setValue(onSearchLotSuccess)));
    }

    private void observerSearchRepositoryFail() {
        getDisposables().add(getSearchLotRepository().getOnSearchLotFail().subscribe(error ->
                onSearchLotRepositoryFailData.setValue(error.getErrorResponse())));

    }

    public SingleLiveEvent<ViewModelAction> getAction() {
        return action;
    }

    public SingleLiveEvent<ErrorResponse> getOnSearchLotRepositoryFailData() {
        return onSearchLotRepositoryFailData;
    }

    public SingleLiveEvent<SearchLotRepository.OnSearchLotSuccess> getOnSearchLotRepositorySuccessData() {
        return onSearchLotRepositorySuccessData;
    }

    public void onSearchButtonPressed() {
        getAction().setValue(ViewModelAction.SEARCH_PRESSED);
    }

    public void searchLot(String identifier) {
        getSearchLotRepository().searchLot(identifier);
    }
}
