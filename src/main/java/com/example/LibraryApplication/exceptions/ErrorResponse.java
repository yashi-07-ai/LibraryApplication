package com.example.LibraryApplication.exceptions;

// Custom Error Response Class
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status = 0;
    private String method = "";
    private String path;
    private int statusCode;
    private String message;

    public ErrorResponse(int status, String method, String message)
    {
        super();
        this.status = status;
        this.method = method;
        this.message = message;
    }

    public ErrorResponse(int status, String message, String path, String method) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.method = method;
    }

    public ErrorResponse(int status, String method) {
        this.status = status;
        this.method = method;
    }

}
