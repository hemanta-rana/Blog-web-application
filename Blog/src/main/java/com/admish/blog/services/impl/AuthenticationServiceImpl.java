package com.admish.blog.services.impl;


import com.admish.blog.domain.entities.User;
import com.admish.blog.exception.EmailAlreadyInUseException;
import com.admish.blog.exception.UserAlreadyExistsException;
import com.admish.blog.repository.UserRepository;
import com.admish.blog.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

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
        return  Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()))
                .setIssuedAt(new Date(System.currentTimeMillis()+ jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserDetails validateUser(String token) {
        String username = extractUsername(token);
       return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public UserDetails registerUser(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()){
            throw new EmailAlreadyInUseException("email already exists ! ");
        }
        if (userRepository.findByName(name).isPresent()){
            throw new UserAlreadyExistsException("user already exist with name "+name);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);

       User savedUser=  userRepository.save(user);

       return userDetailsService.loadUserByUsername(savedUser.getEmail());
    }

    private String extractUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
