package com.wizclass.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wizclass.model.Noticia;
import com.wizclass.model.Pagina;

public interface NoticiaService {
	public Page<Noticia> findAllByPagina(Pagina page, Pageable pageable);
}
