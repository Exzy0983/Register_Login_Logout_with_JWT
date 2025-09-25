package com.example.loginbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // ID

    @Column(unique = true, nullable = false, length = 255)
    private String token; // 토큰 값

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 관계를 나타냄, 하나의 USER가 여러개의 RefreshToken을 가질 수 있음
    @JoinColumn(name = "user_id") // 외래키 컬럼의 이름을 지정
    private User user; // 사용자 연결

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 만료시간

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성시간


}
