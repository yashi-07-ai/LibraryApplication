package com.example.Library.controller;

import com.example.Library.dto.*;
import com.example.Library.exception.*;
import com.example.Library.model.User;
//import com.example.Library.model.UserPrincipal;
import com.example.Library.service.BookService;
import com.example.Library.service.JwtUtil;
import com.example.Library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "APIs for managing users")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private User userPrincipal;

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    // Get all users (Only for Admins)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUsers")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID (Only for Admins & the user himself)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/findUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        log.info("Fetching user by Id : {}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    // Register a new user
//    @PostMapping("/register")
//    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDto) {
//        log.info("Registering user: {}", userDto.getName());
//        User user = userService.addUser(userDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }
//
//    // Authenticate user & generate JWT token
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Get UserDetails from Authentication
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String token = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new AuthResponse(token, userDetails.getUsername(), userDetails.getAuthorities()));
//    }

    // Delete user (Only Admins can delete users)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
