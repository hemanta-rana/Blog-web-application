package com.admish.blog.controllers;

import com.admish.blog.domain.dtos.AuthResponse;
import com.admish.blog.domain.dtos.LoginRequest;
import com.admish.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest loginRequest){
       UserDetails userDetails =  authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

       String tokenValue = authenticationService.generateToken(userDetails);

       AuthResponse authResponse = AuthResponse.builder()
               .token(tokenValue)
               .expireIn(86400)
               .build();

       return ResponseEntity.ok(authResponse);

    }
}
