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
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody @Valid LoginRequest loginRequest){
       UserDetails userDetails =  authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

       String tokenValue = authenticationService.generateToken(userDetails);

       AuthResponse authResponse = AuthResponse.builder()
               .token(tokenValue)
               .expireIn(86400)
               .build();

       return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid RegisterRequest request){
       UserDetails userDetails  =  authenticationService.registerUser(request.getEmail(), request.getPassword(), request.getName());

       String tokenValue = authenticationService.generateToken(userDetails);

       AuthResponse authResponse = AuthResponse.builder()
               .token(tokenValue)
               .expireIn(86400)
               .build();

       return new ResponseEntity<>(
              authResponse,
               HttpStatus.CREATED
       );
    }
}
