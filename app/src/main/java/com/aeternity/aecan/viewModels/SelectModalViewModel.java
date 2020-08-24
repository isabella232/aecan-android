package com.aeternity.aecan.viewModels;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.network.repositories.GenericPostRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import java.util.ArrayList;

import static com.aeternity.aecan.viewModels.SelectModalViewModel.ViewModelAction.*;
import static com.aeternity.aecan.views.modal.SelectDialogFragment.*;

public class SelectModalViewModel extends BaseViewModel {
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> buttonText = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();

    private GenericPostRepository repository;
    private SingleLiveEvent<ErrorResponse> networkError = new SingleLiveEvent<>();

    private String url = "";


    public void setArguments(String title, String buttonText, ArrayList<Item> items, String url) {
        this.title.setValue(title);
        this.buttonText.setValue(buttonText);
        this.items.setValue(items);
        this.url = url;
    }

    public void setArguments(Bundle bundle) {
        this.title.setValue(bundle.getString(Parameters.TITLE_KEY.getKey()));
        this.buttonText.setValue(bundle.getString(Parameters.BUTTON_TEXT_KEY.getKey()));
        this.items.setValue((ArrayList<Item>) bundle.getSerializable(Parameters.ITEMS_KEY.getKey()));
        this.url = (bundle.getString(Parameters.URL_POST_KEY.getKey()));
    }

    public enum ViewModelAction {
        DISMISS_PRESSED,
        BUTTON_PRESSED,
        REQUEST_SUCCESS
    }

    public void sendSelected(String id) {
        if (url != null && !url.isEmpty())
            getRepository().makePost(url, id);
    }


    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getButtonText() {
        return buttonText;
    }

    public MutableLiveData<ArrayList<Item>> getItems() {
        return items;
    }

    private SingleLiveEvent<ViewModelAction> action = new SingleLiveEvent<>();

    public SingleLiveEvent<ViewModelAction> getAction() {
        return action;
    }

    public void onDismiss() {
        action.setValue(DISMISS_PRESSED);
    }

    public void onButtonPressed() {
        action.setValue(BUTTON_PRESSED);
    }

    private GenericPostRepository getRepository() {
        if (repository != null)
            return repository;
        else {
            this.repository = new GenericPostRepository();
            getDisposables().add(repository.getOnGenericSuccessResponse().subscribe(
                    object -> action.postValue(REQUEST_SUCCESS)));

            getDisposables().add(repository.getOnGenericFailureResponse().subscribe(
                    object -> networkError.postValue(object.getErrorResponse())));
        }

        return repository;
    }

    public SingleLiveEvent<ErrorResponse> getNetworkError() {
        return networkError;
    }


}
