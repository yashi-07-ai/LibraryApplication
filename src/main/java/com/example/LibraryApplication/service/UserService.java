package com.example.LibraryApplication.service;

import com.example.LibraryApplication.dto.UserDTO;
import com.example.LibraryApplication.exceptions.BookAlreadyExists;
import com.example.LibraryApplication.exceptions.NoSuchUserExistsExceptions;
import com.example.LibraryApplication.exceptions.UserAlreadyExists;
import com.example.LibraryApplication.model.Book;
import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
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

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);

        // Check if the user exists
        if (userOptional.isPresent()) {
            return userOptional; // Return the user
        } else {
            throw new NoSuchUserExistsExceptions("User not found with id: " + id);
        }
    }

    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public User addUser(UserDTO userDto){
        User user = convertToEntity(userDto);
        if (user.getId() != null) {
            throw new IllegalArgumentException("ID should be null for new users.");
        }

        // You can also check if a book with the same title (or other unique field) exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail()); // Assuming title is unique
        if (existingUser.isPresent()) {
            throw new BookAlreadyExists("User already exists with this email");
        }

        // Save the new book
        userRepository.save(user);
        return user;

    }

    @Transactional
    public String deleteUser(Long id){
        Optional<User> userOptional = userRepository.findById(1L);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
            return "User deleted";
        } else {
            throw new NoSuchUserExistsExceptions("No user with this id exists!");
        }
    }
}
