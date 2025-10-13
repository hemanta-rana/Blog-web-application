package com.admish.blog.services.impl;


import com.admish.blog.services.AuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl  implements AuthenticationService {

   private final AuthenticationManager authenticationManager;
   private final UserDetailsService userDetailsService;

   @Value("${jwt.secret}")
   private  String secretKey;

   private static final Long jwtExpirationMs = 8640000L;

    @Override
    public UserDetails authenticate(String email, String password) {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               email,
               password
       ));
       return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()))
                .setIssuedAt(new Date(System.currentTimeMillis()+ jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return "";
    }

    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
