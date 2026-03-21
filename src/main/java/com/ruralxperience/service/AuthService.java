package com.ruralxperience.service;

import com.ruralxperience.dto.request.LoginRequest;
import com.ruralxperience.dto.request.RefreshTokenRequest;
import com.ruralxperience.dto.request.RegisterRequest;
import com.ruralxperience.dto.response.AuthResponse;
import com.ruralxperience.entity.HostProfile;
import com.ruralxperience.entity.User;
import com.ruralxperience.enums.Role;
import com.ruralxperience.exception.ConflictException;
import com.ruralxperience.repository.HostProfileRepository;
import com.ruralxperience.repository.UserRepository;
import com.ruralxperience.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final HostProfileRepository hostProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already registered: " + request.email());
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(request.role())
                .enabled(true)
                .build();

        userRepository.save(user);

        // Auto-create HostProfile for HOST registrations
        if (request.role() == Role.HOST) {
            HostProfile profile = HostProfile.builder()
                    .user(user)
                    .location("To be updated")
                    .build();
            hostProfileRepository.save(profile);
        }

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (!jwtService.isRefreshToken(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.of(
                accessToken, refreshToken,
                jwtService.getAccessTokenExpiration(),
                user.getId(), user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getRole());
    }
}
