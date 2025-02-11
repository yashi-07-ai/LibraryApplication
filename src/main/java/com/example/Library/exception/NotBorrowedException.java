package com.example.Library.exception;

public class NotBorrowedException extends RuntimeException{

    public NotBorrowedException(){}
    public NotBorrowedException(String msg){
        super(msg);
    }
}