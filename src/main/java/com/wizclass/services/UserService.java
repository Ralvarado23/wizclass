package com.wizclass.services;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wizclass.model.Role;
import com.wizclass.model.User;

/**
 * This interface contains auxiliary methods related to the User entity.
 * @author Raul Alvarado
 *
 */
public interface UserService {
	/**
	 * This method is used to find an user in userRepository by an username.
	 * @param username - this parameter indicates the name of the user searched.
	 * @return - this method returns an User object with the name searched.
	 */
	public User findUserByUsername(String username);
	/**
	 * This method is used to find an user in userRepository by an email.
	 * @param email - this parameter indicates the email of the user searched.
	 * @return - this method returns an User object with the email searched.
	 */
	public User findUserByEmail(String email);
	/**
	 * This method is used to encode the user password before saving it in the repository.
	 * @param user - this parameter contains the user to be saved.
	 */
	public void saveUser(User user);
	/**
	 * This method is used to encode the user password before saving it in the repository.
	 * Additionally, the user is given a set of roles.
	 * @param user - this parameter contains the user to be saved.
	 * @param roles - set of roles that are given to the user object.
	 */
	public void saveUserRole(User user, ArrayList<Role> roles);
	/**
	 * This method returns all the users grouped in pages.
	 * @param pageable - this parameter indicates the number in which pages are grouped.
	 * @return - this method returns all the users grouped in pages by the pageable.
	 */
	public Page<User> findAll(Pageable pageable);
	/**
	 * This method gets the user object who is logged.
	 * @param principal - this parameter represents the current logged user
	 * @return - this method returns the user who is logged.
	 */
	public User getCurrentuser(Principal principal);
}