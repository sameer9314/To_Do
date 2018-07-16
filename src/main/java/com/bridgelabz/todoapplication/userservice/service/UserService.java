package com.bridgelabz.todoapplication.userservice.service;

import java.util.List;

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
	
	/**
	 * Method is written to check whether user with entered userName is present
	 * in the database . If present return true or if not return false. 
	 * @param userName
	 * @return boolean
	 */
	boolean checkUserName(String userName);
	
	/**
	 * Method is written to validate the user credentials.
	 * If userName and password matched return true if not return false.
	 * @param userName
	 * @param password
	 * @return boolean.
	 */
	boolean validateUser(String userName,String password);
	
	/**
	 * Method is written to save the user to the database
	 * If user is not already present.
	 * @param user
	 */
	void updateUser(User user);
}
