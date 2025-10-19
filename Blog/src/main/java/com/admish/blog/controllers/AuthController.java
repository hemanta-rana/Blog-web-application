package com.admish.blog.controllers;

import com.admish.blog.domain.dtos.AuthResponse;
import com.admish.blog.domain.dtos.LoginRequest;
import com.admish.blog.domain.dtos.RegisterRequest;
import com.admish.blog.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        try {
            UserDetails userDetails = authenticationService.authenticate(
                    loginRequest.getEmail(), loginRequest.getPassword()
            );

            String token = authenticationService.generateToken(userDetails);

            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .token(token)
                            .expireIn(authenticationService.getTokenExpiry()) // configurable
                            .build()
            );
        } catch (Exception e) {
            // Global exception handler is better; this is fallback
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .token(null)
                            .expireIn(0)
                            .build());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid RegisterRequest request){
        UserDetails userDetails = authenticationService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );

        String token = authenticationService.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthResponse.builder()
                        .token(token)
                        .expireIn(authenticationService.getTokenExpiry())
                        .build());
    }
}

