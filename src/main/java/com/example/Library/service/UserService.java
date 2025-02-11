package com.example.Library.service;

import com.example.Library.dto.UserDTO;
import com.example.Library.dto.UserResponseDTO;
import com.example.Library.exception.NoSuchBookExistsException;
import com.example.Library.exception.NoSuchUserExistsException;
import com.example.Library.exception.UserAlreadyExists;
import com.example.Library.model.User;
import com.example.Library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static Logger log = LoggerFactory.getLogger(BookService.class);

    public UserResponseDTO convertToDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setRole(String.valueOf(user.getRole()));
        return userResponseDTO;
    }


    public User convertToEntity(UserDTO userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(User.UserRole.valueOf(userDto.getRole()));
        return user;
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
            throw new NoSuchUserExistsException("User not found with id: " + id);
        }
    }

    public List<UserResponseDTO> getUserByName(String name) {
        log.info("Searching for book with keyword : {}", name);
        List<User> users = userRepository.findByName(name).orElseThrow(() -> new NoSuchUserExistsException(
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
            throw new NoSuchUserExistsException("No user with this id exists!");
        }
    }
}
