package com.gtelant.commerce.service.configs;

// import com.gtelant.commerce.service.models.User; // <-- 1. UserDetails 已足夠
import com.gtelant.commerce.service.repositories.UserRepository;
import com.gtelant.commerce.service.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//過濾請求的設定，賦予權限
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 衛哨兵: 如果沒有 Token，直接跳過此過濾器，執行下一個
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String email = jwtService.getUserEmailFromToken(jwtToken);

        // 如果 Token 有效 (email != null) 且 SecurityContext 中尚無使用者
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // 8. 警告: 您的 'User' 類別必須實作 'UserDetails' 介面
            UserDetails userDetails = this.userRepository.findByEmail(email)
                    .orElse(null); // 如果 Token 中的用戶已不存在於資料庫

            if (userDetails != null) {
                // 9. 呼叫我們在 JwtService 中建立的 isTokenValid
                if(jwtService.isTokenValid(jwtToken, userDetails)){

                    // 10. 建立 Spring Security 的 Authentication 物件
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // 我們使用 JWT，所以 credentials (密碼) 為 null
                            userDetails.getAuthorities()
                    );

                    // 11. 將 request 的詳細資訊加入 authToken 中
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 12. 更新 SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        // 13. (重要!) 讓過濾器鏈繼續執行下去
        filterChain.doFilter(request,response);
    }
}
