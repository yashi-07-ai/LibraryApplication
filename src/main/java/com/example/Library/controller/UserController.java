package com.example.Library.controller;

import com.example.Library.dto.*;
import com.example.Library.exception.*;
import com.example.Library.model.User;
import com.example.Library.service.BookService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "APIs for managing users") // Swagger Tag
public class UserController {
    @Autowired
    private UserService userService;

    private static Logger log = LoggerFactory.getLogger(BookService.class);


    @Operation(summary = "Get all users", description = "Fetch a list of all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Fetch a user by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Parameter(description = "ID of the user to fetch") @PathVariable Long id) {
        log.info("Received request to find user by Id");
        try {
            log.info("Fetching user by Id : {}", id);
            Optional<User> user = userService.getUserById(id);
            log.info("Successfully fetched user details");
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error getting the user", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add a new user", description = "Create a new user with provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDto) {
        log.info("Received request to add a user : {}", userDto.getName());
        try {
            User user = userService.addUser(userDto);
            log.info("User added: {}", user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            log.error("Error adding the user", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a user", description = "Remove a user from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Error deleting the user")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable Long id) {
        log.info("Received request to delete a user : {}", id);
        try {
            userService.deleteUser(id);
            log.info("User with id {} no longer exists", id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting the user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}