package com.wizclass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.lang.String;
import com.wizclass.model.Paleta;

/**
 * This interface acts as a repository for the Paleta entity.
 * @author Raul Alvarado
 *
 */
@Repository("paletaRepository")
public interface PaletaRepository extends JpaRepository<Paleta, Long> {
	Paleta findByNombre(String nombre);
}