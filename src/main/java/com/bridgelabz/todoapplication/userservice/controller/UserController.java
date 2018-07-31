package com.bridgelabz.todoapplication.userservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.Utility.Messages;
import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
import com.bridgelabz.todoapplication.userservice.model.Response;
import com.bridgelabz.todoapplication.userservice.model.User;
import com.bridgelabz.todoapplication.userservice.model.UserDto;
import com.bridgelabz.todoapplication.userservice.service.UserServiceImpl;
import com.google.common.base.Preconditions;

import io.swagger.annotations.ApiOperation;

/**
 * Purpose : To control all API services.
 * 
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    11/07/2018
 */
@RestController
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	Response response;
	@Autowired
	Messages messages;
	
	
	final String REQ_IN="REQ_IN";
	final String RES_OUT="RES_OUT";
	
	/**
	 * Method is written to get all the users details from the database.
	 * 
	 * @return List<Users>
	 */
	@ApiOperation(value = "Get all user details")
	@GetMapping(value="/users")
	public List<User> getUserDetails() {
		logger.info(REQ_IN+" "+messages.get("101"));
		return userService.getUserDetails();
	}

	/**
	 * Method is written to receive the user object sent from the client side.
	 * 
	 * @param user
	 * @return ResponseEntity<User>
	 * @throws Exception
	 */
	@ApiOperation(value = "New User Sign Up")
	@PostMapping("/signup")
	public ResponseEntity<Response> signUp(@RequestBody UserDto user) throws Exception {
		logger.info(REQ_IN+messages.get("135"));
		Preconditions.checkNotNull(user,messages.get("121") );
		
		userService.signUp(user);
		
		response.setMessage(messages.get("122") + user.getEmail());
		response.setStatus(1);
		logger.info(RES_OUT+messages.get("135"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Method is written to receive the user object to get user userName and
	 * password to log in after validation. And to generate token if
	 * logged is successful.
	 * 
	 * @param user
	 * @param res
	 * @return ResponseEntity<User>
	 * @throws Exception
	 */
	@ApiOperation(value = "Resgistered User Log In")
	@PostMapping("/login")
	public ResponseEntity<Response> logIn(@RequestBody LoginDTO user, HttpServletResponse res) throws Exception {
		logger.info(REQ_IN+messages.get("133"));
		Preconditions.checkNotNull(user, messages.get("121"));
		Response response=new Response();
		String JWTtoken=userService.logIn(user);
		
		response.setMessage("Welcome "+user.getEmail());
		response.setStatus(1);
		
		res.setHeader("JWTtoken", JWTtoken);
		
		
		logger.info(messages.get("123"));
		logger.info(RES_OUT+messages.get("133"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Method is written to activate the User after user is validated as a true
	 * owner of the email by using the token.
	 * 
	 * @param req
	 * @return String
	 */
	@ApiOperation(value = "Activation Link")
	@GetMapping("/activationlink")
	public ResponseEntity<Response> activation(HttpServletRequest req) {
		logger.info(REQ_IN+messages.get("131"));
		
		userService.claimToken(req.getQueryString());
		
		response.setMessage(messages.get("124"));
		response.setStatus(200);
		
		logger.info(RES_OUT+messages.get("131"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Method is written to recover the account of the user if user forget their
	 * password by taking their email as an input.
	 * 
	 * @param user
	 * @return String
	 * @throws Exception
	 */
	@ApiOperation(value = "Recover Account")
	@PostMapping("/recoveraccount")
	public ResponseEntity<Response> passwordRecover(@RequestParam String email ) throws Exception{
		logger.info(REQ_IN+messages.get("125"));
		
		userService.passwordRecover(email);
			
		response.setMessage(messages.get("126"));
		response.setStatus(1);
		logger.info(RES_OUT+messages.get("125"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Method is written to reset the password.
	 * 
	 * @param user
	 * @param req
	 * @return ResponseEntity<Response>
	 */
	@ApiOperation(value = "Reset Password")
	@PostMapping("/resetpassword")
	public ResponseEntity<Response> resetPassword(@RequestParam String password,HttpServletRequest req){
		logger.info(REQ_IN+messages.get("127"));
		userService.resetPassword(req.getQueryString(),password);
		Response response=new Response();
		response.setMessage(messages.get("129"));
		response.setStatus(200);
		logger.info(RES_OUT+messages.get("127"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
