package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.models.User;
import io.jsonwebtoken.Claims; // <-- 1. 需要匯入
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails; // <-- 2. 需要匯入
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function; // <-- 3. 需要匯入

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private long expirationTimeInMs;

    private Key getSigningKey() {
        byte [] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserEmailFromToken(token); // 這一步已隱含簽章驗證
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//檢查 Token 是否已過期

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


//從 Token 中提取過期時間
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // --- Token 解析方法 (重構) ---


//(重構) 從 Token 中提取 Email (Subject)

    public String getUserEmailFromToken(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * 提取 Token 中的特定 Claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 提取 Token 中的所有 Claims (這一步會驗證簽章)
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token) // 使用 Jws 來驗證
                .getBody();
    }

    // --- Token 生成方法 (不變) ---

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMs))
                .signWith(getSigningKey())
                .compact();
    }
}
