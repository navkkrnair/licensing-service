package com.cts.license.controller;

import com.cts.license.exceptions.LicenseError;
import com.cts.license.exceptions.NoEntityFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LicenseError licenseError = LicenseError.builder()
                                                .message(ex.getMessage())
                                                .build();
        return new ResponseEntity<>(licenseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoEntityFoundException.class)
    public ResponseEntity<LicenseError> handleNoEntityFound(NoEntityFoundException e) {
        return new ResponseEntity(LicenseError.builder()
                                              .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}
