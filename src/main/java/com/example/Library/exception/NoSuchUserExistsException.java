package com.example.Library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoSuchUserExistsException extends RuntimeException{

    public NoSuchUserExistsException(){}
    public NoSuchUserExistsException(String msg){
        super(msg);
    }
}
