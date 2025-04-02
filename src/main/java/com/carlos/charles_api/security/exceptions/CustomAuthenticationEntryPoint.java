package com.carlos.charles_api.security.exceptions;

import com.carlos.charles_api.controller.exceptionHandler.ResourceExceptionHandler;
import com.carlos.charles_api.controller.exceptionHandler.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    ResourceExceptionHandler exHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResponseEntity<StandardError> error = exHandler.authenticationException(e, request);
        response.setContentType("application/json");
        response.getWriter().write(error.getBody().toString());
    }
}
