package com.wizclass.services;

import java.util.ArrayList;

import com.wizclass.model.Ensenanza;
import com.wizclass.model.Pagina;

public interface PaginaService {
	public void savePaginaEnsenanza(Pagina pagina, ArrayList<Ensenanza> ensenanzas);
}
