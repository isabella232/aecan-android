package com.aeternity.aecan.viewModels.base;

import androidx.lifecycle.ViewModel;

import com.aeternity.aecan.persistence.SessionPersistence;
import com.aeternity.aecan.util.Constants;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {
    private ArrayList<Disposable> disposables = new ArrayList<>();

    protected ArrayList<Disposable> getDisposables() {
        return disposables;
    }

    public boolean isUserSignedIn() {
        return SessionPersistence.newOrReadFromPaper().checkIfIsSignedIn();
    }

    public boolean isOperator() {
        return Constants.isOperator();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        for (Disposable disposable : disposables) {
            if (disposable != null) {
                disposable.dispose();
            }
        }
    }
}
