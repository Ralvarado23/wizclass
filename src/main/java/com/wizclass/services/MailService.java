package com.wizclass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendMail(String from, String to, String subject, String text) {
		SimpleMailMessage mail = new SimpleMailMessage(); 
		
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(text);
		
		javaMailSender.send(mail);
	}

}
