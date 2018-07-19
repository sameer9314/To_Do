package com.bridgelabz.todoapplication.userservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
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
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	final String REQ_IN="REQ_IN";
	final String RES_OUT="RES_OUT";
	
	/**
	 * Method is written to get all the users details from the database.
	 * 
	 * @return List<Users>
	 */
	@ApiOperation(value = "Get all user details")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUserDetails() {
		logger.info(REQ_IN+" Get User Details Starts");
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
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Response> signUp(@RequestBody User user, HttpServletResponse res) {
		logger.info(REQ_IN+" Sign Up Starts");
		Response response=new Response();
		try{userService.signUp(user);
		}catch(Exception e) {
			response.setMessage(e+"");
			response.setStatus(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("Mail sent to the user with email " + user.getEmail());
		response.setStatus(1);
		logger.info(RES_OUT+" Sign Up ends");
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
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Response> logIn(@RequestBody LoginDTO user, HttpServletResponse res) {
		logger.info(REQ_IN+" Log In Starts");
		Response response=new Response();
		String token=null;
		try{token=userService.logIn(user);
		}catch(Exception e) {
			
			response.setMessage(e.getMessage()+"");
			response.setStatus(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("Welcome "+user.getEmail());
		response.setStatus(1);
		res.addHeader("token", token);
		logger.info("Token Set In Header");
		logger.info(RES_OUT+" Log In Ends ");
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
	@RequestMapping(value = "/activationlink", method = RequestMethod.GET)
	public ResponseEntity<Response> activation(HttpServletRequest req) {
		logger.info(REQ_IN+" Activating User");
		userService.claimToken(req.getQueryString());
		Response response=new Response();
		response.setMessage("Your account is successfully activated");
		response.setStatus(200);
		logger.info(RES_OUT+" User Activated");
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
	@RequestMapping(value = "/recoveraccount", method = RequestMethod.POST)
	public ResponseEntity<Response> passwordRecover(@RequestParam String email ){
		logger.info(REQ_IN+" Password Recover StartResetting Passworded");
		Response response=new Response();
		try{userService.passwordRecover(email);
			}catch(Exception e) {
				response.setMessage(e+"");
				response.setStatus(-1);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		response.setMessage("Link is sent to your mail.. ");
		response.setStatus(1);
		logger.info(RES_OUT+" Password Recovered");
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
	@RequestMapping(value="/resetpassword",method=RequestMethod.POST)
	public ResponseEntity<Response> resetPassword(@RequestParam String password,HttpServletRequest req){
		logger.info(REQ_IN+" Resetting Password");
		userService.resetPassword(req.getQueryString(),password);
		Response response=new Response();
		response.setMessage("Your Password Is Updated ");
		response.setStatus(200);
		logger.info(RES_OUT+" Password Successfully Reset");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
