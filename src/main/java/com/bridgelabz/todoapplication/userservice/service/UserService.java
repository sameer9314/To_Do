package com.bridgelabz.todoapplication.userservice.service;

import java.util.List;

import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
import com.bridgelabz.todoapplication.userservice.model.User;


/**
 * Purpose : Interface provided for UserService Implementaions.	
 * @author Sameer Saurabh
 * @version  1.0
 * @Since  11/07/2018
 */
public interface UserService {
	
	/**
	 * Method is defined to get all user present in the database.
	 * @return List<User>
	 */
	List<User> getUserDetails();
	
	void signUp(User userRegistered) throws Exception;
	
	String logIn(LoginDTO userloggedIn) throws Exception;

	void claimToken(String claimedToken);
	
	void passwordRecover(String email) throws Exception;
	
	void resetPassword(String claimedToken, String password);
}