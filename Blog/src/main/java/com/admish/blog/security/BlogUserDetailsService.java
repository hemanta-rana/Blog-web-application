package com.admish.blog.security;

import com.admish.blog.domain.entities.User;
import com.admish.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

      User user =   userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("username not found with email "+email)) ;

      return new BlogUserDetails(user);


    }
}
