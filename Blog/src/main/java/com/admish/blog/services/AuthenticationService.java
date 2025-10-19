package com.admish.blog.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    /**
     * Authenticate a user with email and password.
     * @return UserDetails if authentication is successful.
     */
    UserDetails authenticate(String email, String password);

    /**
     * Register a new user.
     * @return UserDetails of the created user.
     */
    UserDetails registerUser(String email, String password, String name);

    /**
     * Generate JWT token for the given user.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Validate JWT token and return UserDetails.
     */
    UserDetails validateUser(String token);

    /**
     * Return token expiry in seconds (configurable).
     */
    long getTokenExpiry();
}
