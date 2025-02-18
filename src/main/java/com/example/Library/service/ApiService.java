package com.example.Library.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://odin.housing.com/odin").build();
    }

    public Mono<String> fetchInfo() {
        return webClient.get()
                .uri("/info")
                .retrieve()
                .bodyToMono(String.class);
    }
}
