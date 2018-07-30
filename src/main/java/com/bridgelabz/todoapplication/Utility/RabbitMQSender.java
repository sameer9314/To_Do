package com.bridgelabz.todoapplication.Utility;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Purpose : To send the mail object in the queue.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 21/07/2018
 */
@Service
public class RabbitMQSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	Mail mail;

	@Value("todo.exchange")
	private String exchange;

	@Value("todo.routingkey")
	private String routingkey;

	/**
	 * To convert the mail object and send to the queue.
	 * 
	 * @param subject
	 * @param body
	 * @param to
	 */
	public void send(String subject, String body, String to) {
		System.out.println("Rabbit sender");
		mail.setSubject(subject);
		mail.setBody(body);
		mail.setTo(to);
		rabbitTemplate.convertAndSend(exchange, routingkey, mail);
		System.out.println("Send msg = " + mail);

	}
}