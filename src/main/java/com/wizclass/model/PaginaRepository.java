package com.wizclass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface acts as a repository for the Pagina entity.
 * @author Raul Alvarado
 *
 */
@Repository("paginaRepository")
public interface PaginaRepository extends JpaRepository<Pagina, Long>{

}