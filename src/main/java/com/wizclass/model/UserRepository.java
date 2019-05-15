package com.wizclass.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface acts as a repository for the User entity.
 * @author Raul Alvarado
 *
 */
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByUsername(String username);
	 User findByEmail(String email);
}