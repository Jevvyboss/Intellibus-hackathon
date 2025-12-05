package dev.oc.loginService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.oc.loginService.components.JwtUtil;
import dev.oc.loginService.model.AuthRequest;
import dev.oc.loginService.model.AuthResponse;
import dev.oc.loginService.model.User;
import dev.oc.loginService.service.UserService;

@RestController
@RequestMapping("api/login")
public class LoginController {
	  @Autowired
	  private  AuthenticationManager authenticationManager;
	  
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	    
	  @Autowired
	  private JwtUtil jwtService;
	  
	  @Autowired
	  private UserService service;

	  @PostMapping("")
	  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
		  try {
			  Authentication authentication = authenticationManager.authenticate(
					  new UsernamePasswordAuthenticationToken(
							  authRequest.getEmail(),      // The email from request
	                          authRequest.getPassword()     // The password from request
	                  )
	          );

	          // STEP 2: If authentication successful, create JWT token
	          if (authentication.isAuthenticated()) {
	              String token = jwtService.generateToken(authRequest.getEmail());
	                
	              // STEP 3: Return the token to the user
	              return ResponseEntity.ok(
	                  new AuthResponse(token, "Login successful")
	              );
	          } else {
	              // This shouldn't normally happen, but just in case
	              return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                      .body(new AuthResponse(null, "Authentication failed"));
	          }
	            
	      } catch (BadCredentialsException e) {
	          // STEP 4: If email/password wrong, return error
	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                  .body(new AuthResponse(null, "Invalid email or password"));
	      }
	  }

	    /**
	     * TOKEN VALIDATION ENDPOINT (Optional - for Gateway to use)
	     * GET /api/auth/validate?token=xxx
	     * 
	     * Returns true if token is valid, false otherwise
	     */
	  @GetMapping("/validate")
	  public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
	      try {
	          String email = jwtService.extractEmail(token);
	          boolean isValid = jwtService.validateToken(token, email);
	          return ResponseEntity.ok(isValid);
	      } catch (Exception e) {
	          return ResponseEntity.ok(false);
	      }
	  }
	  
	  @PostMapping("/register")
	    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
	        try {
	            // STEP 1: Check if user already exists
	            if (service.findByEmail(authRequest.getEmail()).isPresent()) {
	                return ResponseEntity.status(HttpStatus.CONFLICT)
	                        .body(new AuthResponse(null, "Email already registered"));
	            }

	            // STEP 2: Create new user
	            User newUser = new User();
	            newUser.setEmail(authRequest.getEmail());
	            
	            // STEP 3: Hash the password (NEVER store plain text!)
	            String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
	            newUser.setPassword(hashedPassword);

	            // STEP 4: Save user to database
	            service.save(newUser);

	            // STEP 5: Return success message
	            return ResponseEntity.status(HttpStatus.CREATED)
	                    .body(new AuthResponse(null, "User registered successfully"));

	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(new AuthResponse(null, "Registration failed: " + e.getMessage()));
	        }
	    }
}