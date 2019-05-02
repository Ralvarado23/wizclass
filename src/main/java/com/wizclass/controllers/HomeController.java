package com.wizclass.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

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

import com.wizclass.model.NoticiaRepository;
import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserRepository;
import com.wizclass.services.UserService;
import com.wizclass.utils.PageRender;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PaginaRepository paginaRepository;
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	private UserService userService;
	
	public HomeController (UserService userService) {
		this.userService = userService;
	}

    @GetMapping("/")
    public String displayIndex(Model model, Principal principal) {
    	if (principal != null) {
    		User currentUser = userService.getCurrentuser(principal);
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    	}
    	return "index";
    }
    
    @GetMapping("/about")
    public String displayAboutUs(Model model, Principal principal) {
    	if (principal != null) {
	    	User currentUser = userService.getCurrentuser(principal);
	    	model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
    	}
    	return "nosotros";
    }
    
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
    
    @GetMapping("/contact")
	public String contactPage(Principal principal, Model model) {
    	if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			model.addAttribute("user", currentUser);
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		}
		return "contacto";
	}
    
    @GetMapping("/myPages")
	public String displayMyPages(Principal principal, Model model) {
    	if (principal != null) {
			User currentUser = userService.getCurrentuser(principal);
			model.addAttribute("user", currentUser);
			model.addAttribute("userNewsletter", currentUser.getNewsletterActiva());
		}
    	
//    	model.addAttribute("paginas", paginaRepository.findAll()); DESCOMENTAR PARA VER TODAS
		return "misPaginas";
	}
    
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
    
    @GetMapping("/register")
    public String registerPage(Model model) {
	    model.addAttribute("user", new User());
	    return "register";
    }
    
	@PostMapping("/register")
    public String createNewUser(User user, BindingResult bindingResult, @RequestParam("file") MultipartFile picture, @RequestParam(value = "newsletter", required = false) String newsletter, RedirectAttributes attributes, SessionStatus status) {
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
	
	/*@GetMapping("/error")
    public String redirectError(Model model, RedirectAttributes attributes) {
		attributes.addFlashAttribute("msgError", "Ha saltado el error 999. ÒwÓ");
	    return "redirect:/";
    }*/
}