package com.wizclass.controllers;

import java.security.Principal;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.Ensenanza;
import com.wizclass.model.Noticia;
import com.wizclass.model.NoticiaRepository;
import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.services.EnsenanzaServiceImpl;
import com.wizclass.services.NoticiaService;
import com.wizclass.services.UserService;
import com.wizclass.utils.PageRender;

/**
 * This class contains methods that allow the user to interact with the pages that
 * are displayed in the app (view format), send them to the cart or delete them.
 * @author Raul Alvarado
 *
 */
@Controller
@SessionAttributes("pagina")
@RequestMapping("/page")
public class PaginaController {
	
	@Autowired
	private PaginaRepository paginaRepository;
	
	@Autowired
	private NoticiaRepository noticiaRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private UserService userService;
	private NoticiaService noticiaService;
	
	public PaginaController(UserService userService, NoticiaService noticiaService) {
		this.userService = userService;
		this.noticiaService = noticiaService;
	}
	
	/**
	 * This method allows the user to view the index page of a school page which
	 * is going to be displayed in view format
	 * @param id- this parameter represents the id of the page to be displayed
	 * @param model - this parameter is used to send an object to the view 
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns either the view to the page if the user has permission or
	 * 			 will redirect to the web index in case the user is not allowed to enter
	 */
	@GetMapping("/{id}/index")
	public String viewPageIndex(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		List<Noticia> noticias = noticiaRepository.findTop3ByPaginaOrderByIdDesc(page);
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			TreeSet<Ensenanza> ensenanzasOrden = new TreeSet<Ensenanza>(new EnsenanzaServiceImpl());
			for (Ensenanza ensenanza : page.getEnsenanzas()) {
				ensenanzasOrden.add(ensenanza);
			}
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				model.addAttribute("ensenanzasOrden", ensenanzasOrden);
				model.addAttribute("noticiasIndex", noticias);
				return "appIndex";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					model.addAttribute("ensenanzasOrden", ensenanzasOrden);
					model.addAttribute("noticiasIndex", noticias);
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
	
	/**
	 * This method allows the user to view the 'oferta educativa' page of a school page which
	 * is going to be displayed in view format
	 * @param id- this parameter represents the id of the page to be displayed
	 * @param model - this parameter is used to send an object to the view 
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns either the view to the page if the user has permission or
	 * 			 will redirect to the web index in case the user is not allowed to enter
	 */
	@GetMapping("/{id}/oferta_educativa")
	public String viewPageOfEd(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			TreeSet<Ensenanza> ensenanzasOrden = new TreeSet<Ensenanza>(new EnsenanzaServiceImpl());
			for (Ensenanza ensenanza : page.getEnsenanzas()) {
				ensenanzasOrden.add(ensenanza);
			}
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				model.addAttribute("ensenanzasOrden", ensenanzasOrden);
				return "appOfertaEducativa";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					model.addAttribute("ensenanzasOrden", ensenanzasOrden);
					return "appOfertaEducativa";
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
	
	/**
	 * This method allows the user to view the 'noticias' page of a school page which
	 * is going to be displayed in view format
	 * @param newsPage - this parameter contains the number that the paginator will display.
	 * @param id- this parameter represents the id of the page to be displayed
	 * @param model - this parameter is used to send an object to the view 
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns either the view to the page if the user has permission or
	 * 			 will redirect to the web index in case the user is not allowed to enter
	 */
	@GetMapping("/{id}/noticias")
	public String viewPageNoticias(@RequestParam(name="page", defaultValue="0") int newsPage, @PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			Pageable pageRequest = PageRequest.of(newsPage, 4);
			Page<Noticia> noticias = noticiaService.findAllByPagina(page, pageRequest);
			PageRender<Noticia> pageRender = new PageRender<Noticia>("/page/" + id + "/noticias", noticias);
			
			TreeSet<Ensenanza> ensenanzasOrden = new TreeSet<Ensenanza>(new EnsenanzaServiceImpl());
			for (Ensenanza ensenanza : page.getEnsenanzas()) {
				ensenanzasOrden.add(ensenanza);
			}
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				model.addAttribute("ensenanzasOrden", ensenanzasOrden);
				model.addAttribute("noticiasSeccion", noticias);
				model.addAttribute("pageRender", pageRender);
				return "appNoticias";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					model.addAttribute("ensenanzasOrden", ensenanzasOrden);
					model.addAttribute("noticiasSeccion", noticias);
					model.addAttribute("pageRender", pageRender);
					return "appNoticias";
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
	
	//Possible short-term implementation
	/*@GetMapping("/{id}/secretaria")
	public String viewPageSecretaria(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				return "appSecretaria";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					return "appSecretaria";
				}else {
					attributes.addFlashAttribute("msgPageNotPublic", "La página buscada no ha sido publicada aún.");
					return "redirect:/";
				}
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}*/
	
	/**
	 * This method allows the user to view the 'calendario escolar' page of a school page which
	 * is going to be displayed in view format
	 * @param id- this parameter represents the id of the page to be displayed
	 * @param model - this parameter is used to send an object to the view 
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns either the view to the page if the user has permission or
	 * 			 will redirect to the web index in case the user is not allowed to enter
	 */
	@GetMapping("/{id}/calendario_escolar")
	public String viewPageCalendarioEscolar(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				return "appCalendarioEscolar";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					return "appCalendarioEscolar";
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
	
	/**
	 * This method allows the user to view the 'contacto' page of a school page which
	 * is going to be displayed in view format.
	 * @param id- this parameter represents the id of the page to be displayed
	 * @param model - this parameter is used to send an object to the view 
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns either the view to the page if the user has permission or
	 * 			 will redirect to the web index in case the user is not allowed to enter
	 */
	@GetMapping("/{id}/contacto")
	public String viewPageContacto(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = new User();
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (principal != null) {
			currentUser = userService.getCurrentuser(principal);
		}else {
			currentUser = null;
		}
		
		if (page != null) {
			
			if (page.getComprado() == true) {
				model.addAttribute("pagina", page);
				return "appContacto";
			}else {
				if ((currentUser != null) && ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin)))) {
					model.addAttribute("pagina", page);
					return "appContacto";
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
	
	/**
	 * This method allows the user to send a selected page to the cart.
	 * @param id - this parameter represents the id of the page to be moved to the cart
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns a redirect to the page which contains user´s pages info.
	 */
	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable("id") Long id, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		
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
	
	/**
	 * This method allows the user to delete a selected page.
	 * @param id - this parameter represents the id of the page to be deleted
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @param principal - this parameter is used to get the current logged user
	 * @return - the method returns a redirect to the page which contains user´s pages info.
	 */
	@GetMapping("/delete/{id}")
	public String deletePage(@PathVariable("id") Long id, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		
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
	
	/**
	 * This method allows ADMINS to delete a selected page.
	 * @param idPage - this parameter represents the id of the page to be deleted
	 * @param attributes - this parameter allows to send a personalized message to the view
	 * @return - the method returns a redirect to the page which contains user´s pages info.
	 */
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