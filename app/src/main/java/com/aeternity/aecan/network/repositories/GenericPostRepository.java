package com.aeternity.aecan.network.repositories;

import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.network.responses.errors.ErrorHelper;
import com.aeternity.aecan.network.responses.errors.ErrorResponse;
import com.aeternity.aecan.network.responses.errors.ErrorWrapper;
import com.aeternity.aecan.persistence.SessionPersistence;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.reactivex.subjects.PublishSubject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GenericPostRepository {

    private PublishSubject<GenericResponse> onGenericSuccessResponse = PublishSubject.create();
    private PublishSubject<GenericError> onGenericFailureResponse = PublishSubject.create();


    public void makePost(String url, String value) {
        RestClient.genericPost(url, value, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onGenericFailureResponse.onNext(new GenericError(ErrorHelper.getError(e)));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    onGenericSuccessResponse.onNext(new GenericResponse(response));
                    SessionPersistence.notifySuccessObserver();
                } else {
                    ErrorResponse error = ErrorHelper.getError(response);
                    SessionPersistence.notifyFailObserver(error.getMessage());
                    onGenericFailureResponse.onNext(new GenericError(error));
                }

            }
        });
    }


    public PublishSubject<GenericResponse> getOnGenericSuccessResponse() {
        return onGenericSuccessResponse;
    }

    public class GenericError extends ErrorWrapper {
        public GenericError(ErrorResponse errorResponse) {
            super(errorResponse);
        }
    }

    public class GenericResponse {
        Response response;

        public Response getResponse() {
            return response;
        }

        GenericResponse(Response response) {
            this.response = response;
        }
    }

    public PublishSubject<GenericError> getOnGenericFailureResponse() {
        return onGenericFailureResponse;
    }
}
