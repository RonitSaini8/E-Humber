package com.humber.EHumber.services;

import com.humber.EHumber.models.MyUser;
import com.humber.EHumber.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public int saveUser(MyUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return 0;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return 1;
    }
}
