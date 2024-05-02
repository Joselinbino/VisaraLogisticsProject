package com.visara.transportationweb.Model;

public class LoginResponse {
	private boolean success;
	private String userType;

	public LoginResponse(boolean success, String userType) {
		this.success = success;
		this.userType = userType;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getUserType() {
		return userType;
	}

	// Setter method for userType if necessary
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
