package com.buildtrack.buildtrack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Test-only JWT key (base64); the real one comes from the JWT_SECRET env var.
@SpringBootTest(properties =
		"app.jwt.secret=dGVzdC1vbmx5LWp3dC1zZWNyZXQta2V5LWZvci11bml0LXRlc3RzLTEyMzQ1Ng==")
class BuildtrackApplicationTests {

	@Test
	void contextLoads() {
	}

}
