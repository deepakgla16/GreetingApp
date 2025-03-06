package com.example.GreetingApp.controller;

import com.example.GreetingApp.DTO.AuthDTO;
import com.example.GreetingApp.DTO.LoginDTO;
import com.example.GreetingApp.services.AuthServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthServices authServices;

    public AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO authDTO) {
        logger.info("Received Registration Request: {}", authDTO);
        try {
            String response = authServices.registerUser(authDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during registration", e);
            return ResponseEntity.internalServerError().body("Registration failed");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        try {
            String response = authServices.verifyUser(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during verification", e);
            return ResponseEntity.internalServerError().body("Verification failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
    logger.info("Received login Request for email:",loginDTO.getEmail());

    try {
        String token=authServices.loginUser(loginDTO);
        return ResponseEntity.ok("{\"message\": \"Login successful!\", \"token\": \"" + token + "\"}");

    }catch (Exception e) {
        logger.error("Error during login", e);
        return ResponseEntity.status(401).body("Invalid email or password");
    }


    }

}
