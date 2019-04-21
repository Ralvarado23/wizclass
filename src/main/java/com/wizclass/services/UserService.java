package com.wizclass.services;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wizclass.model.Role;
import com.wizclass.model.User;

public interface UserService {
	public User findUserByUsername(String username);
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void saveUserRole(User user, ArrayList<Role> roles);
	public Page<User> findAll(Pageable pageable);
	public User getCurrentuser(Principal principal);
}