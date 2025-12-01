package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.dtos.LoginRequest;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@RequiredArgsConstructor
@Service
public class JwtAuthService {


    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(User user){
        // 檢查用戶是否已存在
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("用戶已存在");
        }
        // 密碼加密
        user.setPassword( passwordEncoder.encode(user.getPassword()));
        // 保存用戶
        userRepository.save(user);
        // 生成JWT令牌
        return jwtService.generateToken(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 驗證密碼
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // 生成JWT令牌
                return jwtService.generateToken(user);
            } else {
                throw new RuntimeException("無效的密碼");
            }
        }
        throw new RuntimeException("用戶不存在");
    }
}
