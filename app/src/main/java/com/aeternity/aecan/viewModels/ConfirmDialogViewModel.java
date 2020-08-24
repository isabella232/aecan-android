package com.aeternity.aecan.viewModels;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.network.repositories.GenericPostRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import static com.aeternity.aecan.util.EditTextValidationUtils.validateNonEmpty;
import static com.aeternity.aecan.views.modal.ConfirmDialogFragment.BODY_KEY;
import static com.aeternity.aecan.views.modal.ConfirmDialogFragment.POSITIVE_BUTTON_TEXT;
import static com.aeternity.aecan.views.modal.ConfirmDialogFragment.TITLE_KEY;
import static com.aeternity.aecan.views.modal.ConfirmDialogFragment.URL_POST_KEY;

public class ConfirmDialogViewModel extends BaseViewModel implements ConfirmationDialogInterface {

    private GenericPostRepository repository;
    private SingleLiveEvent<ErrorResponse> networkError = new SingleLiveEvent<>();

    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> body = new MutableLiveData<>();
    private MutableLiveData<String> positiveButtonText = new MutableLiveData<>();
    private String url;

    @Override
    public MutableLiveData<String> getTitle() {
        return title;
    }

    @Override
    public MutableLiveData<String> getBody() {
        return body;
    }

    @Override
    public MutableLiveData<String> getPositiveButton() {
        return positiveButtonText;
    }

    @Override
    public void onAcceptPressed() {
        makePost();
    }

    @Override
    public void onCancelPressed() {
        action.setValue(ViewModelAction.CANCEL_BUTTON_PRESSED);
    }

    public enum ViewModelAction {
        ACCEPT_BUTTON_PRESSED,
        CANCEL_BUTTON_PRESSED,
        REQUEST_SUCCESS
    }

    public void setParams(Bundle params) {
        this.url = params.getString(URL_POST_KEY);
        this.title.setValue(params.getString(TITLE_KEY));
        this.body.setValue(params.getString(BODY_KEY));
        this.positiveButtonText.setValue(params.getString(POSITIVE_BUTTON_TEXT));
    }

    public SingleLiveEvent<ViewModelAction> getAction() {
        return action;
    }

    private SingleLiveEvent<ViewModelAction> action = new SingleLiveEvent<>();

    public void makePost() {
        if (validateNonEmpty(url)) {
            action.setValue(ViewModelAction.ACCEPT_BUTTON_PRESSED);
            getRepository().makePost(url, "true");
        }
    }

    private GenericPostRepository getRepository() {
        if (repository != null)
            return repository;
        else {
            this.repository = new GenericPostRepository();
            getDisposables().add(repository.getOnGenericSuccessResponse().subscribe(object
                    -> action.postValue(ViewModelAction.REQUEST_SUCCESS)));

            getDisposables().add(repository.getOnGenericFailureResponse().subscribe(object
                    -> networkError.postValue(object.getErrorResponse())));
        }

        return repository;
    }

    public SingleLiveEvent<ErrorResponse> getNetworkError() {
        return networkError;
    }
}
