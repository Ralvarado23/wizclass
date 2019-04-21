package com.wizclass.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		if (name.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El nombre no puede estar vacio.");
			return "redirect:/contact";
		}else if (from.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El email de contacto no puede estar vacio.");
			return "redirect:/contact";
		}else if (subject.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El asunto no puede estar vacio.");
			return "redirect:/contact";
		}else if (message.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El mensaje no puede estar vacio.");
			return "redirect:/contact";
		}

		String text = message + "\n\n De: " + from + ", " + name;
		mailService.sendMail("raulalvarado.69@gmail.com", "raulalvarado.23@gmail.com", subject, text);

		flash.addFlashAttribute("msgMailSent", "Se ha enviado la solicitud correctamente.");
		return "redirect:/";
	}
}
