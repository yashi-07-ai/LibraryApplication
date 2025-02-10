package com.example.LibraryApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAlreadyExists extends RuntimeException{

    public BookAlreadyExists(){}
    public BookAlreadyExists(String msg){
        super(msg);
    }
}
