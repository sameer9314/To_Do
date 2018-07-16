package com.bridgelabz.todoapplication.exception;

/**
 * Purpose : To throw exception if some error occurs during User registration.	
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    14/07/2018
 */
public class RegistrationException extends Exception {


	private static final long serialVersionUID = 1L;

	public RegistrationException(String message) {
		super(message);
	}
	
}
