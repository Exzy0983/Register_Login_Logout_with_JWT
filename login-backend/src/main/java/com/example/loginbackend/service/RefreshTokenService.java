package com.example.loginbackend.service;

import com.example.loginbackend.entity.RefreshToken;
import com.example.loginbackend.entity.User;
import com.example.loginbackend.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    // 리프레시 토큰 생성 및 저장
    public RefreshToken createRefreshToken(User user) {
        // 해당 사용자의 기존 리프레시 토큰들을 모두 삭제. 중복 토큰 방지 목적
        refreshTokenRepository.deleteByUser(user);

        // 고유한 무작위 무작위 문자열 생성. UUID는 전세계적으로 유일한 식별자
        String tokenValue= UUID.randomUUID().toString();

        // 현재시간에서 30일 후를 만료시간으로 설정
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);

        // 빌더 패턴으로 RefreshToken 객체 생성
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(expiresAt)
                .build();

        // 생성된 토큰을 데이터베이스에 저장하고 저장된 객체 반환
        return refreshTokenRepository.save(newRefreshToken);

        // ** 왜 이렇게 작성했는가?
        // * 사용자당 하나의 리프레시 토큰만 유지하여 보안성 향상
        // * UUID 사용으로 토큰 예측 불가능성 확보
        // * 30일의 긴 수명으로 사용자 편의성 제공
    }


    // 리프레시 토큰으로 사용자 조회
    public User getUserByRefreshToken(String token){
        // 토큰으로 DB조회, 없으면 예외발생
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        // 토큰이 만료되었는지 확인
        if(isTokenExpired(refreshToken)){
            refreshTokenRepository.delete(refreshToken); // 만료된 토큰을 DB에서 삭제
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다."); // 만료된 토큰 사용 시 예외 발생
        }

        // 유효한 토큰이면 연결된 사용자 객체 반환
        return refreshToken.getUser();

        // ** 왜 이렇게 작성했는가?
        // * 만료된 토큰은 즉시 삭제하여 보안 위험 제거
        // * 명확한 예외 메시지로 클라이언트에 상황 전달
        // * 유효성 검증 후 사용자 정보 반환으로 안전성 확보
    }


    // 리프레시 토큰 만료 확인
    public boolean isTokenExpired(RefreshToken refreshToken) {
        // 현재시간과 토큰의 만료시간을 비교, 현재시간이 만료시간보다 이후면 만료됨(true), 아니면(false)
        return LocalDateTime.now().isAfter(refreshToken.getExpiresAt());

        // ** 왜 이렇게 작성했는가?
        // * 단순하고 직관적인 시간 비교로 가독성 향상
        // * 별도 메소드로 분리하여 재사용성 확보
        // * boolean 반환으로 조건문에서 사용하기 편리
    }


    // 리프레시 토큰 삭제 (로그아웃)
    public void deleteRefreshToken(String token) {
        // 토큰으로 DB에서 조회, 없으면 예외
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("토큰을 찾을 수 없습니다."));

        // 찾은 토큰을 DB에서 삭제
        refreshTokenRepository.delete(refreshToken);

        // ** 왜 이렇게 작성했는가?
        // * 존재하지 않는 토큰 삭제 시도 시 명확한 피드백 제공
        // * 로그아웃이나 토큰 무효화 시 사용
        // * 실제 객체 토큰 객체를 찾은 후 삭제하여 안전성 확보
    }


    // 사용자별 모든 토큰 삭제
    public void deleteAllRefreshTokenByUser(User user) {
        // 레퍼지토리의 커스텀 메서드로 해당 사용자의 모든 토큰 삭제
        // 내부적으로 DELETE FROM refresh_token WHERE user_id = ? 쿼리 실행
        refreshTokenRepository.deleteByUser(user);

        // ** 왜 이렇게 작성했는가?
        // * 사용자가 모든 기기에서 로그아웃할 때 사용
        // * 보안 사고 발생 시 해당 사용자의 모든 토큰 무효화
        // * 레퍼지토리의 메서드 활용으로 코드 간소화
    }
}
