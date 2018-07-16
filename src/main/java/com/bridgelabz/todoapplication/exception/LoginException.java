package com.bridgelabz.todoapplication.exception;

/**
 * Purpose : To throw the exception if some error occurs during user Log In.
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    14/07/2018
 */
public class LoginException extends Exception{

	

	private static final long serialVersionUID = 1L;

	public LoginException(String message) {
		super(message);
	}
	
}
