package com.wizclass.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface acts as a repository for the Noticia entity.
 * @author Raul Alvarado
 *
 */
@Repository("noticiaRepository")
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{
	public List<Noticia> findTop3ByPaginaOrderByIdDesc(Pagina pagina);
	public List<Noticia> findAllByPaginaOrderByIdDesc(Pagina pagina);
}