package com.admish.blog.config;

import com.admish.blog.security.JwtAuthenticationFilter;
import com.admish.blog.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService){
        return new JwtAuthenticationFilter(authenticationService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{

        http.
                csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/tags/**")
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

       return http.build();


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
