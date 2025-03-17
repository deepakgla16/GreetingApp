package com.example.GreetingApp.services;

import com.example.GreetingApp.DTO.AuthDTO;
import com.example.GreetingApp.DTO.LoginDTO;
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


    private final AuthRepository authRepository;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final Jwt jwtUtil;
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

    public String loginUser(LoginDTO loginDTO){
        Optional<AuthUser> userOptional=authRepository.findByEmail(loginDTO.getEmail());

        if (userOptional.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Email not found");
        }

        AuthUser user =userOptional.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid password");
        }
        if (user.getVerificationToken()!=null){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Verify your account");

        }

        String token=jwtUtil.generateToken(user.getEmail());

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token validation failed");
        }

        return "Login Succsfull and token: "+token;

    }


    public  String forgetPassword(String email){

        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        Optional<AuthUser> userOptional=authRepository.findByEmail(email);
        if (userOptional.isEmpty()){
            return "User not found:"+email;
        }
        AuthUser foundUser=userOptional.get();

        String  resetToken=jwtUtil.generateToken(email);
        foundUser.setResetToken(resetToken);
        authRepository.save(foundUser);


        emailService.sendResetEmail(email,resetToken);

        return "Reset password link sent to:"+email;

    }



    public String resetPassword(String resetToken,String newPassword){
        Optional<AuthUser> userOptional=authRepository.findByResetToken(resetToken);

        if (userOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token");
        }

        AuthUser user=userOptional.get();

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);

        authRepository.save(user);

        return "password reset successfull";

    }

}
