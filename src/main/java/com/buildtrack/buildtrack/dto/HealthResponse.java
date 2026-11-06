package com.buildtrack.buildtrack.dto;

public record HealthResponse(
        String application,
        String message,
        String status,
        String version
) {
}
