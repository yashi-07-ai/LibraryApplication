package com.example.Library.controller;

import com.example.Library.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class externalApiController {

    @Autowired
    private ApiService apiService;

    //hitting external api for demonstration purpose only
    @GetMapping("/fetch")
    public Mono<String> fetchInfo() {
        return apiService.fetchInfo();
    }
}
