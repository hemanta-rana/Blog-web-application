package com.admish.blog.security;

import com.admish.blog.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractToken(request);

            if (token != null) {
                UserDetails userDetails = authenticationService.validateUser(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()

                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                if (userDetails instanceof BlogUserDetails) {
                    request.setAttribute("userId", ((BlogUserDetails) userDetails).getId());
                }
            }
        }catch (Exception ex){
            // do not throw exception
            log.error("received invalid auth token ");
        }
        filterChain.doFilter(request, response);

    }

    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken!= null && bearerToken.startsWith("Bearer 1")){
            return bearerToken.substring(7);
        }

        return null;
    }
}
