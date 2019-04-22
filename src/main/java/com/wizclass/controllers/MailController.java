package com.wizclass.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.services.MailService;
import com.wizclass.services.UserService;

@Controller
public class MailController {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private PaginaRepository paginaRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	private UserService userService;
	
	public MailController (UserService userService) {
		this.userService = userService;
	}
	
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
		mailService.sendMail("wizclass.app@gmail.com", "wizclass.app@gmail.com", subject, text);

		flash.addFlashAttribute("msgMailSent", "Se ha enviado la solicitud correctamente.");
		return "redirect:/";
	}
	
	@PostMapping("/sendMailApp/{idPage}")
	public String sendMailApp(@PathVariable("idPage") Long idPage, @RequestParam("name") String name, @RequestParam("email") String from,
			@RequestParam("subject") String subject, @RequestParam("message") String message, @RequestParam("pageOrigen") Long pageOrigen,
			RedirectAttributes flash, Principal principal) {
		
		if (name.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El nombre no puede estar vacio.");
			return "redirect:/page/" + idPage + "/contacto";
		}else if (from.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El email de contacto no puede estar vacio.");
			return "redirect:/page/" + idPage + "/contacto";
		}else if (subject.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El asunto no puede estar vacio.");
			return "redirect:/page/" + idPage + "/contacto";
		}else if (message.isEmpty()) {
			flash.addFlashAttribute("msgMailError", "El mensaje no puede estar vacio.");
			return "redirect:/page/" + idPage + "/contacto";
		}else if (pageOrigen != idPage) {
			flash.addFlashAttribute("msgMailError", "Error al enviar la solicitud.");
			return "redirect:/page/" + pageOrigen + "/contacto";
		}
		Pagina page = paginaRepository.findById(idPage).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			if (page.getComprado() == true) {
				
				String text = message + "\n\n De: " + from + ", " + name;
				mailService.sendMail("wizclass.app@gmail.com", page.getEmailContacto(), subject, text);

				flash.addFlashAttribute("msgMailSent", "Se ha enviado la solicitud correctamente.");
				return "redirect:/page/" + idPage + "/index";
				
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					
					String text = message + "\n\n De: " + from + ", " + name;
					mailService.sendMail("wizclass.app@gmail.com", page.getEmailContacto(), subject, text);

					flash.addFlashAttribute("msgMailSent", "Se ha enviado la solicitud correctamente.");
					return "redirect:/page/" + idPage + "/index";
					
				}else {
					flash.addFlashAttribute("msgPageNotPublic", "La página buscada no ha sido publicada aún.");
					return "redirect:/";
				}
			}
		}else {
			flash.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}
}
