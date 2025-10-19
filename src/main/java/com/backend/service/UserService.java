package com.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.User;
import com.backend.repository.UserRepository;

@Service
public class UserService {	
	@Autowired
	private UserRepository userRepo;
	
	//all the users in db
	public List<User> findAll(){
		return userRepo.findAll();
	}
	
	public void deleteUser(User user) {
		userRepo.delete(user);
	}
	
	public void addUser(User user) {
		userRepo.save(user);
		
	}
	
	public Optional<User> findByEmail(String email) {
		return userRepo.findByEmail(email);
		
	}
	
	
	

}
