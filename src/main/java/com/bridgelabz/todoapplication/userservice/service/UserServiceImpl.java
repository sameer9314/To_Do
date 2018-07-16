package com.bridgelabz.todoapplication.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.exception.LoginException;
import com.bridgelabz.todoapplication.exception.RegistrationException;
import com.bridgelabz.todoapplication.tokenutility.TokenUtility;
import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
import com.bridgelabz.todoapplication.userservice.model.RegistrationDTO;
import com.bridgelabz.todoapplication.userservice.model.User;
import com.bridgelabz.todoapplication.userservice.repository.UserRepository;


/**
 * Purpose : To provide services for User class.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 11/07/2018
 */
@Service
public class UserServiceImpl {

	// RepositoryImpl repository=new RepositoryImpl();

	@Autowired
	TokenUtility token;

	@Autowired
	UserRepository repository;

	@Autowired
	Mail mail;

	@Autowired
	MailService mailService;
	
	@Autowired
	User user;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/**
	 * @see com.bridgelabz.loginregistration.service.UserService#getUserDetails()
	 * 
	 * Method is defined to get all user present in the database.
	 * @return List<User>
	 */
	public List<User> getUserDetails() {
//		return repository.getUserDetails();
		return repository.findAll();
	}

	public void signUp(RegistrationDTO userRegistered) throws Exception {
		
		if (!userRegistered.getEmail().equals("")&&!userRegistered.getId().equals("")&&!userRegistered.getUserName().equals("")
				&&!userRegistered.getPhoneNumber().equals("")&&!userRegistered.getPassword().equals("")) {
			
			Optional<User> dbUser = repository.findByEmail(userRegistered.getEmail());
			if (dbUser.isPresent()) {
				throw new RegistrationException(userRegistered.getEmail() + "User Allready Exist");
			}
			
		user.setId(userRegistered.getId());	
		user.setEmail(userRegistered.getEmail());
		user.setUserName(userRegistered.getUserName());
		user.setPhoneNumber(userRegistered.getPhoneNumber());
		user.setPassword( passwordEncoder.encode(userRegistered.getPassword()));
		
		System.out.println("Generating Token");
		String validToken = token.generator(user);
		repository.save(user);
		mail.setSubject("Account Confirmation Link");
		mail.setBody("Click the link given below to activate your account \n\n "
				+ "http://192.168.0.61:8080//activationlink/?" + validToken);
		mail.setTo(user.getEmail());
		mailService.sendMail(mail);
		// MailSender.sendMail(user.getEmail(), validToken, null);
		System.out.println("Mail Sent");
		return;
		}
		System.out.println("Throwing exception");
		throw new Exception("Email cannot be null");
	}

	public void logIn(LoginDTO userloggedIn) throws Exception {
		String email=userloggedIn.getEmail();
		String password=userloggedIn.getPassword();
		System.out.println("validating user");
		System.out.println(email);
		System.out.println(password);
		if (!email.equals("") && !password.equals("")) {
			Optional<User> user = repository.findByEmail(email);
			System.out.println("Email founded");
			if (user.isPresent()) {
				if (!user.get().getStatus().equals("false")) {
					if (passwordEncoder.matches(password, user.get().getPassword())) {
						System.out.println("Password Matched");
						System.out.println("validating user true");
						return;
					}
					System.out.println("Password Incorrect");
					throw new LoginException("Password is incorrect");
				} else {
					throw new Exception("Go to your mail and click on the link first to validate your account");
				}
			}
			throw new LoginException("Email  is wrong ");
		}
		throw new LoginException("Email and password cannot be null");
	}

	/**
	 * Method is written to activate user account. As soon as user clicked the link
	 * sent to them their status in the database will be changed to true.
	 * 
	 * @param email
	 */
	public void claimToken(String claimedToken) {
		User newUser = new User();
		Optional<User> user = repository.findByEmail(token.parseJWT(claimedToken));
		newUser.setId(user.get().getId());
		newUser.setUserName(user.get().getUserName());
		newUser.setEmail(user.get().getEmail());
		newUser.setPhoneNumber(user.get().getPhoneNumber());
		newUser.setPassword(user.get().getPassword());
		newUser.setStatus("true");
		repository.save(newUser);
	}

/*	*//**
	 * @see com.bridgelabz.loginregistration.service.UserService#checkUserName(java.lang.String)
	 *
	 *      Method is written to check whether user with entered userName is present
	 *      in the database . If present return true or if not return false.
	 * @param userName
	 * @return boolean
	 * @throws Exception
	 *//*
	public void checkUserName(String email) throws Exception {
		// return repository.checkUserName(userName);
		
			return;
		}*/

	/**
	 * Method is written to recover the password of the user in case if they forget
	 * their password. Password will be sent to the user email.
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public void passwordRecover(String email) throws Exception {
		if (!email.equals("")) {
			Optional<User> user = repository.findByEmail(email);
			if (user.isPresent()) {
				System.out.println("Generating Token");
				String validToken = token.generator(user.get());
				System.out.println("Token generated to recover password "+validToken);
				mail.setSubject("Account Confirmation Link");
				mail.setBody("Click the link given below reset your password \n\n "
						+ "http://192.168.0.61:8080//resetpassword/?" + validToken);
				mail.setTo(user.get().getEmail());
				mailService.sendMail(mail);
				System.out.println("Mail Sent");
				
				return;
			}
			throw new Exception("Email Not Valid,Please Re Write Or SignUp First");
		}
		throw new Exception("Email Cannot Be Null");
	}

	public void resetPassword(String claimedToken,String password) {
		Optional<User> dbUser = repository.findByEmail(token.parseJWT(claimedToken));
		
		user.setId(dbUser.get().getId());
		user.setUserName(dbUser.get().getUserName());
		user.setEmail(dbUser.get().getEmail());
		user.setPhoneNumber(dbUser.get().getPhoneNumber());
		user.setPassword(password);
		user.setStatus("true");
		
		repository.save(user);
	}
	
}
