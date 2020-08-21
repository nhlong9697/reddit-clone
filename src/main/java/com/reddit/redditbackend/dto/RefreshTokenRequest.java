package com.reddit.redditbackend.dto;

import javax.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
    private String username;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(@NotBlank String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
    }
}
