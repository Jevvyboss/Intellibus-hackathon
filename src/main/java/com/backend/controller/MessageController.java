package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.model.Message;
import com.backend.service.ExternalApiService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
	@Autowired
	private ExternalApiService eas;

	
	@PostMapping("/{mess}")
	public ResponseEntity<String> sendMessage(@RequestParam Message mess) {
		String message = eas.addMessage(mess);
		return ResponseEntity.ok(message);
	}

}
