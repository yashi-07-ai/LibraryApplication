package com.example.LibraryApplication.service;

import com.example.LibraryApplication.model.User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int userIdCounter = 1;

    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id){
        return users.get(id);
    }

    public User addUser(User user){
        user.setId(userIdCounter++);
        users.put(user.getId(), user);
        return user;
    }

    public boolean deleteUser(int id){
        return users.remove(id) != null;
    }
}
