package com.example.loginbackend.exception;

import com.example.loginbackend.dto.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {
        log.warn("Validation error at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(
            ExpiredJwtException e, HttpServletRequest request) {
        log.warn("JWT token expired at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message("토큰이 만료되었습니다. 다시 로그인해주세요.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(
            UsernameNotFoundException e, HttpServletRequest request) {
        log.warn("User not found at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message("존재하지 않는 사용자입니다.")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(
            BadCredentialsException e, HttpServletRequest request) {
        log.warn("Invalid credentials at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message("아이디 또는 비밀번호가 올바르지 않습니다.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error at {}: {}", request.getRequestURI(), e.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
