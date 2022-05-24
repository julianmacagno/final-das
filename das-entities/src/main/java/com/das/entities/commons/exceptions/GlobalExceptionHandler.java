package com.das.entities.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntitiesException.class)
    public ResponseEntity<Object> dasEntityException(EntitiesException exception) {
        HttpStatus status;
        switch (exception.getIssueType()) {
            case NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                break;
            case BAD_REQUEST:
                status = HttpStatus.BAD_REQUEST;
                break;
            case UNAUTHORIZED:
                status = HttpStatus.UNAUTHORIZED;
                break;
            case CONFLICT:
                status = HttpStatus.CONFLICT;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return new ResponseEntity<>(exception.getMessage(), status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolation(ConstraintViolationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
