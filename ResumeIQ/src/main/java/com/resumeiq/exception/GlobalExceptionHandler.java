package com.resumeiq.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(
            ResponseStatusException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleResponseStatusException(
                    ResponseStatusException ex) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "timestamp",
                LocalDateTime.now()
        );


        response.put(
                "status",
                ex.getStatusCode().value()
        );


        response.put(
                "error",
                ex.getStatusCode().toString()
        );


        response.put(
                "message",
                ex.getReason() == null
                        ? "Request failed"
                        : ex.getReason()
        );


        return ResponseEntity
                .status(
                        ex.getStatusCode()
                )
                .body(response);
    }


    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleBadRequest(
                    IllegalArgumentException ex) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "timestamp",
                LocalDateTime.now()
        );


        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );


        response.put(
                "error",
                "Bad Request"
        );


        response.put(
                "message",
                ex.getMessage()
        );


        return ResponseEntity
                .badRequest()
                .body(response);
    }


    @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<Map<String, Object>>
            handleRuntimeException(
                    RuntimeException ex) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "timestamp",
                LocalDateTime.now()
        );


        response.put(
                "status",
                HttpStatus
                        .INTERNAL_SERVER_ERROR
                        .value()
        );


        response.put(
                "error",
                "Internal Server Error"
        );


        response.put(
                "message",
                ex.getMessage()
        );


        return ResponseEntity
                .status(
                        HttpStatus
                                .INTERNAL_SERVER_ERROR
                )
                .body(response);
    }
}