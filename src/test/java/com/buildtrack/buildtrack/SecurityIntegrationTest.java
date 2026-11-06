package com.buildtrack.buildtrack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedUserCannotAccessCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedUserCannotAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedUserCannotAccessAdminDashboard() throws Exception {
        String username = "securitytest" + java.util.UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "email": "%s@example.com",
                                    "password": "TestPassword123!"
                                }
                                """.formatted(username, username)))
                .andExpect(status().isCreated());

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "TestPassword123!"
                                }
                                """.formatted(username)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(loginResponse)
                .get("token")
                .asText();

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
