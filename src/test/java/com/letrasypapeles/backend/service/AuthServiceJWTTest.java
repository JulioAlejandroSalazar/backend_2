package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.AuthResponseJWT;
import com.letrasypapeles.backend.entity.LoginRequest;
import com.letrasypapeles.backend.entity.RegisterRequest;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.UserRepository;
import com.letrasypapeles.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceJWTTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceJWT authServiceJWT;

    @Test
    void login_validCredentials_returnsAuthResponse() {
        String username = "usuario";
        String password = "password";
        String token = "jwt-token";

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mock(org.springframework.security.core.Authentication.class));

        User user = User.builder()
                .username(username)
                .password(password)
                .role(Role.CLIENTE)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn(token);

        AuthResponseJWT response = authServiceJWT.login(request);

        assertNotNull(response);
        assertEquals(token, response.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtService, times(1)).getToken(user);
    }


    @Test
    void register_validRequest_returnsAuthResponseAndSavesUser() {
        String username = "nuevoUsuario";
        String passwordRaw = "123456";
        String encodedPassword = "encodedPassword";
        String token = "jwt-token";

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(passwordRaw);
        request.setFirstname("Nombre");
        request.setLastname("Apellido");

        when(passEncoder.encode(passwordRaw)).thenReturn(encodedPassword);

        // Capturar usuario guardado
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        when(jwtService.getToken(any(User.class))).thenReturn(token);

        AuthResponseJWT response = authServiceJWT.register(request);

        assertNotNull(response);
        assertEquals(token, response.getToken());

        User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(Role.CLIENTE, savedUser.getRole());
        assertEquals("Nombre", savedUser.getFirstname());
        assertEquals("Apellido", savedUser.getLastname());

        verify(passEncoder, times(1)).encode(passwordRaw);
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).getToken(any(User.class));
    }
}
