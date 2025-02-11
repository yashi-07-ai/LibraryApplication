package com.example.Library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAlreadyExists extends RuntimeException{

    public BookAlreadyExists(){}
    public BookAlreadyExists(String msg){
        super(msg);
    }
}