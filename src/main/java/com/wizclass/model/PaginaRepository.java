package com.wizclass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("paginaRepository")
public interface PaginaRepository extends JpaRepository<Pagina, Long>{

}
