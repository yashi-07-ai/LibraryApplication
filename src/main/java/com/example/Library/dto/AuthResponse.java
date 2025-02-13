package com.example.Library.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields in JSON response
public class AuthResponse {

    private final String token;
    private final String username;
    private final String role; // Use Set to avoid duplicates

    // Constructor for token-only response
    public AuthResponse(String token) {
        this.token = token;
        this.username = null;
        this.role = null; // Use emptySet instead of List.of()
    }

    // Full constructor
    public AuthResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() { // Change return type to Set<String>
        return role;
    }
}
