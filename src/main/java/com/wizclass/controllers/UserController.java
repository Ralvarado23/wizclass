package com.wizclass.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.PaginaRepository;
import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserRepository;
import com.wizclass.services.UploadService;
import com.wizclass.services.UserService;

@Controller
@SessionAttributes("user")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PaginaRepository paginaRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	private UploadService uploadservice;
	private UserService userService;
	
	public UserController(UploadService uploadservice, UserService userService) {
		this.uploadservice = uploadservice;
		this.userService = userService;
	}
	
    @GetMapping("/profile")
	public String profilePage(Principal principal, Model model) {
		User currentUser = userService.getCurrentuser(principal);
		model.addAttribute("user", currentUser);
		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		return "perfil";
	}

	@GetMapping("/update")
	public String userDataForm(Principal principal, Model model) {
		User currentUser = userService.getCurrentuser(principal);
		model.addAttribute("user", currentUser);
		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		return "perfilUpdate";
	}

	@PostMapping("/update")
	public String userDataForm(@Valid User user, Model model,
			@RequestParam("file") MultipartFile picture,
			@RequestParam("user") String username,
			@RequestParam("mail") String email,
			@RequestParam("pass") String password,
			@RequestParam(value = "newsletter", required = false) String newsletterForm,
			RedirectAttributes flash, SessionStatus status) {
		if(!password.isEmpty() && password!="") {
			user.setPassword(passwordEncoder.encode(password));
		}
		
		if(!email.isEmpty() && email != "") {
			user.setEmail(email);
		}
		
		if(!username.isEmpty() && username != "") {
			User userExists = userService.findUserByUsername(username);
			
			if (userExists != null) {
				flash.addFlashAttribute("messageErrorUser", "Error: Este usuario ya está registrado.");
				return "redirect:/user/update";
			}else {
				user.setUsername(username);
				userRepository.save(user);
				SecurityContextHolder.getContext().setAuthentication(null);
				flash.addFlashAttribute("messageNameChanged", "Se ha cerrado la sesión debido a un cambio en el nombre de usuario.");
				return "redirect:/";
			}
		}
		
		if (newsletterForm != null) {
			if (newsletterForm.equalsIgnoreCase("newsletter_activa")) {
	        	user.setNewsletterActiva(true);
			}else if (newsletterForm.equalsIgnoreCase("newsletter_desactivada")) {
	        	user.setNewsletterActiva(false);
			}
		}else {
			flash.addFlashAttribute("messageErrorUser", "Error: Selecciona el estado de la newsletter.");
			return "redirect:/user/update";
		}
		
		if(!picture.isEmpty()) {
			uploadservice.deleteImage(user);
			uploadservice.addImage(user, picture, flash);
		}

		userRepository.save(user);
		status.setComplete();
		flash.addFlashAttribute("msgUserModified", "Datos modificados con éxito.");
		
		return "redirect:/user/profile";
	}
	
	@PostMapping("/addNewsletter")
	public String suscribeNewsletter(Principal principal, Model model, RedirectAttributes flash) {
		if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			currentUser.setNewsletterActiva(true);
			userRepository.save(currentUser);
			flash.addFlashAttribute("msgSubNewsletter", "Te has suscrito a nuestra newsletter con éxito.");
			return "redirect:/";
		}
		return "redirect:/";
	}
	
	@GetMapping("/details/{id}")
    public String detailSubmit(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal) {
    	User user = userRepository.findById(id).orElse(null);
    	User currentUser = userService.getCurrentuser(principal);
    	
    	if (user != null) {
    		model.addAttribute("user", user);
    		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    		return "detalles";
    	}
    	
		flash.addFlashAttribute("msgUserNotExists", "El usuario buscado no existe.");
    	return "redirect:/adminUsers";
    }
	
	@GetMapping("/updateAdmin/{id}")
	public String update(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal){
		User user = userRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		
		if (user != null) {
    		model.addAttribute("user", user);
    		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    		return "perfilUpdateAdmin";
    	}
		
		flash.addFlashAttribute("msgUserNotExists", "El usuario buscado no existe.");
    	return "redirect:/adminUsers";
	}
	
	@PostMapping("/updateAdmin")
	public String adminUpdateForm(Principal principal, @Valid User user, Model model,
			@RequestParam("file") MultipartFile picture,
			@RequestParam("user") String username,
			@RequestParam("mail") String email,
			@RequestParam("pass") String password,
			@RequestParam(value = "newsletter", required = false) String newsletterForm,
			RedirectAttributes flash, SessionStatus status) {
		if(!password.isEmpty() && password!="") {
			user.setPassword(passwordEncoder.encode(password));
		}
		
		if(!email.isEmpty() && email!="") {
			user.setEmail(email);
		}
		
		if(!username.isEmpty() && username!="") {
			User userExists = userService.findUserByUsername(username);
			
			if (userExists != null) {
				model.addAttribute("messageErrorUser", "Error: Este usuario ya está registrado.");
				model.addAttribute("user", user);
				return "perfilUpdateAdmin";
			}else {
				if (principal.getName() == user.getUsername()) {
					user.setUsername(username);
					userRepository.save(user);
					SecurityContextHolder.getContext().setAuthentication(null);
					flash.addFlashAttribute("messageNameChanged", "Se ha cerrado la sesión debido a un cambio en el nombre del usuario logueado.");
					return "redirect:/";
				}else {
					user.setUsername(username);
				}
			}
		}
		
		if (newsletterForm != null) {
			if (newsletterForm.equalsIgnoreCase("newsletter_activa")) {
	        	user.setNewsletterActiva(true);
			}else if (newsletterForm.equalsIgnoreCase("newsletter_desactivada")) {
	        	user.setNewsletterActiva(false);
			}
		}else {
			flash.addFlashAttribute("messageErrorUser", "Error: Selecciona el estado de la newsletter.");
			return "redirect:/user/update";
		}
		
		if(!picture.isEmpty()) {
			uploadservice.deleteImage(user);
			uploadservice.addImage(user, picture, flash);
		}
		
		userRepository.save(user);
		status.setComplete();
		flash.addFlashAttribute("msgUserModificado", "Datos modificados con éxito.");
		
		model.addAttribute("user", user);
		return "redirect:/adminUsers";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes attributes, Principal principal) {
		User user = userRepository.findById(id).orElse(null);
		
		if (user != null) {
			uploadservice.deleteImage(user);
			
			if (principal.getName() == user.getUsername()) {
				userRepository.deleteById(id);
				SecurityContextHolder.getContext().setAuthentication(null);
				attributes.addFlashAttribute("msgDeletedUser", "El usuario se ha borrado correctamente.");
				return "redirect:/";
			}
			
			userRepository.deleteById(id);
			attributes.addFlashAttribute("msgDeletedUser", "El usuario se ha borrado correctamente.");
			return "redirect:/adminUsers";
		}
		
		attributes.addFlashAttribute("msgUserNotExists", "El usuario buscado no existe.");
    	return "redirect:/adminUsers";
	}
	
	@GetMapping("/pages/{id}")
    public String displayPagesUser(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal) {
    	User user = userRepository.findById(id).orElse(null);
    	User currentUser = userService.getCurrentuser(principal);
    	
    	if (user != null) {
    		model.addAttribute("user", user);
    		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    		return "paginasUserAdmin";
    	}
		
		flash.addFlashAttribute("msgUserNotExists", "El usuario buscado no existe.");
    	return "redirect:/adminUsers";
    }
	
	@GetMapping("/createUserAdmin")
	public String mostrarCreateUser(Model model, Principal principal) {
		User currentUser = userService.getCurrentuser(principal);
		
		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		model.addAttribute("user", new User());
		return "registerAdmin";
	}
	
	@PostMapping("/createUserAdmin")
    public String postCreateUser(User user, BindingResult bindingResult, Model model,
    		@RequestParam("file") MultipartFile picture,
    		@RequestParam(value = "user_role", required = false) String user_role,
    		@RequestParam(value = "admin_role", required = false) String admin_role,
    		@RequestParam(value = "newsletter", required = false) String newsletterForm,
    		RedirectAttributes attributes, SessionStatus status) {
		
    	User userExists = userService.findUserByUsername(user.getUsername());
    	if (userExists != null) {
    		attributes.addFlashAttribute("message", "Error: Este usuario ya está registrado.");
    		return "redirect:/user/createUserAdmin";
		}if (user.getUsername().isEmpty()) {
    		attributes.addFlashAttribute("message", "Error: El nombre de usuario no puede estar vacío.");
    		return "redirect:/user/createUserAdmin";
		} if (user.getEmail().isEmpty()) {
    		attributes.addFlashAttribute("message", "Error: El email no puede estar vacío.");
    		return "redirect:/user/createUserAdmin";
		}if (user.getPassword().isEmpty()) {
			attributes.addFlashAttribute("message", "Error: La contraseña no puede estar vacía.");
    		return "redirect:/user/createUserAdmin";
		}else {
	  
    		Role userUserRole = roleRepository.findByRole("USER");
            Role userAdminRole = roleRepository.findByRole("ADMIN");

            ArrayList<Role> roles = new ArrayList<Role>();
            
            if (user_role!=null) {
				roles.add(userUserRole);
			}
            
            if (admin_role!=null) {
				roles.add(userAdminRole);
			}
            
            if (roles.size()==0) {
            	attributes.addFlashAttribute("message", "Error: Escoge al menos un rol.");
        		return "redirect:/user/createUserAdmin";
			}
    		
    		if (newsletterForm != null) {
    			if (newsletterForm.equalsIgnoreCase("newsletter_activa")) {
    	        	user.setNewsletterActiva(true);
    			}else if (newsletterForm.equalsIgnoreCase("newsletter_desactivada")) {
    	        	user.setNewsletterActiva(false);
    			}
    		}else {
    			attributes.addFlashAttribute("message", "Error: Selecciona el estado de la newsletter.");
        		return "redirect:/user/createUserAdmin";
    		}
    		
    		if(!picture.isEmpty()) {
    			String uniqueFileName = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
    			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);

    			try {
    				Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());
	
    				attributes.addFlashAttribute("info", "Se ha subido la imagen correctamente.");
		
    				user.setPicture(uniqueFileName);
    				System.out.println(user.getPicture());
		
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}else {
    			user.setPicture("avatar.png");
    		}
    		
    		userService.saveUserRole(user, roles);
    		
    		status.setComplete();
    		attributes.addFlashAttribute("msgUserCreado", "Se ha registrado un nuevo usuario.");
    		return "redirect:/adminUsers";
    	}
    }
}