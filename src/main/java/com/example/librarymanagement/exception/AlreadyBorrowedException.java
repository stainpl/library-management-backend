package com.example.librarymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyBorrowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AlreadyBorrowedException(String msg) {
        super(msg);
    }
}
