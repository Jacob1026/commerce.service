package com.gtelant.commerce.service.controllers;

import com.gtelant.commerce.service.dtos.AuthResponse;
import com.gtelant.commerce.service.dtos.LoginRequest;
import com.gtelant.commerce.service.dtos.UserRequest;
import com.gtelant.commerce.service.mappers.UserMapper;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.services.JwtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/jwt")

public class JwtAuthController {

    private UserMapper userMapper;
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
