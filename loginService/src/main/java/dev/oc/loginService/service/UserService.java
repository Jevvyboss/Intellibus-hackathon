package dev.oc.loginService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.oc.loginService.model.*;
import dev.oc.loginService.repository.UserRepo;
@Service
public class UserService {
	@Autowired
	private UserRepo repo;
	
	public List<User> findAll(){
		return repo.findAll();
		
	}
	
	public Optional<User> findByEmail(String email){
		return repo.findByEmail(email);
	}
	
	public void save(User user) {
		repo.save(user);
	}

}
