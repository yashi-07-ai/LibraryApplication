package com.example.LibraryApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoSuchUserExistsExceptions extends RuntimeException{

    public NoSuchUserExistsExceptions(){}
    public NoSuchUserExistsExceptions(String msg){
        super(msg);
    }
}
