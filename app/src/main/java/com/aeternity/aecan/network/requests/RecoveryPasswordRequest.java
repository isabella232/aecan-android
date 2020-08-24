package com.aeternity.aecan.network.requests;

public class RecoveryPasswordRequest {
    private String email;

    public RecoveryPasswordRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
