package com.example.LibraryApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists() {}
    public UserAlreadyExists(String msg) {
        super(msg);
    }
}
