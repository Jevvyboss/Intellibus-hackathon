package com.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.model.User;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public List<User> getAll() {
		return userService.findAll();
	}
	
	@PostMapping("")
	public Optional<User> authUser(@RequestBody User user) {
		Optional<User> userTest = userService.findByEmail(user.getEmail());
		
		if(userTest.isPresent()) {
			User userFound = userTest.get();
			if(user.getPassword().equals(user.getPassword())) {
				return userTest;
			}
			
		}
		
		return null;
		
	}
	
	@PostMapping("/newuser")
	public void addUser(@RequestBody User user) {
		userService.addUser(user);
	}

}
