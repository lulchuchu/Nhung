package com.example.demo.Service;

import com.example.demo.Modal.User;
import com.example.demo.Repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepository;

    public UserService(UserRepo userRepo) {
        this.userRepository = userRepo;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }


}
