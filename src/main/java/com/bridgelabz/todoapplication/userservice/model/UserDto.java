package com.bridgelabz.todoapplication.userservice.model;

/**
 * Purpose : POJO class of UserDTO.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 20/07/2018
 */
public class UserDto {

	String userName;
	String email;
	String phoneNumber;
	String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserDto [userName=" + userName + ", email=" + email + ", phoneNumber=" + phoneNumber + ", password="
				+ password + "]";
	}

}
