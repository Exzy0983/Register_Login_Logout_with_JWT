package com.example.loginbackend.repository;

import com.example.loginbackend.entity.RefreshToken;
import com.example.loginbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // 토큰 문자열로 조회
    Optional<RefreshToken> findByToken(String token);

    // 사용자별 토큰 조회
    Optional<RefreshToken> findByUser(User user);

    // 사용자별 토큰 삭제 (로그아웃 시)
    void deleteByUser(User user);

    // 만료된 토큰들 삭제 (배치 작업용)
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
