package com.vipinSpring6.security.service;

import com.vipinSpring6.security.CustomUserDetails;
import com.vipinSpring6.security.entity.User;
import com.vipinSpring6.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(Objects.isNull(user)){
            System.out.println("User not available ");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
