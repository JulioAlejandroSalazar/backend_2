package com.letrasypapeles.backend.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        user = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    void testGetToken_NotNullAndContainsUsername() {
        String token = jwtService.getToken(user);
        assertNotNull(token);

        // El username extraído debe ser igual al del usuario
        String username = jwtService.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testIsTokenValid_ReturnsTrueForValidToken() {
        String token = jwtService.getToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void testIsTokenValid_ReturnsFalseForDifferentUser() {
        String token = jwtService.getToken(user);
        UserDetails otherUser = new User("otheruser", "password", Collections.emptyList());
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void testIsTokenExpired_ReturnsTrueIfExpired() throws InterruptedException {
        // Crear un JwtService que genera tokens que expiran rápido (1 ms)
        JwtService shortLivedJwtService = new JwtService() {
            @Override
            public String getToken(UserDetails user) {
                return io.jsonwebtoken.Jwts.builder()
                        .setClaims(new java.util.HashMap<>())
                        .setSubject(user.getUsername())
                        .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                        .setExpiration(new java.util.Date(System.currentTimeMillis() + 1)) // expira muy rápido
                        .signWith(getKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .compact();
            }
        };

        String token = shortLivedJwtService.getToken(user);

        Thread.sleep(5); // esperar que expire

        assertTrue(shortLivedJwtService.isTokenExpired(token));
    }

    @Test
    void testGetClaim_ReturnsCorrectClaim() {
        String token = jwtService.getToken(user);
        String subject = jwtService.getClaim(token, Claims::getSubject);
        assertEquals("testuser", subject);
    }

    @Test
    void testGetAllClaims_ReturnsClaimsWithCorrectSubject() {
        String token = jwtService.getToken(user);
        Claims claims = jwtService.getAllClaims(token);
        assertEquals("testuser", claims.getSubject());
    }
}
