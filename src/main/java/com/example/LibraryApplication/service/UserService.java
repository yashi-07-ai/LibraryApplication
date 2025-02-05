package com.example.LibraryApplication.service;

import com.example.LibraryApplication.model.User;
import com.example.LibraryApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
//    private final Map<Integer, User> users = new HashMap<>();

    @Autowired
    UserRepository userRepo;

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public User getUserById(int id){
        return userRepo.findById(id);
    }

    public User addUser(User user){
        userRepo.save(user);
        return user;
    }

    public String deleteUser(int userId){

            if(userRepo.findById(userId) != null){
                userRepo.deleteById(userId);
                return "User deleted";
            }
            else{
                return "User not Found";
            }


    }
}
