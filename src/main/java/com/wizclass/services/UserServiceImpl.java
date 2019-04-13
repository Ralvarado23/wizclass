package com.wizclass.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wizclass.model.Role;
import com.wizclass.model.RoleRepository;
import com.wizclass.model.User;
import com.wizclass.model.UserPagesDao;
import com.wizclass.model.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserPagesDao pagesManager;
	@Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Page<User> findAll(Pageable pageable) {
		return pagesManager.findAll(pageable);
	}
    
	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        
        Role userUserRole = roleRepository.findByRole("USER");
        
        user.setRoles(new HashSet<Role>(Arrays.asList(userUserRole)));
        
        System.out.println("Usuario guardado: " + user);
		userRepository.save(user);
	}
	
	@Override
	public void saveUserRole(User user, ArrayList<Role> roles) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        
        user.setRoles(new HashSet<Role>(roles));
        
        System.out.println("Usuario guardado: " + user);
		userRepository.save(user);
	}
}