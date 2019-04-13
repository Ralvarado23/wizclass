package com.wizclass.services;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.wizclass.model.Ensenanza;
import com.wizclass.model.Pagina;
import com.wizclass.model.PaginaRepository;

public class PaginaServiceImpl implements PaginaService{
	
	@Autowired
	PaginaRepository paginaRepository;
	
	@Override
	public void savePaginaEnsenanza(Pagina pagina, ArrayList<Ensenanza> ensenanzas) {
        pagina.setEnsenanzas(new HashSet<Ensenanza>(ensenanzas));
        
        paginaRepository.save(pagina);
        System.out.println("Pagina guardada: " + pagina);
	}
}
