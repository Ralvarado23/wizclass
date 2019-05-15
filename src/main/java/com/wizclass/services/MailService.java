package com.wizclass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * This class contains auxiliary methods related to mails.
 * @author Raul Alvarado
 *
 */
@Service
public class MailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	/**
	 * This method sends emails.
	 * @param from - this parameter contains the email of the transmitter
	 * @param to - this parameter contains data about whom is receiving the email.
	 * @param subject - this parameter contains the subject of the message
	 * @param text - this parameter contains the content of the message
	 */
	public void sendMail(String from, String to, String subject, String text) {
		SimpleMailMessage mail = new SimpleMailMessage(); 
		
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(text);
		
		javaMailSender.send(mail);
	}
}