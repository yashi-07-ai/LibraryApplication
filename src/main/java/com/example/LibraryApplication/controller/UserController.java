package com.example.LibraryApplication.controller;

import com.example.LibraryApplication.dto.UserDTO;
import com.example.LibraryApplication.dto.UserResponseDTO;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.service.BookService;
import com.example.LibraryApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@ControllerAdvice
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        log.info("Received request to find user by Id");
        try{
            log.info("Fetching user by Id : {}", id);
            Optional<User> user = userService.getUserById(id);
            log.info("Successfully fetched user details");
            return ResponseEntity.ok(user);
        }
        catch (Exception e){
            log.error("Error getting the user", e);
            return ResponseEntity.notFound().build();
        }
        //Optional<User> user = userService.getUserById(id);
        //return user != null ? (ResponseEntity<User>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDto){
        log.info("Received request to add a user : {}", userDto.getName());
        try{
            User user = userService.addUser(userDto);
            log.info("Book added: {}", user.getId());
            return ResponseEntity.ok(user);
        }
        catch (Exception e){
            log.error("Error adding the user", e);
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        log.info("Received request to delete a user : {}", id);
        try{
            userService.deleteUser(id);
            log.info("User with id {} no longer exists", id);
            return ResponseEntity.ok("User deleted successfully");
        }
        catch (Exception e){
            log.error("Error deleting the user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }

    }
}
