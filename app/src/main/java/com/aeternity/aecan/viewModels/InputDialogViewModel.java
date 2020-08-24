package com.aeternity.aecan.viewModels;

import android.os.Bundle;
import android.view.View;

import com.aeternity.aecan.network.repositories.GenericPostRepository;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.base.BaseViewModel;

import static com.aeternity.aecan.util.EditTextValidationUtils.validateNonEmpty;
import static com.aeternity.aecan.views.modal.InputDialogFragment.BUTTON_TEXT_KEY;
import static com.aeternity.aecan.views.modal.InputDialogFragment.INPUT_TYPE_KEY;
import static com.aeternity.aecan.views.modal.InputDialogFragment.PREFIX_KEY;
import static com.aeternity.aecan.views.modal.InputDialogFragment.SUFFIX_KEY;
import static com.aeternity.aecan.views.modal.InputDialogFragment.TITLE_KEY;
import static com.aeternity.aecan.views.modal.InputDialogFragment.URL_POST_KEY;
import static com.aeternity.aecan.views.modal.InputDialogFragment.USER_INPUT_KEY;

public class InputDialogViewModel extends BaseViewModel {

    private GenericPostRepository repository;
    private SingleLiveEvent<ErrorResponse> networkError = new SingleLiveEvent<>();

    public enum ViewModelAction {
        BUTTON_PRESSED,
        REQUEST_SUCCESS
    }

    private String title;
    private String prefix;
    private String suffix;
    private String buttonText;
    private String userInput;

    private String url;

    private int inputType;
    private int textAlignment = View.TEXT_ALIGNMENT_TEXT_START;

    public void setParams(Bundle params) {
        this.title = params.getString(TITLE_KEY);
        this.prefix = params.getString(PREFIX_KEY);
        this.prefix = params.getString(PREFIX_KEY);
        this.suffix = params.getString(SUFFIX_KEY);
        this.buttonText = params.getString(BUTTON_TEXT_KEY);
        this.userInput = params.getString(USER_INPUT_KEY);
        this.inputType = params.getInt(INPUT_TYPE_KEY);
        this.url = params.getString(URL_POST_KEY);

        if (prefix != null && !prefix.isEmpty()) {
            if (suffix != null && !suffix.isEmpty()) {
                textAlignment = View.TEXT_ALIGNMENT_CENTER;
            } else {
                textAlignment = View.TEXT_ALIGNMENT_TEXT_START;
            }
        } else {
            textAlignment = View.TEXT_ALIGNMENT_TEXT_END;
        }
    }


    public SingleLiveEvent<ViewModelAction> getAction() {
        return action;
    }

    private SingleLiveEvent<ViewModelAction> action = new SingleLiveEvent<>();

    public void onButtonPressed() {
        action.setValue(ViewModelAction.BUTTON_PRESSED);
    }

    public boolean hasRepository() {
        return validateNonEmpty(url);
    }

    public void makePost(String value) {
        if (validateNonEmpty(url) && validateNonEmpty(value)) {
            getRepository().makePost(url, value);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getInputType() {
        return inputType;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getUserInput() {
        return userInput;
    }

    public int getTextAlignment() {
        return textAlignment;
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

    public SingleLiveEvent<ErrorResponse> getNetworkError() {
        return networkError;
    }

    public void onFrameTouched() {
        System.out.println("Frame Touch");
    }
}
