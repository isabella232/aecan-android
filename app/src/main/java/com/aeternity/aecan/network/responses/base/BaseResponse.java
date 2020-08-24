package com.aeternity.aecan.network.responses.base;

public class BaseResponse {
    private String message;
    private String keyword;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
