package com.buildtrack.buildtrack.controller;

import com.buildtrack.buildtrack.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public HealthResponse checkHealth() {

        return new HealthResponse(
                "BuildTrack",
                "Construction management system is running!",
                "ACTIVE",
                "1.0.0"
        );
    }
}
