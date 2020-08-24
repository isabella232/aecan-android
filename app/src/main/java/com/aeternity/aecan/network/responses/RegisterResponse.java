package com.aeternity.aecan.network.responses;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String password;
    private String message;

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }
}
