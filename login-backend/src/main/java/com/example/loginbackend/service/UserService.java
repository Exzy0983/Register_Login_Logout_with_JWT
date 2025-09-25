package com.example.loginbackend.service;

import com.example.loginbackend.dto.LoginRequestDTO;
import com.example.loginbackend.dto.LoginResponseDTO;
import com.example.loginbackend.dto.RegisterRequestDTO;
import com.example.loginbackend.dto.RegisterResponseDTO;
import com.example.loginbackend.entity.Gender;
import com.example.loginbackend.entity.RefreshToken;
import com.example.loginbackend.entity.Role;
import com.example.loginbackend.entity.User;
import com.example.loginbackend.repository.UserRepository;
import com.example.loginbackend.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    //========================================= Helper Method =========================================//
    private void checkFieldValidation(String value, String fieldName, int minLength, int maxLength) {
        if (value == null || value.trim().isEmpty()) { // 필수 필드체크(null, 빈값)
            throw new IllegalArgumentException(fieldName + "은(는) 필수입니다.");
        }

        if (value.length() < minLength || value.length() > maxLength) { // 필수 필드 길이 체크
            throw new IllegalArgumentException(
                    fieldName + "은(는) " + minLength + "자 이상 " + maxLength + "자 이하여야 합니다."
            );
        }
    }

    private void checkDuplicateLoginId(String loginId) { // 아이디 중복 체크
        Optional<User> existingUser = userRepository.findByLoginId(loginId);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
    }

    // Entity -> DTO
    private RegisterResponseDTO userToDTO(User user) {
        return RegisterResponseDTO.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .regDate(user.getRegDate())
                .role(user.getRole())
                .gender(user.getGender())
                .build();
    }

    // DTO -> Entity
    private User dtoToUser(RegisterRequestDTO dto, String encodedPassword) {
        return User.builder()
                .loginId(dto.getLoginId())
                .password(encodedPassword)
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .gender(Gender.valueOf(dto.getGender()))
                .role(Role.USER)
                .build();
    }


    //========================================= Helper Method End =========================================//


    //=========================================== Validation ===========================================//
    private void validateRegisterRequest(RegisterRequestDTO dto) {
        // 1. DTO Null Check
        if (dto == null) {
            throw new IllegalArgumentException("요청된 데이터가 없습니다.");
        }

        // 2. 필수 필드 + 길이 체크
        checkFieldValidation(dto.getName(), "이름", 2, 20);
        checkFieldValidation(dto.getLoginId(), "로그인 아이디", 3, 50);
        checkFieldValidation(dto.getPassword(), "비밀번호", 8, 50);
        checkFieldValidation(dto.getEmail(), "이메일", 5, 100);
        checkFieldValidation(dto.getAddress(), "주소", 5, 100);
        checkFieldValidation(dto.getPhone(), "전화번호", 10, 20);
        checkFieldValidation(dto.getGender(), "성별", 4, 6);


        // 3. 형식체크(이메일)
        if (dto.getEmail() != null) {
            String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!dto.getEmail().matches(emailRegex)) {
                throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
            }
        }

        // 4. 성별 검증
        if (dto.getGender() != null) {
            String normalizeGender = dto.getGender().toUpperCase();
            if (!normalizeGender.equals("MALE") && !normalizeGender.equals("FEMALE") && !normalizeGender.equals("OTHER")) {
                throw new IllegalArgumentException("성별은 MALE 또는 FEMALE 또는 OTHER 이어야 합니다");
            }
            dto.setGender(normalizeGender);
        }

        // 5. 전화번호 형식 검증
        if (dto.getPhone() != null) {
            String normalizePhone = dto.getPhone().replaceAll("-", "");
            String phoneRegex = "^010\\d{8}$";
            if (!normalizePhone.matches(phoneRegex)) {
                throw new IllegalArgumentException("전화번호는 010으로 시작하는 11자리 숫자여야 합니다.");
            }
            dto.setPhone(normalizePhone);
        }

        // 6. 중복 검사
        checkDuplicateLoginId(dto.getLoginId());
    }
    //=========================================== Validation End  ===========================================//


    //============================================== Register  ==============================================//
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        // 1. 검증
        validateRegisterRequest(dto);

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. DTO -> Entity
        User user = dtoToUser(dto, encodedPassword);

        // 4. 저장
        User savedUser = userRepository.save(user);

        // 5. Entity -> DTO
        return userToDTO(savedUser);
    }
    //============================================ Register End  ============================================//


    //================================================ Login ================================================//
    @Transactional
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {

        // 사용자 조회 및 존재 확인
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        boolean isPasswordMatch = passwordEncoder.matches(dto.getPassword(), user.getPassword());

        // 비밀번호 불일치 시 예외 처리
        if (!isPasswordMatch) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Access Token 생성
        String accessToken = jwtUtil.generateToken(user.getLoginId(), user.getRole().toString());

        // 토큰 만료시간 계산
        Long expirationTime = System.currentTimeMillis() + 3600000L;

        // Refresh Token 생성 및 저장
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // LoginResponseDTO 생성 및 반환
        return LoginResponseDTO.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expirationTime(expirationTime)
                .role(user.getRole())
                .build();
    }
    //============================================== Login End  ==============================================//
}


