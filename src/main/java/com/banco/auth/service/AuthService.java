package com.banco.auth.service;

import com.banco.auth.dto.*;
import com.banco.auth.entity.User;
import com.banco.auth.exception.UserAlreadyExistsException;
import com.banco.auth.repository.UserRepository;
import com.banco.auth.security.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
            log.info("Intento de login para usuario: {}", request.getUsername());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            User user = (User) authentication.getPrincipal();
            
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            log.info("Login exitoso para usuario: {}", user.getUsername());

            return AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .userInfo(AuthResponse.UserInfo.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .role(user.getRole().name())
                            .build())
                    .build();

        } catch (AuthenticationException e) {
            log.warn("Credenciales inválidas para usuario: {}", request.getUsername());
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("Intento de registro para usuario: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("El username ya está en uso");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("El email ya está en uso");
        }

        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido. Roles válidos: CLIENTE, EMPLEADO, GERENTE, ADMIN");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(role)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("Registro exitoso para usuario: {}", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationTime())
                .userInfo(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole().name())
                        .build())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            String username = jwtService.extractUsername(refreshToken);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            if (jwtService.validateToken(refreshToken, user)) {
                String newToken = jwtService.generateToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);

                log.info("Token refrescado exitosamente para usuario: {}", user.getUsername());

                return AuthResponse.builder()
                        .token(newToken)
                        .refreshToken(newRefreshToken)
                        .expiresIn(jwtService.getExpirationTime())
                        .userInfo(AuthResponse.UserInfo.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .role(user.getRole().name())
                                .build())
                        .build();
            } else {
                throw new BadCredentialsException("Refresh token inválido");
            }
        } catch (Exception e) {
            log.error("Error al refrescar token: {}", e.getMessage());
            throw new BadCredentialsException("Refresh token inválido");
        }
    }

    public boolean validateToken(String token) {
        try {
            return jwtService.validateToken(token);
        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            return false;
        }
    }
}
