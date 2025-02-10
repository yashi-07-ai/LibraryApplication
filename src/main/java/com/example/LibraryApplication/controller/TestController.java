package com.example.LibraryApplication.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test API", description = "APIs for testing")
public class TestController {

    @GetMapping
    @Operation(summary = "Test endpoint", description = "A simple test endpoint")
    @ApiResponse(responseCode = "200", description = "Success")
    public String test() {
        return "Hello, Swagger!";
    }
}