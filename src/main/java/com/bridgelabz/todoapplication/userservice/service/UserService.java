package com.bridgelabz.todoapplication.userservice.service;

import java.util.List;

import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
import com.bridgelabz.todoapplication.userservice.model.User;
import com.bridgelabz.todoapplication.userservice.model.UserDto;

/**
 * Purpose : Interface provided for UserService Implementaions.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 11/07/2018
 */
public interface UserService {

	/**
	 * Method is defined to get all user details present in the database.
	 * 
	 * @return List<User>
	 */
	List<User> getUserDetails();

	/**
	 * Method is written to register new User.
	 * 
	 * @param userRegistered
	 * @throws Exception
	 */
	void signUp(UserDto userRegistered) throws Exception;

	/**
	 * Method is written to allow the user to use the user service if only they are
	 * registered.
	 * 
	 * @param userloggedIn
	 * @return
	 * @throws Exception
	 */
	String logIn(LoginDTO userloggedIn) throws Exception;

	/**
	 * Method is written to activate user account. As soon as user clicked the link
	 * sent to them their status in the database will be changed to true.
	 * 
	 * @param claimedToken
	 */
	void claimToken(String claimedToken);

	/**
	 * Method is written to recover the password of the user in case if they forget
	 * their password. Password will be sent to the user email.
	 * 
	 * @param email
	 * @throws Exception
	 */
	void passwordRecover(String email) throws Exception;

	/**
	 * Method is written to reset the password of the user.
	 * 
	 * @param claimedToken
	 * @param password
	 */
	void resetPassword(String claimedToken, String password);
}