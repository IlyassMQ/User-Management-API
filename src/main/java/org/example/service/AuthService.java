package org.example.service;

import org.example.entity.User;
import org.example.exception.AcounntNotActive;
import org.example.exception.InvalidCredentialsException;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;


    public AuthService(UserService userService,BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    public User Login(String email, String password){
        User user = userService.findByEmail(email);

        if ( !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        } else if (!user.isActive()) {
            throw new AcounntNotActive();
        }
        return user;
    }
}
