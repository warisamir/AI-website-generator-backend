package com.rockhardy.lovable.advice;

import com.rockhardy.lovable.exception.BadRequestException;
import com.rockhardy.lovable.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 – Business validation
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    // 400 – Request body validation (@Valid)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Invalid request payload", ex);
    }

    // 400 – Malformed JSON
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJson(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request", ex);
    }

    // 404 – Resource missing
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    // 409 – DB constraint
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrity(Exception ex) {
        return build(HttpStatus.CONFLICT, "Data integrity violation", ex);
    }

    // 500 – Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternal(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex);
    }

    private ResponseEntity<ApiResponse<?>> build(HttpStatus status, String message, Exception ex) {
        ApiError apiError = ApiError.builder()
                .status(status)
                .message(message)
                .build();

        log.error(message, ex);
        return new ResponseEntity<>(new ApiResponse<>(apiError), status);
    }
}








