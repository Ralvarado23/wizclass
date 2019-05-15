package com.wizclass.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.User;
import com.wizclass.services.UserService;
import com.wizclass.utils.PageRender;

/**
 * This class contains methods that allow the user to interact with the pages that
 * are displayed in the web. It also let the user login/logout and register.
 * @author Raul Alvarado
 *
 */
@Controller
public class HomeController {
	
	@Autowired
	private PaginaRepository paginaRepository;
	
	private UserService userService;
	
	public HomeController (UserService userService) {
		this.userService = userService;
	}

	/**
	 * This method displays the web index page
	 * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the web index page
	 */
    @GetMapping("/")
    public String displayIndex(Model model, Principal principal) {
    	if (principal != null) {
    		User currentUser = userService.getCurrentuser(principal);
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    	}
    	return "index";
    }
    
    /**
     * This method displays the web 'sobre nosotros' (about us) page
	 * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the web 'sobre nosotros' page
     */
    @GetMapping("/about")
    public String displayAboutUs(Model model, Principal principal) {
    	if (principal != null) {
	    	User currentUser = userService.getCurrentuser(principal);
	    	model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    	}
    	return "nosotros";
    }
    
    /**
     * This method allows ADMINS to control other users´ info
     * @param page - this parameter contains the number that the paginator will display.
     * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
     * @return - this method returns the web 'administrar usuarios' page
     */
    @GetMapping("/adminUsers")
    public String mostrarAdminUsuarios(@RequestParam(name="page", defaultValue="0") int page, Model model, Principal principal) {
    	
    	Pageable pageRequest = PageRequest.of(page, 4);
		Page<User> users = userService.findAll(pageRequest);
		PageRender<User> pageRender = new PageRender<User>("/adminUsers", users);
		
		model.addAttribute("users", users);
		model.addAttribute("pageRender", pageRender);
    	
		if (principal != null) {
	    	User currentUser = userService.getCurrentuser(principal);
	    	model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    	}
		
        return "adminUsers";
    }
    
    /**
     * This method displays the web contact page
	 * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the web 'contacto' page
     */
    @GetMapping("/contact")
	public String contactPage(Principal principal, Model model) {
    	if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			model.addAttribute("user", currentUser);
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		}
		return "contacto";
	}
    
    /**
     * This method allows the user to view all the pages that he/she owns
	 * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the web 'mis paginas' page
     */
    @GetMapping("/myPages")
	public String displayMyPages(Principal principal, Model model) {
    	if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			model.addAttribute("user", currentUser);
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		}
    	
