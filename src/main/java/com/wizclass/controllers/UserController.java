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

import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserRepository;
import com.wizclass.services.UploadService;
import com.wizclass.services.UserService;

/**
 * This class contains methods that allow the user to interact with itself. It also contains
 * methods that are admin-only.
 * @author Raul Alvarado
 *
 */
@Controller
@SessionAttributes("user")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	private UploadService uploadservice;
	private UserService userService;
	
	public UserController(UploadService uploadservice, UserService userService) {
		this.uploadservice = uploadservice;
		this.userService = userService;
	}
	
	/**
	 * This method displays the profile page of the current logged user.
	 * @param principal - this parameter is used to get the current logged user
	 * @param model - this parameter is used to send an object to the view
	 * @return - this method returns the profile page.
	 */
    @GetMapping("/profile")
	public String profilePage(Principal principal, Model model) {
		User currentUser = userService.getCurrentuser(principal);
		model.addAttribute("user", currentUser);
		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		return "perfil";
	}

    /**
     * This method displays the user update form page.
     * @param principal - this parameter is used to get the current logged user
	 * @param model - this parameter is used to send an object to the view
     * @return - this method returns the user update form page.
     */
	@GetMapping("/update")
	public String userDataForm(Principal principal, Model model) {
		User currentUser = userService.getCurrentuser(principal);
		model.addAttribute("user", currentUser);
		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		return "perfilUpdate";
	}

	/**
	 * This method allows user to update their profiles.
	 * @param user - this parameter contains the object User sent from userDataForm form
	 * @param model - this parameter is used to send an object to the view
	 * @param picture - this parameter contains the value of the attribute 'picture' of user object
	 * @param username - this parameter contains the value of the attribute 'username' of user object
	 * @param email - this parameter contains the value of the attribute 'email' of user object
	 * @param password - this parameter contains the value of the attribute 'password' of user object
	 * @param newsletterForm - this parameter contains the value of the attribute 'newsletterActiva' of user object
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @param status - this parameter controls the status of the @ SessionAttributes annotation
	 * @return - this method returns a redirect to the user profile page when the update is correct, to the web
	 * 			 index page when one user change its own username, or to the user update form when the update fails.
	 */
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
	
	/**
	 * This method allows user to subscribe to the newsletter.
	 * @param principal - this parameter is used to get the current logged user
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @return - this method returns a redirect to the web index.
	 */
	@PostMapping("/addNewsletter")
	public String suscribeNewsletter(Principal principal, RedirectAttributes flash) {
		if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			currentUser.setNewsletterActiva(true);
			userRepository.save(currentUser);
			flash.addFlashAttribute("msgSubNewsletter", "Te has suscrito a nuestra newsletter con éxito.");
			return "redirect:/";
		}
		return "redirect:/";
	}
	
	/**
	 * This method allow ADMINS to view users´ profiles.
	 * @param id - this parameter represents the id of the user to be displayed
	 * @param model - this parameter is used to send an object to the view
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns a redirect to the details page when the selected user exists or
	 * 			 a redirect to the admin users page when it does not exist.
	 */
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
	
	/**
	 * This method allows ADMINS to get users´s profile update form.
	 * @param id - this parameter represents the id of the user to be displayed
	 * @param model - this parameter is used to send an object to the view
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the update form if the user exists or the
	 * 			 admin users page otherwise.
	 */
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
	
	/**
	 * This method allows ADMINS to update users´ profiles.
	 * @param principal - this parameter is used to get the current logged user
	 * @param user - this parameter contains the object User sent from update form
	 * @param model - this parameter is used to send an object to the view
	 * @param picture - this parameter contains the value of the attribute 'picture' of user object
	 * @param username - this parameter contains the value of the attribute 'username' of user object
	 * @param email - this parameter contains the value of the attribute 'email' of user object
	 * @param password - this parameter contains the value of the attribute 'password' of user object
	 * @param newsletterForm - this parameter contains the value of the attribute 'newsletterActiva' of user object
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @param status - this parameter controls the status of the @ SessionAttributes annotation
	 * @return - this method returns a redirect to the admin users page when the update is correct, to the web
	 * 			 index page when one admin change its own username, or to the user update form when the update fails.
	 */
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
	
	/**
	 * This method allow ADMINS to delete users´ account.
	 * @param id - this parameter represents the id of the user to be deleted.
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns a redirect to the admin users page when the deleted user is not
	 * 			 logged or to the web index page otherwise.
	 */
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
	
	/**
	 * This method allow ADMINS to view all users´ pages. 
	 * @param id - this parameter represents the id of the user which pages are displayed.
	 * @param model - this parameter is used to send an object to the view
	 * @param flash - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns a redirect to the admin users page when the selected user
	 * 			 does not exist or to the pages page otherwise.
	 */
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
	
	/**
	 * This method displays the user register form for ADMINS.
	 * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the register admin page.
	 */
	@GetMapping("/createUserAdmin")
	public String mostrarCreateUser(Model model, Principal principal) {
		User currentUser = userService.getCurrentuser(principal);
		
		model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		model.addAttribute("user", new User());
		return "registerAdmin";
	}
	
	/**
	 * This method allow ADMINS to create new users.
	 * @param user - this parameter contains the object User sent from mostrarCreateUser form
	 * @param bindingResult - this parameter will check if the parameter user is valid
	 * @param model - this parameter is used to send an object to the view
	 * @param picture - this parameter contains the value of the attribute 'picture' of user object
	 * @param user_role - this parameter will return true if user_role checkbox is checked or false if it is unchecked
	 * @param admin_role - this parameter will return true if admin_role checkbox is checked or false if it is unchecked
	 * @param newsletterForm - this parameter contains the value of the attribute 'newsletterActiva' of user object
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param status - this parameter controls the status of the @ SessionAttributes annotation
	 * @return - this method return a redirect to the admin users page if the user is created correctly or to the
	 * 			 register form otherwise.
	 */
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