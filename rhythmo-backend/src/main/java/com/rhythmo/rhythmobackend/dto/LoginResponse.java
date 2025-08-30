package com.rhythmo.rhythmobackend.dto;

import java.time.Instant;

public record LoginResponse(String token, Instant expirationTime) {
}
