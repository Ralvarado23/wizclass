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
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;
import com.wizclass.model.Paleta;
import com.wizclass.model.PaletaRepository;
import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserRepository;
import com.wizclass.model.Ensenanza;
import com.wizclass.model.EnsenanzaRepository;

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
	
//	private PaginaService paginaService;
//	
//	public AppController (PaginaService paginaService) {
//		this.paginaService = paginaService;
//	}
	
	@GetMapping("/savePage")
	public String savePage(RedirectAttributes attributes) {
		attributes.addFlashAttribute("msgPageSaved", "Se ha guardado la página correctamente.");
		return "redirect:/";
	}
	
	@GetMapping("/generalInfo")
	public String basicInfoFormGet(Model model) {
		model.addAttribute("pagina", new Pagina());
		return "appForm";
	}

	@PostMapping("/generalInfo")
    public String basicInfoFormPost(@Valid Pagina pagina, BindingResult bindingResult, Model model,
    		@RequestParam("file") MultipartFile picture,
    		@RequestParam("numero") String numero,
    		
    		@RequestParam(value = "ens_infantil", required = false) String ens_infantil,
    		@RequestParam(value = "ens_primaria", required = false) String ens_primaria,
    		@RequestParam(value = "ens_secundaria", required = false) String ens_secundaria,
    		@RequestParam(value = "ens_bachillerato", required = false) String ens_bachillerato,
    		@RequestParam(value = "ens_fprofesional", required = false) String ens_fprofesional,
    		
    		@RequestParam(value = "paleta", required = false) String paletaForm,
    		RedirectAttributes attributes, SessionStatus status, Principal principal) {
		
		pagina.setUser(userRepository.findByUsername(principal.getName()));
		
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
		
		if (numero.equalsIgnoreCase("") || numero.contains("-")) {
			pagina.setNumero("S/N");
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
		
		paginaRepository.save(pagina);
		System.out.println("Pagina guardada: " + pagina);
		
		status.setComplete();
		
		model.addAttribute("pagina", pagina);
		return "appIndexForm";
    }
	
	@GetMapping("/updatePage/{id}")
	public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes, Principal principal) {
		
		Pagina page = paginaRepository.findById(id).orElse(null);
		User currentUser = userRepository.findByUsername(principal.getName());
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
    		
    		@RequestParam(value = "ens_infantil", required = false) String ens_infantil,
    		@RequestParam(value = "ens_primaria", required = false) String ens_primaria,
    		@RequestParam(value = "ens_secundaria", required = false) String ens_secundaria,
    		@RequestParam(value = "ens_bachillerato", required = false) String ens_bachillerato,
    		@RequestParam(value = "ens_fprofesional", required = false) String ens_fprofesional,
    		
    		@RequestParam(value = "paleta", required = false) String paletaForm,
    		RedirectAttributes attributes, SessionStatus status) {
		
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
		
		if (numero.equalsIgnoreCase("") || numero.contains("-")) {
			pagina.setNumero("S/N");
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
		
		paginaRepository.save(pagina);
		System.out.println("Pagina id a guardar: " + pagina.getId());
		System.out.println("Pagina guardada: " + pagina);
		
		status.setComplete();
		
		model.addAttribute("pagina", pagina);
		return "appIndexForm";
    }
}
