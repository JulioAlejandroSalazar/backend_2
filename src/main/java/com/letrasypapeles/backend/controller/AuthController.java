package com.letrasypapeles.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.letrasypapeles.backend.entity.AuthResponseJWT;
import com.letrasypapeles.backend.entity.LoginRequest;
import com.letrasypapeles.backend.entity.RegisterRequest;
import com.letrasypapeles.backend.service.AuthServiceJWT;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceJWT authServiceJWT;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseJWT> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authServiceJWT.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseJWT> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authServiceJWT.register(request));
    }

}
