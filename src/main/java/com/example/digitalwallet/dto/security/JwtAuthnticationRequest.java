package com.example.digitalwallet.dto.security;

public class JwtAuthnticationRequest {
    private String username;
    private String password;


    public JwtAuthnticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JwtAuthnticationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
