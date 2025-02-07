package com.example.LibraryApplication.controller;

import com.example.LibraryApplication.dto.UserDTO;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.service.BookService;
import com.example.LibraryApplication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@ControllerAdvice
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        return user != null ? (ResponseEntity<User>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDto){
        User user = userService.addUser(userDto);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String message = userService.deleteUser(id);
        return ResponseEntity.ok(message);
    }
}
