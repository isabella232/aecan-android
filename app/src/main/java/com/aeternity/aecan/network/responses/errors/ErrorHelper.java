package com.aeternity.aecan.network.responses.errors;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import retrofit2.HttpException;

public class ErrorHelper {
    public static ErrorResponse getError(Throwable e) {
        Log.d("ERROR", e.toString());
        if (e instanceof HttpException) {
            try {
                String errorResponse = ((HttpException) e).response().errorBody().string();
                JSONObject object = new JSONObject(errorResponse);
                return new ErrorResponse(object);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
                return new ErrorResponse(ex.getMessage());
            }
        } else {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }

    public static ErrorResponse getError(Response response) {

        try {
            String errorResponse = response.body().string();
            JSONObject object = new JSONObject(errorResponse);
            return new ErrorResponse(object);
        } catch (IOException | JSONException | IllegalStateException ex) {
            ex.printStackTrace();
            return new ErrorResponse(response.message());
        }

    }
}