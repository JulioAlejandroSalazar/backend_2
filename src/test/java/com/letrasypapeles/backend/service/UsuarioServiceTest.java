package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UsuarioService usuarioService;

    @Test
    void loadUserByUsername_existingUser_returnsUserDetails() {
        String username = "juan";
        User mockUser = User.builder()
                .username(username)
                .password("password123")
                .role(Role.ADMIN)  // usando el enum Role
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = usuarioService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_nonExistingUser_throwsUsernameNotFoundException() {
        String username = "noExiste";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> usuarioService.loadUserByUsername(username));

        assertTrue(exception.getMessage().contains(username));
        verify(userRepository, times(1)).findByUsername(username);
    }
}
