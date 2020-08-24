package com.aeternity.aecan.network.responses.errors;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResponse {
    private String error;
    private String error_description;
    private String message;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(JSONObject object) {
        try {
            if (object.has("error_description"))
                this.setError_description(String.valueOf(object.get("error_description")));
            if (object.has("error"))
                this.setError(String.valueOf(object.get("error")));
            if (object.has("message"))
                this.setMessage(String.valueOf(object.get("message")));

        } catch (JSONException e) {
            e.printStackTrace();
            this.error_description = (e.getMessage());
            this.error = ("JSONException");
        }
    }

    private boolean checkNotEmptyOrNull(String text) {
        return text != null && !text.isEmpty();
    }

    private String voidIfNull(String text) {
        if (checkNotEmptyOrNull(text))
            return text;
        else return "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }


    public String getErrorDescription() {
        return error_description;
    }

    public String getFormattedMessage() {
        if (message == null && error_description == null)
            return error;

        if (checkNotEmptyOrNull(error))
            return String.format("%s %s", voidIfNull(message), voidIfNull(error_description));
        else
            return String.format("%s %s", voidIfNull(message), voidIfNull(error_description));

    }

    public void setError(String error) {
        this.error = error;
    }


    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", error_description='" + error_description + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
