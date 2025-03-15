package com.carlos.charles_api.controller.exceptionHandler;

import com.carlos.charles_api.service.exceptions.DatabaseException;
import com.carlos.charles_api.service.exceptions.EmailException;
import com.carlos.charles_api.service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//TRATAMENTO GERAL DE EXCEÇÕES
@ControllerAdvice
public class ResourceExceptionHandler {

    // RECURSO NÃO ENCONTRADO
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(LocalDateTime.now(), status.value(), "Recurso não encontrado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // BANCO DE DADOS
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(LocalDateTime.now(), status.value(), "Erro de banco de dados", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // EMAIL
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<StandardError> database(EmailException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(LocalDateTime.now(), status.value(), "Erro de e-mail", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // DADOS INVÁLIDOS
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {

        ValidationError err = new ValidationError();
        err.setTimeStamp(LocalDateTime.now());
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
        StandardError err = new StandardError(LocalDateTime.now(), status.value(), "Erro interno: "+e.getClass().getSimpleName(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}

class ValidationError extends StandardError {

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError() {
        super();
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }
}

@Data
@AllArgsConstructor
class FieldMessage {
    private String fieldName;
    private String message;
}

