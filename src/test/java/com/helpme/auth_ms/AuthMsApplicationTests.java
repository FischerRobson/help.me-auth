package com.helpme.auth_ms;

import com.helpme.auth_ms.repositories.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=none",
		"JWT_SECRET=testsecret",
		"JWT_EXPIRATION_TIME_IN_MINUTES=60",
		"CORS_ALLOWED_ORIGINS=http://localhost:3000"
})
class AuthMsApplicationTests {

	@BeforeAll
	static void setUp() {
		try {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

			setEnv("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
			setEnv("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
			setEnv("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
			setEnv("JWT_SECRET", dotenv.get("JWT_SECRET"));
			setEnv("JWT_EXPIRATION_TIME_IN_MINUTES", dotenv.get("JWT_EXPIRATION_TIME_IN_MINUTES"));
			setEnv("CORS_ALLOWED_ORIGINS", dotenv.get("CORS_ALLOWED_ORIGINS"));
		} catch (Exception e) {
			System.out.println("Warning: .env not loaded. Using environment variables.");
		}
	}

	private static void setEnv(String key, String value) {
		if (value != null && System.getProperty(key) == null) {
			System.setProperty(key, value);
		}
	}


	@Test
	void contextLoads() {
	}

}
