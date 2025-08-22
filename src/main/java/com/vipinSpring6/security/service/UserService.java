package com.vipinSpring6.security.service;

import com.vipinSpring6.security.entity.User;
import com.vipinSpring6.security.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder
                .encode(user.getPassword()));
        return userRepository.save(user);
    }
}
