package com.bridgelabz.todoapplication.userservice.model;

public class ResetPasswordDto {
	String password;

	public ResetPasswordDto() {
		super();
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ResetPasswordDto [password=" + password + "]";
	}
	
}
