package com.gtelant.commerce.service.configs;

// import com.gtelant.commerce.service.repositories.UserRepository; // <-- **修正 #1: 移除**
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 衛哨兵
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String email = jwtService.getUserEmailFromToken(jwtToken);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            try {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if(jwtService.isTokenValid(jwtToken, userDetails)){

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (UsernameNotFoundException e) {
                // 如果 Token 中的用戶在資料庫中找不到 (例如已被刪除)
                // 我們什麼也不做，SecurityContext 保持為 null (未驗證)
                // 這樣請求在後面就會被 Spring Security 擋下 (因為 .anyRequest().authenticated())
            }
        }
        filterChain.doFilter(request,response);
    }
}