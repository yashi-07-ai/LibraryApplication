package com.example.LibraryApplication.exceptions;

public class BookAlreadyExists extends RuntimeException{
    private String message;

    public BookAlreadyExists(){}

    public BookAlreadyExists(String msg){
        this.message = msg;
    }
}
