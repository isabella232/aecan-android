package com.aeternity.aecan.network.responses.errors;

public class ErrorWrapper {

    private ErrorResponse errorResponse;

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public ErrorWrapper(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

}
