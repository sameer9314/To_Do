package com.bridgelabz.todoapplication.userservice.model;

/**
 * Purpose : POJO class for LoginDTO	
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    20/07/2018
 */
public class LoginDTO {

	private String email;
	private String password;
	
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
