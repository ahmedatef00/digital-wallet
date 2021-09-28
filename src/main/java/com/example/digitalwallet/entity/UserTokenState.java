package com.example.digitalwallet.entity;

public class UserTokenState {


    private String access_token;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


    public UserTokenState(String access_token, int expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }


    public UserTokenState() {
    }


}
