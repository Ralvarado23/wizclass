package com.wizclass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("noticiaRepository")
public interface NoticiaRepository extends JpaRepository<Noticia, Long>{
}
