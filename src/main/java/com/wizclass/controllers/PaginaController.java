package com.wizclass.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserRepository;

@Controller
@SessionAttributes("pagina")
@RequestMapping("/page")
public class PaginaController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PaginaRepository paginaRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/{id}/index")
	public String viewPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userRepository.findByUsername(principal.getName());
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				return "appIndex";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					return "appIndex";
				}else {
					attributes.addFlashAttribute("msgPageNotPublic", "La página buscada no ha sido publicada aún.");
					return "redirect:/";
				}
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}
	
	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable("id") Long id, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userRepository.findByUsername(principal.getName());
		
		if (page != null) {
			if (page.getEnCarrito() == false && page.getUser().getId() == currentUser.getId()) {
				page.setEnCarrito(true);
				paginaRepository.save(page);
				attributes.addFlashAttribute("msgPageToCart", "La página se ha añadido al carrito.");
				return "redirect:/myPages";
			}else if (page.getUser().getId() != currentUser.getId()) {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/myPages";
			}
		}
		
		attributes.addFlashAttribute("msgPageAlreadyInCart", "La página buscada ya se encuentra en el carrito.");
    	return "redirect:/myPages";
	}
	
	@GetMapping("/update/{id}")
	public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userRepository.findByUsername(principal.getName());
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					return "appFormUpdate";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}

	@GetMapping("/delete/{id}")
	public String deletePage(@PathVariable("id") Long id, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userRepository.findByUsername(principal.getName());
		
		if (page != null) {
			if (page.getUser().getId() == currentUser.getId()) {
				paginaRepository.deleteById(id);
				attributes.addFlashAttribute("msgDeletedPage", "La página se ha borrado correctamente.");
				return "redirect:/myPages";
			}
		}
		
		attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
		return "redirect:/myPages";
	}
	
	@GetMapping("/deleteAdmin/{id}")
	public String deletePageAdmin(@PathVariable("id") Long idPage, RedirectAttributes attributes) {
		
		Pagina page = paginaRepository.findById(idPage).orElse(null);
		Long idUser = (long) 0;
		if (page != null) {
			idUser = page.getUser().getId();
			paginaRepository.deleteById(idPage);
			attributes.addFlashAttribute("msgDeletedPage", "La página se ha borrado correctamente.");
			return "redirect:/user/pages/" + idUser;
		}
		
		attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
		return "redirect:/adminUsers";
	}
}
