package com.reddit.redditbackend.dto;

public class AuthenticationResponse {
    private String authenticationToke;
    private String username;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String authenticationToke, String username) {
        this.authenticationToke = authenticationToke;
        this.username = username;
    }

    public String getAuthenticationToke() {
        return authenticationToke;
    }

    public void setAuthenticationToke(String authenticationToke) {
        this.authenticationToke = authenticationToke;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
