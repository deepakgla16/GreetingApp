package com.example.GreetingApp.services;

import com.example.GreetingApp.DTO.AuthDTO;
import com.example.GreetingApp.Utils.Jwt;
import com.example.GreetingApp.model.AuthUser;
import com.example.GreetingApp.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServices {

    @Autowired
    private final AuthRepository authRepository;
    @Autowired
    private final Jwt jwtutil;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public String registerUser(AuthDTO authDTO) {
        if (authRepository.findByEmail(authDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        AuthUser user = new AuthUser();
        user.setFirstName(authDTO.getFirstName());
        user.setLastName(authDTO.getLastName());
        user.setEmail(authDTO.getEmail());
        user.setPassword(passwordEncoder.encode(authDTO.getPassword()));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        authRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return "Verification email sent";
    }

    public String verifyUser(String token) {
        Optional<AuthUser> userOptional = authRepository.findByVerificationToken(token);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

        AuthUser user = userOptional.get();
        user.setVerificationToken(null);
        authRepository.save(user);

        return "Account verified";
    }
}
