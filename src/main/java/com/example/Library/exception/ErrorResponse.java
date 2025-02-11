package com.example.Library.exception;

// Custom Error Response Class

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "error response structure")
public class ErrorResponse {
    @Schema
    private int status;

    @Schema
    private String message;

    @Schema
    private String path;

    @Schema
    private String method;

    public ErrorResponse(int status, String message, String path, String method) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.method = method;
    }
}
