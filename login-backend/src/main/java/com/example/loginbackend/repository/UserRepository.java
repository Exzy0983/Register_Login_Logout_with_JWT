package com.example.loginbackend.repository;

import com.example.loginbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 중복 검사
    Optional<User> findByLoginId(String username);
}
