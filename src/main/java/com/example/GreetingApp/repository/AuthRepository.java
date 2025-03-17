package com.example.GreetingApp.repository;

import com.example.GreetingApp.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthUser, Long>{
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByVerificationToken(String token);

    Optional<AuthUser>findByResetToken(String resetToken);
}
