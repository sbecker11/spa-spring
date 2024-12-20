package com.spexture.service;

import com.spexture.model.User;
import com.spexture.repository.HBaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private HBaseUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerUser(String email, String password) throws IOException {
        String id = email; // Using email as id for simplicity
        User user = new User(id, email, bCryptPasswordEncoder.encode(password));
        return userRepository.saveUser(user);
    }

    public User findUserByEmail(String email) throws IOException {
        return userRepository.getUser(email);
    }

    // Other methods like authentication, update, delete can be implemented here
}