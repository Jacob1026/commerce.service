package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.dtos.AuthResponse;
import com.gtelant.commerce.service.dtos.LoginRequest;
import com.gtelant.commerce.service.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private long expirationTimeInMs;

    private Key getSigningKey() {
        byte [] keyBytes = Decoders.BASE64.decode(jwtSecret); //把 Base64 格式還原成原始 bytes
        return Keys.hmacShaKeyFor(keyBytes);//hmacShaKeyFor自動檢查長度
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserEmailFromToken(token); // 這一步已隱含簽章驗證
        // 檢查：Email 是否吻合 且 Token 未過期
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//檢查 Token 是否已過期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token)//檢查play load裡的(Expiration Time) 欄位
                .before(new Date());
    }
//從 Token 中提取過期時間
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


//(重構) 從 Token 中提取 Email (Subject)
    public String getUserEmailFromToken(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    //提取 Token 中的特定 Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);// 先取得整包
        return claimsResolver.apply(claims);// 再執行指定的拿取動作
    }

   //提取 Token 中的所有 Claims (這一步會驗證簽章)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())//驗證用的鑰匙
                .build()
                .parseClaimsJws(token) // 驗證簽名並拆開信封
                .getBody();            //拿出裡面的信件內容 (Claims Map)
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())// 將使email放入 Payload
                .setIssuedAt(new Date(System.currentTimeMillis()))// 發證時間
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMs))//1H過期
                .signWith(getSigningKey())
                .compact();
    }


}
