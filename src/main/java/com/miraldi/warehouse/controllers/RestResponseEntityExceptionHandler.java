package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.services.AccessDeniedException;
import com.miraldi.warehouse.services.IllegalArgumentException;
import com.miraldi.warehouse.services.IncorrectDataException;
import com.miraldi.warehouse.services.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>("ResourceNotFoundException: " + ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("IllegalArgumentException: " +
                ex.getMessage(),
                new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>("AccessDeniedException: " + ex.getMessage(),
                new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<Object> handleIncorrectData(IncorrectDataException exception) {
        return new ResponseEntity<>("IncorrectDataException: "+exception.getErrors(),
                new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


}