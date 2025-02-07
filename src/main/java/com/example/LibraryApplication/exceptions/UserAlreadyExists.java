package com.example.LibraryApplication.exceptions;

public class UserAlreadyExists extends RuntimeException {
    private String message;

    public UserAlreadyExists() {}

    public UserAlreadyExists(String msg) {
        super(msg);
        this.message = msg;
    }
}
