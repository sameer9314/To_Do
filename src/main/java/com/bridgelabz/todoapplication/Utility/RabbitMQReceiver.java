package com.bridgelabz.todoapplication.Utility;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQReceiver {
	
	@Autowired
	MailService mailService;
	
	@RabbitListener(queues="javainuse.queue")
	public void mailReceiver(Mail mail) throws MessagingException {
		mailService.sendMail(mail.getSubject(), mail.getBody(), mail.getTo());
	}
}
