package com.example.LibraryApplication.exceptions;

public class NotBorrowedException extends RuntimeException{

    public NotBorrowedException(){}
    public NotBorrowedException(String msg){
        super(msg);
    }
}
