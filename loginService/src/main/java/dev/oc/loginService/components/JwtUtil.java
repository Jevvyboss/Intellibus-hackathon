package dev.oc.loginService.components;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

    // Reads from application.yml: jwt.secret
    @Value("${jwt.secret}")
    private String secret;

    // Reads from application.yml: jwt.expiration (in milliseconds)
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * MAIN METHOD: Creates a JWT token for a user
     * Called when user successfully logs in
     * 
     * @param email - The user's email (used as identifier)
     * @return A JWT token string
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();        
        return createToken(claims, email);
    }

    /**
     * Actually builds the JWT token
     * 
     * @param claims - Extra data to store in token (like roles)
     * @param subject - The user identifier (email in our case)
     * @return The complete JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)  // Custom data
                .setSubject(subject)  // User's email
                .setIssuedAt(new Date(System.currentTimeMillis()))  // When token was created
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // When it expires
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // Sign with secret key
                .compact();  // Build the final token string
    }

    /**
     * Extracts the email from a token
     * 
     * @param token - The JWT token
     * @return The user's email
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts the expiration date from a token
     * 
     * @param token - The JWT token
     * @return When the token expires
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Opens up the token and reads all data inside
     * 
     * @param token - The JWT token
     * @return All the claims (data) stored in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())  // Use secret to verify signature
                .build()
                .parseClaimsJws(token)  // Parse and validate the token
                .getBody();  // Get the data inside
    }

    /**
     * Checks if a token has expired
     * 
     * @param token - The JWT token
     * @return true if expired, false if still valid
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates a token is correct and not expired
     * 
     * @param token - The JWT token
     * @param email - The expected user email
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    /**
     * Converts the secret string into a cryptographic key
     * Used for signing and verifying tokens
     */
    private Key getSignKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}