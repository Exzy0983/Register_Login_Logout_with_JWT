package com.example.loginbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final SecretKey key;

    // 생성자
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.access-token-expiration}") long accessTokenExpirationTime,
                   @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationTime) {
        this.secretKey = secretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String generateToken(String loginId, String role) {
        return Jwts.builder()
                .subject(loginId)                                                  // 이 토큰이 누구의 것인가?
                .claim("role", role)                                              // 권한 추가
                .issuedAt(new Date())                                              // 토큰이 언제 만들어졌는지 기록
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime)) // 현재시간을 밀리초로 변환 + 유효기간 = 만료시점
                .signWith(key)                                                     // 만든 토큰을 암호화 키로 서명
                .compact();                                                        // 최종적으로 JWT 문자열 생성
    }

    // 토큰 정보 추출
    public String extractLoginId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 파싱
    private Claims extractAllClaims(String token) {
        return Jwts.parser()                    // 파서 생성
                .verifyWith(key)                // 서명 검증용 키 설정
                .build()                        // 파서 빌드
                .parseSignedClaims(token)       // 토큰 파싱
                .getPayload();                  // Claims 추출
    }

    // 권한 추출
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String loginId) {
        return Jwts.builder()
                .subject(loginId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(key)
                .compact();
    }

    // 리프레시 토큰 유효성 검사
    public boolean validateRefreshToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
