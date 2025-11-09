package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.AuthResponse;
import com.gtelant.commerce.service.dtos.LoginRequest;
import com.gtelant.commerce.service.dtos.UserRequest;
import com.gtelant.commerce.service.mappers.UserMapper;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.services.JwtAuthService;
import com.gtelant.commerce.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")

public class JwtAuthController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtAuthService jwtAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse>register(@RequestBody UserRequest userRequest){
        User user = userMapper.toUser(userRequest);
        String token = jwtAuthService.registerUser(user);
        return ResponseEntity.ok(new AuthResponse(token) );
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@RequestBody LoginRequest loginRequest){
        String token = jwtAuthService.loginUser(loginRequest);
        return ResponseEntity.ok(new AuthResponse(token) );
    }

}
