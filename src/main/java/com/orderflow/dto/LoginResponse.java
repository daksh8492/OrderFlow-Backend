package com.orderflow.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}