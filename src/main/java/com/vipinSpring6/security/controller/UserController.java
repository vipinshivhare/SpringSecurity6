package com.vipinSpring6.security.controller;

import com.vipinSpring6.security.entity.User;
import com.vipinSpring6.security.repository.UserRepository;
import com.vipinSpring6.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){// entity wala user
        //return userRepository.save(user);
        return userService.register(user);
    }


    @PostMapping("/login")
    public String login(@RequestBody User user){
        var u = userRepository.findByUserName(user.getUsername());
        if(!Objects.isNull(u))
            return "success";
        return "failed";
    }

}
