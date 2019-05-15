package com.wizclass.services;

import java.util.ArrayList;

import com.wizclass.model.Ensenanza;
import com.wizclass.model.Pagina;

/**
 * This interface contains auxiliary methods related to the Pagina entity.
 * @author Raul Alvarado
 *
 */
public interface PaginaService {
	/**
	 * This method sets the enseñanzas in an object of type Pagina.
	 * @param pagina - this parameter contains the page which is added Enseñanzas
	 * @param ensenanzas - this parameter contains all the objects of type Enseñanza to be added to pagina
	 */
	public void savePaginaEnsenanza(Pagina pagina, ArrayList<Ensenanza> ensenanzas);
}