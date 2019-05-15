package com.wizclass.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * This interface is used to get the Noticia objects stored in one page.
 * @author Raul Alvarado
 *
 */
public interface NoticiaPagesDao extends PagingAndSortingRepository<Noticia, Long> {
	public Page<Noticia> findAllByPaginaOrderByIdDesc(Pagina pagina, Pageable pageable);
}