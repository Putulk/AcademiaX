package com.nextgen.erp.auth.exceptions;

import com.nextgen.erp.auth.entity.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiError> handleRefreshToken(
            InvalidRefreshTokenException ex) {

        ApiError error = ApiError.builder()
                .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }
}