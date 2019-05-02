package com.wizclass.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.Paleta;
import com.wizclass.model.PaletaRepository;
import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserRepository;
import com.wizclass.services.EnsenanzaServiceImpl;
import com.wizclass.services.NoticiaService;
import com.wizclass.services.UserService;
import com.wizclass.utils.PageRender;
import com.wizclass.model.Ensenanza;
import com.wizclass.model.EnsenanzaRepository;
import com.wizclass.model.Noticia;
import com.wizclass.model.NoticiaRepository;

@Controller
@RequestMapping("/app")
@SessionAttributes("pagina")
public class AppController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PaginaRepository paginaRepository;
	
	@Autowired
	private EnsenanzaRepository ensenanzaRepository;
	
	@Autowired
	private PaletaRepository paletaRepository;
	
	@Autowired
	private NoticiaRepository noticiaRepository;

//	private PaginaService paginaService;
//	
//	public AppController (PaginaService paginaService) {
//		this.paginaService = paginaService;
//	}
	
	private UserService userService;
	private NoticiaService noticiaService;
	
	public AppController (UserService userService, NoticiaService noticiaService) {
		this.userService = userService;
		this.noticiaService = noticiaService;
	}
	
	@GetMapping("/savePage")
	public String savePage(RedirectAttributes attributes, SessionStatus status) {
		attributes.addFlashAttribute("msgPageSaved", "Se ha guardado la página correctamente.");
		status.setComplete();
		return "redirect:/";
	}
	
	@GetMapping("/generalInfo")
	public String basicInfoFormGet(Model model) {
		model.addAttribute("pagina", new Pagina());
		return "appForm";
	}
	
	@GetMapping("/create/{id}/index")
	public String createIndexGet(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		List<Noticia> noticias = noticiaRepository.findTop3ByPaginaOrderByIdDesc(page);
		
		if (page != null) {
			
			TreeSet<Ensenanza> ensenanzasOrden = new TreeSet<Ensenanza>(new EnsenanzaServiceImpl());
			for (Ensenanza ensenanza : page.getEnsenanzas()) {
				ensenanzasOrden.add(ensenanza);
			}
			
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					model.addAttribute("ensenanzasOrden", ensenanzasOrden);
					model.addAttribute("noticiaNew", new Noticia());
					model.addAttribute("noticiasIndex", noticias);
					return "appIndexForm";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}
	
	@GetMapping("/create/{id}/oferta_educativa")
	public String createOfEdGet(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			
			TreeSet<Ensenanza> ensenanzasOrden = new TreeSet<Ensenanza>(new EnsenanzaServiceImpl());
			for (Ensenanza ensenanza : page.getEnsenanzas()) {
				ensenanzasOrden.add(ensenanza);
			}
			
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					model.addAttribute("ensenanzasOrden", ensenanzasOrden);
					return "appOfertaEducativaForm";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}
	
	@GetMapping("/create/{id}/noticias")
	public String createNoticiasGet(@RequestParam(name="page", defaultValue="0") int newsPage, @PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			
			Pageable pageRequest = PageRequest.of(newsPage, 4);
			Page<Noticia> noticias = noticiaService.findAllByPagina(page, pageRequest);
			PageRender<Noticia> pageRender = new PageRender<Noticia>("/app/create/" + id + "/noticias", noticias);
			
			TreeSet<Ensenanza> ensenanzasOrden = new TreeSet<Ensenanza>(new EnsenanzaServiceImpl());
			for (Ensenanza ensenanza : page.getEnsenanzas()) {
				ensenanzasOrden.add(ensenanza);
			}
			
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					model.addAttribute("ensenanzasOrden", ensenanzasOrden);
					model.addAttribute("noticiasSeccion", noticias);
					model.addAttribute("pageRender", pageRender);
					return "appNoticiasForm";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}
	
	//Posible implantación a corto plazo
	/*@GetMapping("/create/{id}/secretaria")
	public String createSecretariaGet(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					return "appSecretariaForm";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}*/
	
	@GetMapping("/create/{id}/calendario_escolar")
	public String createCalendarioGet(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					return "appCalendarioEscolarForm";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}
	
	@GetMapping("/create/{id}/contacto")
	public String createContactGet(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					return "appContactoForm";
			}else {
				attributes.addFlashAttribute("msgPageNotMine", "No eres dueño de la página solicitada.");
				return "redirect:/";
			}
		}else {
			attributes.addFlashAttribute("msgPageNotFound", "La página buscada no existe.");
			return "redirect:/";
		}
	}

	@PostMapping("/create")
    public String indexPost(@Valid Pagina pagina, BindingResult bindingResult, Model model,
    		@RequestParam("file") MultipartFile picture,
    		@RequestParam("numero") String numero,
    		@RequestParam("codigoPostal") String codigoPostal,
    		
    		@RequestParam(value = "ens_infantil", required = false) String ens_infantil,
    		@RequestParam(value = "ens_primaria", required = false) String ens_primaria,
    		@RequestParam(value = "ens_secundaria", required = false) String ens_secundaria,
    		@RequestParam(value = "ens_bachillerato", required = false) String ens_bachillerato,
    		@RequestParam(value = "ens_fprofesional", required = false) String ens_fprofesional,
    		
    		@RequestParam(value = "paleta", required = false) String paletaForm,
    		RedirectAttributes attributes, SessionStatus status, Principal principal) {
		
		if (bindingResult.hasErrors()) {
        	System.err.println("Error en la validacion " 
        			+ bindingResult.getAllErrors());
        	List<ObjectError> errores = bindingResult.getAllErrors();
        	FieldError tester = null;
        	
        	for (int i = 0; i<errores.size(); i++) {
	        	if (errores.get(i) instanceof FieldError) {
	                tester = (FieldError) errores.get(i);
	                if (!tester.getField().equalsIgnoreCase("paleta")) {
	                	model.addAttribute("messageError", "Error: " + errores.get(i).getDefaultMessage()); 
	                	return "appForm";
	                }
	            }
        	}
        }
		
		if (pagina.getTitulo().isEmpty()) {
			model.addAttribute("messageError", "Error: El titulo no puede ser nulo.");
    		return "appForm";
		}else if (pagina.getNombre().isEmpty()) {
			model.addAttribute("messageError", "Error: El nombre no puede ser nulo.");
    		return "appForm";
		}else if (pagina.getCalle().isEmpty()) {
			model.addAttribute("messageError", "Error: La calle no puede ser nula.");
    		return "appForm";
		}else if (pagina.getLocalidad().isEmpty()) {
			model.addAttribute("messageError", "Error: La localidad no puede ser nula.");
    		return "appForm";
		}else if (pagina.getTelefonoContacto().isEmpty()) {
			model.addAttribute("messageError", "Error: El teléfono no puede ser nulo.");
    		return "appForm";
		}else if (pagina.getEmailContacto().isEmpty()) {
			model.addAttribute("messageError", "Error: El email de contacto no puede ser nulo.");
    		return "appForm";
		}
		
		pagina.setUser(userRepository.findByUsername(principal.getName()));
		
		if (numero.equalsIgnoreCase("") || numero.contains("-")) {
			pagina.setNumero("S/N");
		}
		
		if (codigoPostal.equalsIgnoreCase("") || codigoPostal.contains("-")) {
			pagina.setCodigoPostal("");
		}
		
		Ensenanza ensInfantil = ensenanzaRepository.findByEnsenanza("EDUCACION INFANTIL");
		Ensenanza ensPrimaria = ensenanzaRepository.findByEnsenanza("EDUCACION PRIMARIA");
		Ensenanza ensSecundaria = ensenanzaRepository.findByEnsenanza("EDUCACION SECUNDARIA");
		Ensenanza ensBachillerato = ensenanzaRepository.findByEnsenanza("BACHILLERATO");
		Ensenanza ensFProfesional = ensenanzaRepository.findByEnsenanza("FORMACION PROFESIONAL");

        ArrayList<Ensenanza> ensenanzas = new ArrayList<Ensenanza>();
        
        if (ens_infantil!=null) {
        	ensenanzas.add(ensInfantil);
		}
        
        if (ens_primaria!=null) {
        	ensenanzas.add(ensPrimaria);
		}
        
        if (ens_secundaria!=null) {
        	ensenanzas.add(ensSecundaria);
		}
        
        if (ens_bachillerato!=null) {
        	ensenanzas.add(ensBachillerato);
		}
        
        if (ens_fprofesional!=null) {
        	ensenanzas.add(ensFProfesional);
		}
        
        if (ensenanzas.size()==0) {
        	model.addAttribute("messageError", "Error: Escoge al menos una enseñanza.");
    		return "appForm";
		}
        
        pagina.setEnsenanzas(new HashSet<Ensenanza>(ensenanzas));
		
		Paleta paleta = new Paleta();
		
		if (paletaForm != null) {
			if (paletaForm.equalsIgnoreCase("rojo")) {
	        	paleta = paletaRepository.findByNombre("rojo");
			}
			
			if (paletaForm.equalsIgnoreCase("naranja")) {
	        	paleta = paletaRepository.findByNombre("naranja");
			}
			
			if (paletaForm.equalsIgnoreCase("amarillo")) {
	        	paleta = paletaRepository.findByNombre("amarillo");
			}
			
			if (paletaForm.equalsIgnoreCase("verde")) {
	        	paleta = paletaRepository.findByNombre("verde");
			}
			
			if (paletaForm.equalsIgnoreCase("teal")) {
	        	paleta = paletaRepository.findByNombre("teal");
			}
			
			if (paletaForm.equalsIgnoreCase("azul")) {
	        	paleta = paletaRepository.findByNombre("azulIndigo");
			}
			
			if (paletaForm.equalsIgnoreCase("morado")) {
	        	paleta = paletaRepository.findByNombre("morado");
			}
			
			if (paletaForm.equalsIgnoreCase("negro")) {
	        	paleta = paletaRepository.findByNombre("negro");
			}
		}else {
			model.addAttribute("messageError", "Error: Escoge una paleta de colores.");
    		return "appForm";
		}
		
		pagina.setPaleta(paleta);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		pagina.setFechaModificacion(dateFormat.format(date));
		pagina.setEnCarrito(false);
		pagina.setComprado(false);
		
		if(!picture.isEmpty()) {
			String uniqueFileName = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);

			try {
				Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());

				attributes.addFlashAttribute("info", "Se ha subido la imagen correctamente.");
	
				pagina.setPicture(uniqueFileName);
				System.out.println(pagina.getPicture());
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			pagina.setPicture("WizclassLogo.png");
		}
		
		paginaRepository.save(pagina);
		System.out.println("Pagina guardada: " + pagina);
		
		status.setComplete();
		
		model.addAttribute("pagina", pagina);
		return "redirect:/app/create/"+ pagina.getId() + "/index";
    }
	
	@GetMapping("/updatePage/{id}")
	public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (page != null) {
			if ((page.getUser().getId() == currentUser.getId()) || (currentUser.getRoles().contains(admin))) {
					model.addAttribute("pagina", page);
					System.out.println("voy a pasar: " + page);
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
	
	@PostMapping("/updatePage/{id}")
    public String pageUpdateFormPost(@Valid Pagina pagina, BindingResult bindingResult, Model model,
    		@RequestParam("file") MultipartFile picture,
    		@RequestParam("numero") String numero,
    		@RequestParam("codigoPostal") String codigoPostal,
    		
    		@RequestParam(value = "ens_infantil", required = false) String ens_infantil,
    		@RequestParam(value = "ens_primaria", required = false) String ens_primaria,
    		@RequestParam(value = "ens_secundaria", required = false) String ens_secundaria,
    		@RequestParam(value = "ens_bachillerato", required = false) String ens_bachillerato,
    		@RequestParam(value = "ens_fprofesional", required = false) String ens_fprofesional,
    		
    		@RequestParam(value = "paleta", required = false) String paletaForm,
    		RedirectAttributes attributes, SessionStatus status) {
		
		if (bindingResult.hasErrors()) {
        	System.err.println("Error en la validacion " 
        			+ bindingResult.getAllErrors());
        	List<ObjectError> errores = bindingResult.getAllErrors();
        	FieldError tester = null;
        	
        	for (int i = 0; i<errores.size(); i++) {
	        	if (errores.get(i) instanceof FieldError) {
	                tester = (FieldError) errores.get(i);
	                if (!tester.getField().equalsIgnoreCase("paleta")) {
	                	attributes.addFlashAttribute("messageError", "Error: " + errores.get(i).getDefaultMessage()); 
	                	return "redirect:/app/updatePage/" + pagina.getId();
	                }
	            }
        	}
        }

		if (pagina.getTitulo().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El titulo no puede ser nulo.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}else if (pagina.getNombre().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El nombre no puede ser nulo.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}else if (pagina.getCalle().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: La calle no puede ser nula.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}else if (pagina.getLocalidad().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: La localidad no puede ser nula.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}else if (pagina.getTelefonoContacto().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El teléfono no puede ser nulo.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}else if (pagina.getEmailContacto().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El email de contacto no puede ser nulo.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}else if (pagina.getUser() == null) {
			attributes.addFlashAttribute("messageError", "Error: Método no permitido. Utiliza el formulario para actualizar la página.");
    		return "redirect:/app/updatePage/" + pagina.getId();
		}
		
		if (numero.equalsIgnoreCase("") || numero.contains("-")) {
			pagina.setNumero("S/N");
		}
		
		if (codigoPostal.equalsIgnoreCase("") || codigoPostal.contains("-")) {
			pagina.setCodigoPostal("");
		}
		
		Ensenanza ensInfantil = ensenanzaRepository.findByEnsenanza("EDUCACION INFANTIL");
		Ensenanza ensPrimaria = ensenanzaRepository.findByEnsenanza("EDUCACION PRIMARIA");
		Ensenanza ensSecundaria = ensenanzaRepository.findByEnsenanza("EDUCACION SECUNDARIA");
		Ensenanza ensBachillerato = ensenanzaRepository.findByEnsenanza("BACHILLERATO");
		Ensenanza ensFProfesional = ensenanzaRepository.findByEnsenanza("FORMACION PROFESIONAL");

        ArrayList<Ensenanza> ensenanzas = new ArrayList<Ensenanza>();
        
        if (ens_infantil!=null) {
        	ensenanzas.add(ensInfantil);
		}
        
        if (ens_primaria!=null) {
        	ensenanzas.add(ensPrimaria);
		}
        
        if (ens_secundaria!=null) {
        	ensenanzas.add(ensSecundaria);
		}
        
        if (ens_bachillerato!=null) {
        	ensenanzas.add(ensBachillerato);
		}
        
        if (ens_fprofesional!=null) {
        	ensenanzas.add(ensFProfesional);
		}
        
        if (ensenanzas.size()==0) {
        	model.addAttribute("messageError", "Error: Escoge al menos una enseñanza.");
    		return "appFormUpdate";
		}
        
        pagina.setEnsenanzas(new HashSet<Ensenanza>(ensenanzas));
		
		Paleta paleta = new Paleta();
		
		if (paletaForm != null) {
			if (paletaForm.equalsIgnoreCase("rojo")) {
	        	paleta = paletaRepository.findByNombre("rojo");
			}
			
			if (paletaForm.equalsIgnoreCase("naranja")) {
	        	paleta = paletaRepository.findByNombre("naranja");
			}
			
			if (paletaForm.equalsIgnoreCase("amarillo")) {
	        	paleta = paletaRepository.findByNombre("amarillo");
			}
			
			if (paletaForm.equalsIgnoreCase("verde")) {
	        	paleta = paletaRepository.findByNombre("verde");
			}
			
			if (paletaForm.equalsIgnoreCase("teal")) {
	        	paleta = paletaRepository.findByNombre("teal");
			}
			
			if (paletaForm.equalsIgnoreCase("azul")) {
	        	paleta = paletaRepository.findByNombre("azulIndigo");
			}
			
			if (paletaForm.equalsIgnoreCase("morado")) {
	        	paleta = paletaRepository.findByNombre("morado");
			}
			
			if (paletaForm.equalsIgnoreCase("negro")) {
	        	paleta = paletaRepository.findByNombre("negro");
			}
		}else {
			model.addAttribute("messageError", "Error: Escoge una paleta de colores.");
    		return "appUpdateForm";
		}
		
		pagina.setPaleta(paleta);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		pagina.setFechaModificacion(dateFormat.format(date));
		
		if(!picture.isEmpty()) {
			String uniqueFileName = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);

			try {
				Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());

				attributes.addFlashAttribute("info", "Se ha subido la imagen correctamente.");
	
				pagina.setPicture(uniqueFileName);
				System.out.println(pagina.getPicture());
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		paginaRepository.save(pagina);
		System.out.println("Pagina id a guardar: " + pagina.getId());
		System.out.println("Pagina guardada: " + pagina);
		
		status.setComplete();
		
		model.addAttribute("pagina", pagina);
		return "redirect:/app/create/"+ pagina.getId() + "/index";
    }
	
	@PostMapping("/addNews/{idPage}")
    public String addNewsPost(@PathVariable("idPage") Long idPage, @Valid Noticia noticia, BindingResult bindingResult, Model model,
    		@RequestParam("file") MultipartFile picture,
    		RedirectAttributes attributes, SessionStatus status) {
		
		if (bindingResult.hasErrors()) {
        	System.err.println("Error en la validacion " 
        			+ bindingResult.getAllErrors());
        	List<ObjectError> errores = bindingResult.getAllErrors();
        	FieldError tester = null;
        	
        	if (errores.get(0) instanceof FieldError) {
                tester = (FieldError) errores.get(0);
        		attributes.addFlashAttribute("messageError", "Error en el " + tester.getField() +  ": " + errores.get(0).getDefaultMessage() + " caracteres.");

            }
        	
        	return "redirect:/app/create/" + idPage + "/index";
        }

		if (noticia.getTitulo().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El titulo de la noticia no puede ser nulo.");
    		return "redirect:/app/create/" + idPage + "/index";
		} else if (noticia.getCuerpo().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El cuerpo de la noticia no puede ser nulo.");
    		return "redirect:/app/create/" + idPage + "/index";
		}
		
		noticia.setPagina(paginaRepository.findById(idPage).orElse(null));
		
		if(!picture.isEmpty()) {
			String uniqueFileName = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);

			try {
				Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());

				attributes.addFlashAttribute("info", "Se ha subido la imagen correctamente.");
	
				noticia.setImagen(uniqueFileName);
				System.out.println(noticia.getImagen());
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			noticia.setImagen("defaultNews.jpg");
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		noticia.setFechaPublicacion(dateFormat.format(date));
		
		noticiaRepository.save(noticia);
		System.out.println("Noticia creada: " + noticia);
		
		attributes.addFlashAttribute("msgNoticiaCreada", "Se ha añadido la noticia con éxito.");
		return "redirect:/app/create/" + idPage + "/index";
	}
	
	@PostMapping("/updateNews/{idNews}")
    public String updateNewsPost(@PathVariable("idNews") Long idNews, @Valid Noticia noticia, BindingResult bindingResult, Model model,
    		@RequestParam("file") MultipartFile picture,
    		@RequestParam(name="newsPage", defaultValue="index") String newsPage,
    		RedirectAttributes attributes, SessionStatus status) {
		
		Noticia noticiaOld = noticiaRepository.findById(idNews).orElse(null);
		Long idPage = noticiaOld.getPagina().getId();
		
		if (bindingResult.hasErrors()) {
        	System.err.println("Error en la validacion " 
        			+ bindingResult.getAllErrors());
        	List<ObjectError> errores = bindingResult.getAllErrors();
        	FieldError tester = null;
        	
        	if (errores.get(0) instanceof FieldError) {
                tester = (FieldError) errores.get(0);
        		attributes.addFlashAttribute("messageError", "Error en actualización del " + tester.getField() +  ": " + errores.get(0).getDefaultMessage() + " caracteres.");

            }
        	if (newsPage.equalsIgnoreCase("noticias")) {
        		return "redirect:/app/create/" + idPage + "/noticias";
			}else {
				return "redirect:/app/create/" + idPage + "/index";
			}
        }

		if (noticia.getTitulo().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El titulo de la noticia no puede ser nulo.");
			if (newsPage.equalsIgnoreCase("noticias")) {
        		return "redirect:/app/create/" + idPage + "/noticias";
			}else {
				return "redirect:/app/create/" + idPage + "/index";
			}
		} else if (noticia.getCuerpo().isEmpty()) {
			attributes.addFlashAttribute("messageError", "Error: El cuerpo de la noticia no puede ser nulo.");
			if (newsPage.equalsIgnoreCase("noticias")) {
        		return "redirect:/app/create/" + idPage + "/noticias";
			}else {
				return "redirect:/app/create/" + idPage + "/index";
			}
		}
		
		noticia.setPagina(paginaRepository.findById(idPage).orElse(null));
		
		if(!picture.isEmpty()) {
			String uniqueFileName = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);

			try {
				Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());

				attributes.addFlashAttribute("info", "Se ha subido la imagen correctamente.");
	
				noticia.setImagen(uniqueFileName);
				System.out.println(noticia.getImagen());
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			noticia.setImagen(noticiaOld.getImagen());
		}
		
		noticia.setFechaPublicacion(noticiaOld.getFechaPublicacion());
		noticia.setId(idNews);
		
		noticiaRepository.save(noticia);
		System.out.println("Noticia actualizada: " + noticia);
		
		attributes.addFlashAttribute("msgNoticiaActualizada", "Se ha actualizado la noticia con éxito.");
		if (newsPage.equalsIgnoreCase("noticias")) {
    		return "redirect:/app/create/" + idPage + "/noticias";
		}else {
			return "redirect:/app/create/" + idPage + "/index";
		}
	}
	
	@GetMapping("/deleteNews/{id}")
	public String deletePage(@PathVariable("id") Long id, RedirectAttributes attributes, Principal principal,
			@RequestParam(name="newsPage", defaultValue="index") String newsPage) {
		
		Noticia noticia = noticiaRepository.findById(id).orElse(null);
		User currentUser = userService.getCurrentuser(principal);
		Role admin = roleRepository.findByRole("ADMIN");
		
		if (noticia != null) {
			User duenno = noticia.getPagina().getUser();
			if (duenno.getId() == currentUser.getId()|| (currentUser.getRoles().contains(admin))) {
				
				if (noticia.getPagina().getNoticias().size() < 2) {
					attributes.addFlashAttribute("messageError", "La página debe contener al menos 3 noticias para poder borrar.");
					if (newsPage.equalsIgnoreCase("noticias")) {
						return "redirect:/app/create/" + noticia.getPagina().getId() + "/noticias";
					}else {
						return "redirect:/app/create/" + noticia.getPagina().getId() + "/index";
					}
				}else {
					noticiaRepository.deleteById(id);
					//System.out.println("NOTICIA: " + noticiaRepository.findById(id));
					attributes.addFlashAttribute("msgDeletedNews", "La noticia se ha borrado correctamente.");
					if (newsPage.equalsIgnoreCase("noticias")) {
						return "redirect:/app/create/" + noticia.getPagina().getId() + "/noticias";
					}else {
						return "redirect:/app/create/" + noticia.getPagina().getId() + "/index";
					}
				}
				
			}
		}
		
		attributes.addFlashAttribute("msgNewsNotMine", "No eres dueño de la noticia solicitada.");
		return "redirect:/";
	}
	
}
