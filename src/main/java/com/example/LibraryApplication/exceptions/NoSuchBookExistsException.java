package com.example.LibraryApplication.exceptions;

public class NoSuchBookExistsException extends RuntimeException{
    private String message;

    public NoSuchBookExistsException(){}

    public NoSuchBookExistsException(String msg){
        this.message = msg;
    }
}
