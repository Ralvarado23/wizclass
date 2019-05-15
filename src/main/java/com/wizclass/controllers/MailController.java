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

/**
 * This class contains methods that allow the user to send emails.
 * @author Raul Alvarado
 *
 */
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
	
	/**
	 * This method allows the user to send email from the web
	 * @param name - this parameter contains the name of the transmitter
	 * @param from - this parameter contains the email of the transmitter
	 * @param subject - this parameter contains the subject of the message
	 * @param message - this parameter contains the content of the message
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @return - this method redirects to the index when the message is sent or to the
	 * 			 contact form when an error occurs.
	 */
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
	
	/**
	 * This method allows the user to send email from a school page
	 * @param idPage - this parameter represents the id of the page from where the email is sent.
	 * 		  Must be equal to pageOrigen to create the email.
	 * @param name - this parameter contains the name of the transmitter
	 * @param from - this parameter contains the email of the transmitter
	 * @param subject - this parameter contains the subject of the message
	 * @param message - this parameter contains the content of the message
	 * @param pageOrigen - this parameter represents the id of the page from where the email is sent.
	 * 		  Must be equal to idPage to create the email.
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the school index page when the email is sent, the school contact page
	 * 			 when the email has errors or the web index page when the user is not allowed to send the email.
	 */
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