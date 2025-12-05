package dev.oc.loginService.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import dev.oc.loginService.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * SECURITY FILTER CHAIN
     * Configures which endpoints are protected
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for stateless JWT authentication)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            
            // Configure which endpoints need authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login/**", "/api/login").permitAll()  // Login is public
                .anyRequest().authenticated()  // Everything else needs auth
            )
            
            // Stateless session - we don't store sessions, only JWT tokens
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Use our custom authentication provider
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * AUTHENTICATION PROVIDER
     * Tells Spring Security HOW to authenticate users
     * 
     * This connects:
     * - UserDetailsService (loads user from database)
     * - PasswordEncoder (checks if password matches)
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // How to load users from database
        authProvider.setUserDetailsService(userDetailsService);
        
        // How to check passwords (BCrypt hashing)
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    /**
     * AUTHENTICATION MANAGER
     * This is what the AuthController uses to authenticate users
     * 
     * The AuthController calls:
     * authenticationManager.authenticate(username, password)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * PASSWORD ENCODER
     * Uses BCrypt to hash passwords
     * 
     * When storing password: "password123" → "$2a$10$N9qo8uLOickgx2..."
     * When checking: Hashes input and compares with stored hash
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}