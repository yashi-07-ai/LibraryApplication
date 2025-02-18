package com.example.Library.controller;

import com.example.Library.dto.AuthRequest;
import com.example.Library.dto.AuthResponse;
import com.example.Library.dto.UserDTO;
import com.example.Library.dto.UserResponseDTO;
import com.example.Library.model.User;
//import com.example.Library.model.UserPrincipal;
import com.example.Library.repository.UserRepository;
import com.example.Library.service.JwtUtil;
import com.example.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;


// handles the login part only -> generates token for a user and returns it as response
@RestController
@RequestMapping("/auth")
public class authController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String helloWorld(){
        return "Welcome";
    }

//    @GetMapping("/profile")
//    public String getUserProfile() {
//        // Get the authenticated user's principal
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        // Assuming userDetails is of type UserPrincipal
//        if (userDetails instanceof User userPrincipal) {
//            return "User Profile: " + userPrincipal.getUsername();
//        }
//
//        return "No user authenticated.";
//    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserDTO userDto) {
        //log.info("Registering user: {}", userDto.getName());
        UserResponseDTO user = userService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        return ResponseEntity.ok(new AuthResponse(token, username, role));
    }
}


