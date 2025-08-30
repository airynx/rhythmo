package com.rhythmo.rhythmobackend.service;

import com.rhythmo.rhythmobackend.dto.LoginRequest;
import com.rhythmo.rhythmobackend.dto.LoginResponse;
import com.rhythmo.rhythmobackend.dto.RegisterRequest;
import com.rhythmo.rhythmobackend.model.User;
import com.rhythmo.rhythmobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;

    private static final MacAlgorithm MAC_ALGORITHM = MacAlgorithm.HS256;

    @Value("${security.jwt.expiration_time}")
    private long EXPIRATION_TIME_SEC;

    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is in use.");
        }
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is in use.");
        }
        return userRepository.save(
                new User(
                        registerRequest.username(),
                        registerRequest.email(),
                        passwordEncoder.encode(registerRequest.password()))
        );
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        Jwt token = formToken(authentication);
        return new LoginResponse(token.getTokenValue(), token.getExpiresAt());
    }

    private Jwt formToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(EXPIRATION_TIME_SEC);

        JwsHeader jwsHeader = JwsHeader.with(MAC_ALGORITHM).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expirationTime)
                .claim("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    }
}
