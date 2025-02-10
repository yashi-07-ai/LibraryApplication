package com.example.LibraryApplication.service;

import com.example.LibraryApplication.dto.BookResponseDTO;
import com.example.LibraryApplication.dto.UserDTO;
import com.example.LibraryApplication.dto.UserResponseDTO;
import com.example.LibraryApplication.exceptions.NoSuchBookExistsException;
import com.example.LibraryApplication.exceptions.NoSuchUserExistsExceptions;
import com.example.LibraryApplication.exceptions.UserAlreadyExists;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO convertToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(String.valueOf(user.getRole()))
                .build();
    }

    public User convertToEntity(UserDTO userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .role(User.UserRole.valueOf(userDto.getRole()))
                .build();
    }

    public List<UserResponseDTO> getAllUsers(){
        log.info("Getting all the users in the library");
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            log.error("No user found in library");
            throw new NoSuchBookExistsException("No user found");
        }

        log.info("Displaying the users");
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id){
        log.info("Inside getUserById method");

        Optional<User> userOptional = userRepository.findById(id);

        // Check if the user exists
        if (userOptional.isPresent()) {
            log.info("User found");
            return userOptional; // Return the user
        } else {
            throw new NoSuchUserExistsExceptions("User not found with id: " + id);
        }
    }

    public List<UserResponseDTO> getUserByName(String name) {
        log.info("Searching for book with keyword : {}", name);
        List<User> users = userRepository.findByName(name).orElseThrow(() -> new NoSuchUserExistsExceptions(
                "No user found with name : " + name
        ));

        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public User addUser(UserDTO userDto){
        log.info("Inside addUser method");
        User user = convertToEntity(userDto);
        if (user.getId() != null) {
            log.error("ID null");
            throw new IllegalArgumentException("ID should be null for new users.");
        }

        // You can also check if a book with the same title (or other unique field) exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail()); // Assuming title is unique
        if (existingUser.isPresent()) {
            log.error("User already exists");
            throw new UserAlreadyExists("User already exists with this email");
        }

        // Save the new book
        log.info("Adding new user");
        userRepository.save(user);
        return user;

    }

    @Transactional
    public void deleteUser(Long id){
        log.info("Inside deleteUser method");
        Optional<User> userOptional = userRepository.findById(1L);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("Deleting user");
            userRepository.delete(user);
        } else {
            log.error("No user found");
            throw new NoSuchUserExistsExceptions("No user with this id exists!");
        }
    }
}
