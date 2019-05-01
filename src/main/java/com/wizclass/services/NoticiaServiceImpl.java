package com.wizclass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wizclass.model.Noticia;
import com.wizclass.model.NoticiaPagesDao;
import com.wizclass.model.Pagina;

@Service
public class NoticiaServiceImpl implements NoticiaService{

	@Autowired
	private NoticiaPagesDao pagesManager;
	
	@Override
	public Page<Noticia> findAllByPagina(Pagina page, Pageable pageable) {
		return pagesManager.findAllByPaginaOrderByIdDesc(page, pageable);
	}
}