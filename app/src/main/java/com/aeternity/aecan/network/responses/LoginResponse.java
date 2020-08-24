package com.aeternity.aecan.network.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken = "";

    @SerializedName("refresh_token")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccesTokenWBearer() {
        return "Bearer " + accessToken;

    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean checkIfIsSignedIn() {
        if (getAccessToken() == null || getAccessToken().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
