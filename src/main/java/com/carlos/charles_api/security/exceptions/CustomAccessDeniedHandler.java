package com.carlos.charles_api.security.exceptions;

import com.carlos.charles_api.controller.exceptionHandler.ResourceExceptionHandler;
import com.carlos.charles_api.controller.exceptionHandler.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.StandardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    ResourceExceptionHandler exHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        ResponseEntity<StandardError> error = exHandler.accessDeniedException(e, request);
        response.setContentType("application/json");
        response.getWriter().write(error.getBody().toString());
    }
}
