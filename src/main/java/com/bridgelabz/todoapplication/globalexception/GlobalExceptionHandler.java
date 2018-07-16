package com.bridgelabz.todoapplication.globalexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.todoapplication.exception.LoginException;
import com.bridgelabz.todoapplication.exception.RegistrationException;
import com.bridgelabz.todoapplication.userservice.model.Response;


/**
 * Purpose : GlobalExceptiona Class to handle all the exception occurs during
 * the application execution.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 147/07/2018
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Method is written to handle all the exception occur during User registration.
	 * And return back the response with the HttpStatus.
	 * 
	 * @param exception
	 * @return ResponseEntity<Response>
	 */
	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<Response> handleRegistartionException(RegistrationException exception) {
		logger.info("Error Occured While Registration" + exception.getMessage(), exception);

		Response response = new Response();

		response.setMessage(exception.getMessage());
		response.setStatus(0);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Method is written to handle all the exception occur during User Log In. And
	 * return back the response with the HttpStatus.
	 * 
	 * @param exception
	 * @return ResponseEntity<Response>
	 */
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<Response> handleLoginException(LoginException exception) {
		logger.info("Error Ocuured While Login" + exception.getMessage(), exception);

		Response response = new Response();

		response.setMessage(exception.getMessage());
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Method is written to handle all the common exception which can be occur
	 * during the Application execution. And return back the response with the
	 * HttpStatus. And return back the response with the HttpStatus.
	 * 
	 * @param exception
	 * @return ResponseEntity<Response>
	 */
	@ExceptionHandler
	public ResponseEntity<Response> handleOtherException(Exception exception) {
		logger.info("Error Occured: " + exception.getMessage(), exception);

		Response response = new Response();

		response.setMessage(exception.getMessage());
		response.setStatus(-1);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
