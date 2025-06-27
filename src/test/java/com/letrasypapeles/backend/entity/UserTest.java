package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserDetailsImplementation() {
        Role role = Role.ADMIN;
        User user = User.builder()
                .id(1)
                .username("testuser")
                .firstname("Test")
                .lastname("User")
                .password("password123")
                .role(role)
                .build();

        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());

        assertEquals("Test", user.getFirstname());
        assertEquals("User", user.getLastname());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("ROLE_" + role.name(), authority.getAuthority());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = User.builder().id(1).username("u1").build();
        User user2 = User.builder().id(1).username("u1").build();
        User user3 = User.builder().id(2).username("u2").build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
