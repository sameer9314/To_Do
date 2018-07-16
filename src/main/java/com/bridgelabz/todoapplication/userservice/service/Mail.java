package com.bridgelabz.todoapplication.userservice.service;

import org.springframework.stereotype.Service;

@Service
public class Mail {

	private String to;
	private String subject;
	private String body;

	public Mail() {
		super();
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
