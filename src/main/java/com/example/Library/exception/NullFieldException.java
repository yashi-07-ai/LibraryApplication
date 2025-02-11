package com.example.Library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NullFieldException extends RuntimeException {
    public NullFieldException(String msg){
        super(msg);
    }
}

