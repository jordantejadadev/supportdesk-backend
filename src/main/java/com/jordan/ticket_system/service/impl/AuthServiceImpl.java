package com.jordan.ticket_system.service.impl;

import com.jordan.ticket_system.dto.LoginRequest;
import com.jordan.ticket_system.dto.RegisterRequestDTO;
import com.jordan.ticket_system.dto.UserResponseDTO;
import com.jordan.ticket_system.entity.RefreshToken;
import com.jordan.ticket_system.entity.Role;
import com.jordan.ticket_system.entity.User;
import com.jordan.ticket_system.exception.ResourceAlreadyExistsException;
import com.jordan.ticket_system.exception.UnauthorizedException;
import com.jordan.ticket_system.mapper.UserMapper;
import com.jordan.ticket_system.repository.RefreshTokenRepository;
import com.jordan.ticket_system.repository.UserRepository;
import com.jordan.ticket_system.security.JwtService;
import com.jordan.ticket_system.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;

    @Override
    public User login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UnauthorizedException("Credenciales inválidas"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password incorrecto");
        }

        return user;
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .user(user)
                .build();

        RefreshToken saved = refreshTokenRepository.save(refreshToken);

        return saved;
    }

    @Override
    public String refresh(String refreshTokenValue) {

        if(refreshTokenValue == null) {
            throw new UnauthorizedException("Refresh token no encontrado");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token expirado");
        }

        User user = refreshToken.getUser();

        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    @Override
    public void logout(String refreshTokenValue) {

        if(refreshTokenValue == null) {
            return;
        }

        refreshTokenRepository.findByToken(refreshTokenValue).ifPresent(refreshTokenRepository::delete);

    }

    @Override
    public String createAccessToken(User user) {

        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email a registrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // El rol lo decide el backend
        user.setRole(Role.SUPPORT);

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

}
