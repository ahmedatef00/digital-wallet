package com.example.digitalwallet.dto.security;

import com.example.digitalwallet.validation.annotation.NullOrNotBlank;

public class JwtAuthnticationRequest {
    @NullOrNotBlank(message = "Login Email can be null but not blank")
    private String email;
    @NullOrNotBlank(message = "Login Password can  not be blank")

    private String password;


    public JwtAuthnticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public JwtAuthnticationRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
