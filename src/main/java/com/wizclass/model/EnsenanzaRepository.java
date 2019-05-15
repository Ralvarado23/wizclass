package com.wizclass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.lang.String;
import com.wizclass.model.Ensenanza;

/**
 * This interface acts as a repository for the Enseñanza entity.
 * @author Raul Alvarado
 *
 */
@Repository("ensenanzaRepository")
public interface EnsenanzaRepository extends JpaRepository<Ensenanza, Long>{
	Ensenanza findByEnsenanza(String ensenanza);
}