package com.finance.personal_finance_tracker.payload;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";

    public AuthResponse(String token) { this.token = token; }
    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
}
