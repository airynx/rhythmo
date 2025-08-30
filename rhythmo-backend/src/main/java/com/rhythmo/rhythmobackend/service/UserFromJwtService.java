package com.rhythmo.rhythmobackend.service;

import com.rhythmo.rhythmobackend.exception.NoSuchEntityException;
import com.rhythmo.rhythmobackend.model.User;
import com.rhythmo.rhythmobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFromJwtService {
    private final UserRepository userRepository;
    public User usingRepo(Jwt jwt) throws NoSuchEntityException {
        String username = jwt.getSubject();
        return userRepository.getByUsername(username).orElseThrow(() -> new NoSuchEntityException(username));
    }
}
