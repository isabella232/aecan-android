package com.aeternity.aecan.network.requests;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest {

    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("grant_type")
    private String grantType;

    public RefreshRequest(String refreshToken, String grantType) {
        this.refreshToken = refreshToken;
        this.grantType = grantType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }
}
