package com.example.LibraryApplication.exceptions;

public class NoSuchUserExistsExceptions extends RuntimeException{
    private String message;

    public NoSuchUserExistsExceptions(){}

    public NoSuchUserExistsExceptions(String msg){
        super(msg);
        this.message = msg;
    }
}
