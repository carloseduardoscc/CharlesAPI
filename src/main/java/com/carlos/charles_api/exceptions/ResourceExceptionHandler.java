package com.carlos.charles_api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    // RECURSO NÃO ENCONTRADO
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status.value(), "Recurso não encontrado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // AUTENTICAÇÃO
    @ExceptionHandler(com.carlos.charles_api.exceptions.AuthenticationException.class)
    public ResponseEntity<StandardError> authenticationException(AuthenticationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(status.value(), "Problema com autenticação", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // AUTORIZAÇÃO
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorizationException(AuthorizationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(status.value(), "Acesso negado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // REGRA DE NEGÓCIO
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> businessRuleException(BusinessRuleException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(status.value(), "Regra de negócio violada", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // BANCO DE DADOS
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(status.value(), "Problema com banco de dados", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // EMAIL
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<StandardError> emailException(EmailException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(status.value(), "Problema com e-mail", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // ENTRADA DE DADOS INVÁLIDA
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {

        ValidationError err = new ValidationError();
        err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        err.setError("Erro de validação");
        err.setMessage("Um ou mais campos estão inválidos");
        err.setPath(request.getRequestURI());

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            err.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // QUALQUER OUTRA EXCEÇÃO
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(status.value(), "Erro interno: " + e.getClass().getSimpleName(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}

