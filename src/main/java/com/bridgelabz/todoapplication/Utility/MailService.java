package com.bridgelabz.todoapplication.Utility;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

	public void sendMail(String subject,String body,String to ) throws MessagingException;
}
