package com.wizclass.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("noticiaRepository")
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{
	public List<Noticia> findTop3ByPaginaOrderByIdDesc(Pagina pagina);
	public List<Noticia> findAllByPaginaOrderByIdDesc(Pagina pagina);
}
