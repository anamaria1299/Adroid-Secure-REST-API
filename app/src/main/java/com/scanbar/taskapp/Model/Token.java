package com.scanbar.taskapp.Model;

public class Token {

    private String accessToken;

    public Token(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
