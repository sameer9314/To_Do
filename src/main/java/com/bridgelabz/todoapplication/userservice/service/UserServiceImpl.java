package com.bridgelabz.todoapplication.userservice.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.Utility.MailService;
import com.bridgelabz.todoapplication.Utility.RabbitMQSender;
import com.bridgelabz.todoapplication.tokenutility.TokenUtility;
import com.bridgelabz.todoapplication.userservice.model.LoginDTO;
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
public class UserServiceImpl implements UserService{

	@Autowired
	TokenUtility token;

	@Autowired
	UserRepository repository;

	@Autowired
	MailService mailService;

	@Autowired
	RabbitMQSender sender;
	
	
	@Autowired
	User user;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${ipaddress}")
	String ipaddress;

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	final String REQ_IN="REQ_IN";
	final String RES_OUT="RES_OUT";
	/**
	 * @see com.bridgelabz.loginregistration.service.UserService#getUserDetails()
	 * 
	 *      Method is defined to get all user present in the database.
	 * @return List<User>
	 */
	public List<User> getUserDetails() {
		logger.info(RES_OUT+" Get User Details Ends");
		return repository.findAll();
	}

	/**
	 * Method is written to receive the user object sent from the controller and
	 * after checking null values it generates token and send the email to the user
	 * to validate that the registered user is valid, only after checking that user
	 * is already not present.
	 * 
	 * @param userRegistered
	 * @throws Exception
	 */
	public void signUp(User userRegistered) throws Exception {
		if (!userRegistered.getEmail().equals("")
				&& !userRegistered.getUserName().equals("") && !userRegistered.getPhoneNumber().equals("")
				&& !userRegistered.getPassword().equals("")) {

			Optional<User> dbUser = repository.findByEmail(userRegistered.getEmail());
			if (dbUser.isPresent()) {
				logger.error("User Allready Exist");
				throw new Exception(userRegistered.getEmail() + "User Allready Exist");
			}
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
			user.setId(timeStamp);
			user.setEmail(userRegistered.getEmail());
			user.setUserName(userRegistered.getUserName());
			user.setPhoneNumber(userRegistered.getPhoneNumber());
			user.setPassword(passwordEncoder.encode(userRegistered.getPassword()));
			user.setStatus("false");

			String validToken = token.generator(user);
			repository.save(user);
			logger.info("User registered");
			String subject = "Account Confirmation Link";
			String body = "Click the link given below to activate your account \n\n " + ipaddress + "/activationlink/?"
					+ validToken;
			String to = user.getEmail();
			
			sender.send(subject, body, to);
			//mailService.sendMail(subject, body, to);
			logger.info("Mail Sent");
			return;
		}
		logger.error("Field is null");
		throw new Exception("Field cannot be null");
	}

	/**
	 * Method is written to take the log in credentials entered by the user. And
	 * check the credentials, if its correct then it allows user to log in otherwise
	 * throws exception.
	 * 
	 * @param userloggedIn
	 * @throws Exception
	 */
	public String logIn(LoginDTO userloggedIn) throws Exception {
		String email = userloggedIn.getEmail();
		String password = userloggedIn.getPassword();
		if (!email.equals("") && !password.equals("")) {
			Optional<User> user = repository.findByEmail(email);
			if (user.isPresent()) {
				if (!user.get().getStatus().equals("false")) {
					if (passwordEncoder.matches(password, user.get().getPassword())) {
						String validToken = token.generator(user.get());
						logger.info("User Logged In with Email : "+email);
						return validToken;
					}
					logger.error("Password is incorrect");
					throw new Exception("Password is incorrect");
				} else {
					logger.error("Account Not Activated");
					throw new Exception("Go to your mail and click on the link first to validate your account");
				}
			}
			logger.error("Email is Wrong");
			throw new Exception("Email  is wrong ");
		}
		logger.error("Email or password or Both is null");
		throw new Exception("Email and password cannot be null");
	}

	/**
	 * Method is written to activate user account. As soon as user clicked the link
	 * sent to them their status in the database will be changed to true.
	 * 
	 * @param email
	 */
	public void claimToken(String claimedToken) {
		String email=token.parseJWT(claimedToken).getSubject();
		Optional<User> dbUser = repository.findByEmail(email);
		logger.info("User Founded With Email : "+email);
		logger.info("Setting Status To True");
		dbUser.get().setStatus("true");
		repository.save(dbUser.get());
	}

	/**
	 * Method is written to recover the password of the user in case if they forget
	 * their password. Password will be sent to the user email.
	 * 
	 * @param email
	 * @throws Exception
	 */
	public void passwordRecover(String email) throws Exception {
		if (!email.equals("")) {
			Optional<User> user = repository.findByEmail(email);
			if (user.isPresent()) {
				String validToken = token.generator(user.get());
				String subject = "Account Confirmation Link";
				String body = "Click the link given below reset your password \n\n " + ipaddress + "/resetpassword/?"
						+ validToken;
				String to = user.get().getEmail();
				sender.send(subject, body, to);
				//mailService.sendMail(subject, body, to);
				logger.info("Link Is Sent To User With The New Token");
				return;
			}
			logger.error("Email Not Valid");
			throw new Exception("Email Not Valid,Please Re Write Or SignUp First");
		}
		logger.error("Email Is Null");
		throw new Exception("Email Cannot Be Null");
	}

	/**
	 * Method is written to reset the password of the user.
	 * 
	 * @param claimedToken
	 * @param password
	 */
	public void resetPassword(String claimedToken, String password) {
		Optional<User> dbUser = repository.findByEmail(token.parseJWT(claimedToken).getSubject());
		dbUser.get().setPassword(passwordEncoder.encode(password));
		dbUser.get().setStatus("true");
		repository.save(dbUser.get());
	}
}
