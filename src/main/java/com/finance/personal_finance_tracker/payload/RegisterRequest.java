package com.finance.personal_finance_tracker.payload;

public class RegisterRequest {
    private String username;
    private String password;
    // optionally roles
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
