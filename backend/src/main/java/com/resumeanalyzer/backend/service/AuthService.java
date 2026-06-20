package com.resumeanalyzer.backend.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resumeanalyzer.backend.config.JwtUtil;
import com.resumeanalyzer.backend.dto.LoginRequest;
import com.resumeanalyzer.backend.dto.RegisterRequest;
import com.resumeanalyzer.backend.entity.User;
import com.resumeanalyzer.backend.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
    }

    public String registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }

        User user=User.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .createdAt(LocalDateTime.now())
                    .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    public String loginUser(LoginRequest request){
        User user=userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()->new RuntimeException("Invalid email or password"));

        boolean passwordMatches=passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!passwordMatches){
            throw new RuntimeException("Invalid email or password");
        }

        // generate jwt token
        return jwtUtil.generateToken(user.getEmail());
    }

    

    
}
