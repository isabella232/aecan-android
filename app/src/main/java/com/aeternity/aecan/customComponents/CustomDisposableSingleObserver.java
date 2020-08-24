package com.aeternity.aecan.customComponents;

import com.aeternity.aecan.network.responses.errors.ErrorHelper;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;

import io.reactivex.observers.DisposableSingleObserver;

public abstract class CustomDisposableSingleObserver<T> extends DisposableSingleObserver<T> {

    public abstract void onError(ErrorResponse errorResponse);

    @Override
    public void onError(Throwable e) {
        onError(ErrorHelper.getError(e));
    }


}
