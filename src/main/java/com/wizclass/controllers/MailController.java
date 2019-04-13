package com.wizclass.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.services.MailService;

@Controller
public class MailController {

	@Autowired
	private MailService mailService;
	
	@PostMapping("/sendMail")
	public String sendMail(@RequestParam("name") String name, @RequestParam("email") String from,
			@RequestParam("subject") String subject, @RequestParam("message") String message,
			RedirectAttributes flash) {

		String text = message + "\n\n De: " + from + ", " + name;
		mailService.sendMail("raulalvarado.**@gmail.com", "raulalvarado.23@gmail.com", subject, text);

		flash.addFlashAttribute("msgMailSent", "Se ha enviado la solicitud correctamente.");
		return "redirect:/";
	}
}
