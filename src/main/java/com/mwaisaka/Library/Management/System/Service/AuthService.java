package com.mwaisaka.Library.Management.System.Service;


import com.mwaisaka.Library.Management.System.Dto.mapper.UserMapper;
import com.mwaisaka.Library.Management.System.Dto.request.*;
import com.mwaisaka.Library.Management.System.Dto.response.AuthResponse;
import com.mwaisaka.Library.Management.System.Dto.response.MessageResponse;
import com.mwaisaka.Library.Management.System.models.User;
import com.mwaisaka.Library.Management.System.Repository.UserRepository;
import com.mwaisaka.Library.Management.System.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = userMapper.toEntity(request, passwordEncoder);
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(user);

        return userMapper.toAuthResponse(user, jwt);
    }

    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        return new MessageResponse("Password reset link sent to your email!");
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return new MessageResponse("Password reset successfully!");
    }
}
