package com.bridgelabz.todoapplication.userservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
import com.bridgelabz.todoapplication.userservice.model.RegistrationDTO;
import com.bridgelabz.todoapplication.userservice.model.ResetPasswordDto;
import com.bridgelabz.todoapplication.userservice.model.Response;
import com.bridgelabz.todoapplication.userservice.model.User;
import com.bridgelabz.todoapplication.userservice.service.UserServiceImpl;

import io.swagger.annotations.ApiOperation;

/**
 * Purpose : To control all API services.
 * 
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    11/07/2018
 */
@RestController
public class userController {

	@Autowired
	private UserServiceImpl userService;

	/**
	 * Method is written to get all the users details from the database.
	 * 
	 * @return List<Users>
	 */
	@ApiOperation(value = "Get all user details")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUserDetails() {
		return userService.getUserDetails();
	}

	/**
	 * Method is written to receive the user object sent from the postman.
	 * 
	 * @param user
	 * @return ResponseEntity<User>
	 * @throws Exception
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Response> signUp(@RequestBody RegistrationDTO user, HttpServletResponse res) throws Exception {
		userService.signUp(user);
		Response response=new Response();
		response.setMessage("Mail sent to the user with email " + user.getEmail());
		response.setStatus(200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Method is written to receive the user object to get user userName and
	 * password to log in after validation. An to generate token if successful
	 * logged in.
	 * 
	 * @param user
	 * @param res
	 * @return ResponseEntity<User>
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Response> logIn(@RequestBody LoginDTO user, HttpServletResponse res) throws Exception {
		userService.logIn(user);
		Response response=new Response();
		response.setMessage("Welcome "+user.getEmail());
		response.setStatus(200);
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	/**
	 * Method is written to activate the User after user is validated as a true
	 * owner of the email by using the token.
	 * 
	 * @param req
	 * @return String
	 */
	@RequestMapping(value = "/activationlink", method = RequestMethod.GET)
	public ResponseEntity<Response> activation(HttpServletRequest req) {
		userService.claimToken(req.getQueryString());
		Response response=new Response();
		response.setMessage("Your account is successfully activated");
		response.setStatus(200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Method is written to recover the account of the user if user forget their
	 * password by taking their email as an input
	 * 
	 * @param user
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/recoveraccount", method = RequestMethod.POST)
	public ResponseEntity<Response> passwordRecover(@RequestParam String email ) throws Exception {
		userService.passwordRecover(email);
		Response response=new Response();
		response.setMessage("Link is sent to your mail.. ");
		response.setStatus(200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/resetpassword",method=RequestMethod.POST)
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto user,HttpServletRequest req){
		System.out.println(req.getQueryString());
		userService.resetPassword(req.getQueryString(),user.getPassword());
		Response response=new Response();
		response.setMessage("Your Password Is Updated ");
		response.setStatus(200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
