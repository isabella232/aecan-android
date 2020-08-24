package com.aeternity.aecan.viewModels;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.aeternity.aecan.network.repositories.GenericPostRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import static com.aeternity.aecan.util.EditTextValidationUtils.validateNonEmpty;
import static com.aeternity.aecan.views.modal.DatePickerDialogFragment.BUTTON_TEXT_KEY;
import static com.aeternity.aecan.views.modal.DatePickerDialogFragment.TITLE_KEY;
import static com.aeternity.aecan.views.modal.DatePickerDialogFragment.URL_POST_KEY;
import static com.aeternity.aecan.views.modal.DatePickerDialogFragment.USER_INPUT_KEY;

public class DatePickerDialogViewModel extends BaseViewModel {

    private GenericPostRepository repository;
    private SingleLiveEvent<ErrorResponse> networkError = new SingleLiveEvent<>();

    public enum ViewModelAction {
        BUTTON_PRESSED,
        REQUEST_SUCCESS
    }

    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> buttonText = new MutableLiveData<>();
    private String userInput;

    private String url;

    public void setParams(Bundle params) {
        this.title.setValue(params.getString(TITLE_KEY));
        this.buttonText.setValue(params.getString(BUTTON_TEXT_KEY));
        this.userInput = params.getString(USER_INPUT_KEY);

        this.url = params.getString(URL_POST_KEY);
    }


    public SingleLiveEvent<ViewModelAction> getAction() {
        return action;
    }

    private SingleLiveEvent<ViewModelAction> action = new SingleLiveEvent<>();

    public void onButtonPressed() {
        action.setValue(ViewModelAction.BUTTON_PRESSED);
        if(hasRepository()){
            if (validateNonEmpty(url) && validateNonEmpty(userInput)) {
                getRepository().makePost(url, userInput);
            }
        }

    }

    public boolean hasRepository() {
        return validateNonEmpty(url);
    }

    public String getTitle() {
        return title.getValue();
    }


    public String getButtonText() {
        return buttonText.getValue();
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    private GenericPostRepository getRepository() {
        if (repository != null)
            return repository;
        else {
            this.repository = new GenericPostRepository();
            getDisposables().add(repository.getOnGenericSuccessResponse().subscribe(object -> {
                action.postValue(ViewModelAction.REQUEST_SUCCESS);

            }));

            getDisposables().add(repository.getOnGenericFailureResponse().subscribe(object -> {

                networkError.postValue(object.getErrorResponse());

            }));
        }

        return repository;
    }

    public String getUserInput() {
        return userInput;
    }

    public SingleLiveEvent<ErrorResponse> getNetworkError() {
        return networkError;
    }
}
