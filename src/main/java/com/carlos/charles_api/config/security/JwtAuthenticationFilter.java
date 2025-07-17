package com.carlos.charles_api.config.security;

import com.carlos.charles_api.exceptions.ResourceNotFoundException;
import com.carlos.charles_api.model.entity.User;
import com.carlos.charles_api.repository.UserRepository;
import com.carlos.charles_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.recoverToken(request);
        if (token != null) {
            String username = tokenService.validateToken(token);
            User user = userRepository.findByEmail(username);

            // Validação
            if (user == null) {
                throw new ResourceNotFoundException("O usuário autenticado não foi encontrado!");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}