package com.example.librarymanagement.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, WebRequest request) {
        ApiError body = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), body.getStatus());
    }

    @ExceptionHandler(AlreadyBorrowedException.class)
    public ResponseEntity<ApiError> handleConflict(AlreadyBorrowedException ex, WebRequest request) {
        ApiError body = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), body.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = ex.getStatusCode() instanceof HttpStatus
                ? (HttpStatus) ex.getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError body = new ApiError(status, ex.getReason() != null ? ex.getReason() : "Unknown error");
        return new ResponseEntity<>(body, new HttpHeaders(), body.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, WebRequest request) {
        ApiError body = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), body.getStatus());
    }

    public static class ApiError {
        private final HttpStatus status;
        private final String message;

        public ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
