package com.wizclass.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wizclass.model.Noticia;
import com.wizclass.model.Pagina;

/**
 * This interface contains auxiliary methods related to the Noticia entity.
 * @author Raul Alvarado
 *
 */
public interface NoticiaService {
	/**
	 * This method returns all the news (noticia objects) stored in a page (pagina object).
	 * @param page - this parameter represents the page which news are obtained.
	 * @param pageable - this parameter represents the way in which pages are grouped.
	 * @return this method return the news stored in a page.
	 */
	public Page<Noticia> findAllByPagina(Pagina page, Pageable pageable);
}