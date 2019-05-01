package com.wizclass.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoticiaPagesDao extends PagingAndSortingRepository<Noticia, Long> {
	public Page<Noticia> findAllByPaginaOrderByIdDesc(Pagina pagina, Pageable pageable);
}
