package com.bridgelabz.todoapplication.Utility;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Autowired
	Mail mail;
	
	@Value("javainuse.exchange")
	private String exchange;
	
	@Value("javainuse.routingkey")
	private String routingkey;	
	
	public void send(String subject,String body, String to) {
		System.out.println("Rabbit sender");
		mail.setSubject(subject);
		mail.setBody(body);
		mail.setTo(to);
		rabbitTemplate.convertAndSend(exchange, routingkey, mail);
		System.out.println("Send msg = " + mail);
	    
	}
}