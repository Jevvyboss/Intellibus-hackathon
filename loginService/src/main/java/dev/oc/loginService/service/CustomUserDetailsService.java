package dev.oc.loginService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.oc.loginService.model.User;
import dev.oc.loginService.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    /**
     * Spring Security calls this method during authentication
     * 
     * @param email - The email the user entered (we use email as username)
     * @return UserDetails object that Spring Security understands
     * @throws UsernameNotFoundException if user doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // STEP 1: Find user in database by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // STEP 2: Convert our User entity to Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())  // Use email as username
                .password(user.getPassword())  // The HASHED password from database
                .roles("USER")  // You can change this if you have roles
                .build();
    }
}