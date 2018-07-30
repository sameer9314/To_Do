package com.bridgelabz.todoapplication.Utility;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Purpose : To receive the mail from the queue.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 21/07/2018
 */
@Service
public class RabbitMQReceiver {

	@Autowired
	MailService mailService;

	/**
	 * 
	 * @param mail
	 * @throws MessagingException
	 */
	@RabbitListener(queues = "todo.queue")
	public void mailReceiver(Mail mail) throws MessagingException {
		mailService.sendMail(mail.getSubject(), mail.getBody(), mail.getTo());
	}
}
