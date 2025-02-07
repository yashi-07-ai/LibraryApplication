package com.example.LibraryApplication.exceptions;

public class NotBorrowedException extends RuntimeException{
    private String message;

    public NotBorrowedException(){}

    public NotBorrowedException(String msg){
        this.message=msg;
    }
}
