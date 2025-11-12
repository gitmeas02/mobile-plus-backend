package com.example.mobile.error.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handle404(NoHandlerFoundException ex, WebRequest request) {
        // Prefer the request's description (contains the URI), fall back to the exception's URL if empty
        String path = null;
        if (request != null) {
            // WebRequest#getDescription(false) returns something like "uri=/requested/path"
            String desc = request.getDescription(false);
            if (desc != null && desc.startsWith("uri=")) {
                path = desc.substring(4);
            }
        }
        if (path == null || path.isEmpty()) {
            path = ex.getRequestURL();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Custom 404 Error: " + path + " not found");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(NoResourceFoundException ex, WebRequest request) {
        // Handle static resource not found exceptions
        String path = null;
        if (request != null) {
            String desc = request.getDescription(false);
            if (desc != null && desc.startsWith("uri=")) {
                path = desc.substring(4);
            }
        }
        if (path == null || path.isEmpty()) {
            path = ex.getResourcePath();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Custom 404 Error: " + path + " not found");
    }
}
