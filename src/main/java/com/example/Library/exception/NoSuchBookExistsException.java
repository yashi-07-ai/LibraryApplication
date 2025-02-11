package com.example.Library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoSuchBookExistsException extends RuntimeException{

    public NoSuchBookExistsException(){}
    public NoSuchBookExistsException(String msg){
        super(msg);
    }
}