//    	model.addAttribute("paginas", paginaRepository.findAll()); Uncomment to see all pages with no filter
		return "misPaginas";
	}
    
    /**
     * This method allows the user to view all the pages that are in the cart
	 * @param model - this parameter is used to send an object to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - this method returns the web 'carrito' page
     */
    @GetMapping("/cart")
	public String displayCart(Principal principal, Model model) {
    	if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			List<Pagina> pagesUser = currentUser.getPaginas();
			List<Pagina> pagesCart = new ArrayList<>();
			
			double totalPrecio = 0;
			
			for (Pagina page : pagesUser) {
				if (page.getEnCarrito() == true) {
					pagesCart.add(page);
					totalPrecio = totalPrecio + page.getPrecio();
				}
			}
			
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
			model.addAttribute("paginas", pagesCart);
			model.addAttribute("totalPrecio", totalPrecio);
		}
		return "carrito";
	}
    
    /**
     * This method allows the user to remove items from the cart.
     * @param idPage - this parameter represents the id of the page to be removed from the cart
     * @param attributes - this parameter allows to send a personalized message to the view
     * @param principal - this parameter is used to get the current logged user
     * @return - this method returns the web 'carrito' page
     */
    @GetMapping("/cart/remove/{id}")
   	public String removeFromCart(@PathVariable("id") Long idPage, RedirectAttributes attributes, Principal principal) {
		
    	Pagina page = paginaRepository.findById(idPage).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		
		if (page != null) {
			if (page.getEnCarrito() == true && page.getUser().getId() == currentUser.getId()) {
				page.setEnCarrito(false);
				paginaRepository.save(page);
				attributes.addFlashAttribute("msgRemovedCart", "La página se ha eliminado del carrito correctamente.");
				return "redirect:/cart";
			}else if (page.getUser().getId() != currentUser.getId()) {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/cart";
			}
		}
    	
		attributes.addFlashAttribute("msgPageCartNotExists", "La página buscada no se encuentra en el carrito.");
    	return "redirect:/cart";
   	}
    
    /**
     * This method allows the user to purchase the pages in the cart.
     * @param attributes - this parameter allows to send a personalized message to the view
     * @param principal - this parameter is used to get the current logged user
     * @return - this method will redirect to the index where the purchase is completed correctly or
     * 			 to the cart page when the purchase is not correct.
     */
    @GetMapping("/buyPages")
	public String buyPage(Principal principal, RedirectAttributes attributes) {
		User currentUser = userService.getCurrentuser(principal);
		
		List<Pagina> pages = currentUser.getPaginas();
		int contPageCart = 0;
		
		for (Pagina page : pages) {
			if (page.getEnCarrito() == true) {
				contPageCart++;
				page.setEnCarrito(false);
				page.setComprado(true);
				paginaRepository.save(page);
			}
		}
		
		if (contPageCart != 0) {
			attributes.addFlashAttribute("msgPageBought", "La compra se ha realizado con éxito.");
			return "redirect:/";
		}else {
			attributes.addFlashAttribute("msgCartEmpty", "La cesta está vacía. No se puede realizar la compra.");
		return "redirect:/cart";
		}		
	}
    
    /**
     * This method displays the login form.
     * @param error - this optional parameter will indicate if an error occurs while starting the session.
     * @param logout - this optional parameter will indicate if the user recently closed the session.
     * @param principal - this parameter is used to get the current logged user
     * @param flash - this parameter allows to send a personalized message to the view
     * @return - this method will return a redirect to the web index when the login/logout is completed or
     * 			 a redirect to the login form when an error happens
     */
    @GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Principal principal,
			RedirectAttributes flash) {

		if (principal != null) {
			flash.addFlashAttribute("info", "Ya tiene una sesión abierta");
			return "redirect:/";
		}
		
		if (error != null) {
			flash.addFlashAttribute("error", "Datos incorrectos.");
			return "redirect:/login";
		}
		
		if (logout != null) {
			flash.addFlashAttribute("msgLogout", "Se ha cerrado la sesión.");
			return "redirect:/";
		}
		return "login";
	}
    
    /**
     * This method displays the register form.
     * @param model - this parameter is used to send an object to the view
     * @return - this method returns the register form
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
	    model.addAttribute("user", new User());
	    return "register";
    }
    
    /**
     * This method allows the user to register into the web
     * @param user - this parameter contains the object User sent from registerPage form
     * @param bindingResult - this parameter will check if the parameter user is valid
     * @param picture - this parameter contains the value of the attribute 'picture' of user object
     * @param newsletter - this parameter will return true if newsletter checkbox is checked or false if it is unchecked
     * @param attributes - this parameter allows to send a personalized message to the view
     * @param status - this parameter controls the status of the @ SessionAttributes annotation
     * @return - this method returns a redirect to the index when the user is registered correctly or
     * 			 will redirect back to register form when there is an error.
     */
	@PostMapping("/register")
    public String createNewUser(User user, BindingResult bindingResult, @RequestParam("file") MultipartFile picture,
    		@RequestParam(value = "newsletter", required = false) String newsletter, RedirectAttributes attributes, SessionStatus status) {
    	User userExists = userService.findUserByUsername(user.getUsername());
    	if (userExists != null) {
    		attributes.addFlashAttribute("message", "Error: Este usuario ya está registrado.");
    		return "redirect:/register";
    	} if (user.getEmail().isEmpty()) {
    		attributes.addFlashAttribute("message", "Error: El email no puede estar vacío.");
    		return "redirect:/register";
		}if (user.getUsername().isEmpty()) {
    		attributes.addFlashAttribute("message", "Error: El nombre de usuario no puede estar vacío.");
    		return "redirect:/register";
		}if (user.getPassword().isEmpty()) {
			attributes.addFlashAttribute("message", "Error: La contraseña no puede estar vacía.");
    		return "redirect:/register";
		}else {
	  
    		if(!picture.isEmpty()) {
    			String uniqueFileName = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
    			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);

    			try {
    				Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());
	
    				System.out.println("Se ha subido la imagen correctamente.");
		
    				user.setPicture(uniqueFileName);
    				System.out.println(user.getPicture());
		
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}else {
    			user.setPicture("avatar.png");
    		}
    		
    		if (newsletter != null) {
				user.setNewsletterActiva(true);
			}else {
				user.setNewsletterActiva(false);
			}
  
    		userService.saveUser(user);
    		status.setComplete();
    		attributes.addFlashAttribute("msgUserCreated", "Usuario registrado con éxito. ¡Bienvenido a WizClass!");
    		return "redirect:/";
    	}
    }
	
	//Test trying to find error 999 which shows no error message. It is an unusual error.
	/*@GetMapping("/error")
    public String redirectError(Model model, RedirectAttributes attributes) {
		attributes.addFlashAttribute("msgError", "Ha saltado el error 999.");
	    return "redirect:/";
    }*/
}