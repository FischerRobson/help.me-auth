package com.example.challenge;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeApplication {

	public static void main(String[] args) {
		Dotenv dotenv = null;
		try {
			dotenv = Dotenv.load();
		} catch (Exception e) {
			System.out.println(".env file not found, assuming environment variables are set externally.");
		}

		if (dotenv != null) {
			System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
			System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
			System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
			System.setProperty("HOST", dotenv.get("HOST"));
			System.setProperty("HOST", dotenv.get("HOST"));
			System.setProperty("JWT_EXPIRATION_TIME_IN_MINUTES", dotenv.get("JWT_EXPIRATION_TIME_IN_MINUTES"));
			System.setProperty("CORS_ALLOWED_ORIGINS", dotenv.get("CORS_ALLOWED_ORIGINS"));
		}

		String jdbcUrl = "jdbc:postgresql://" + System.getenv("HOST") + "/" + System.getenv("POSTGRES_DB");
		System.out.println("JDBC URL: " + jdbcUrl);

		SpringApplication.run(ChallengeApplication.class, args);
	}

}
