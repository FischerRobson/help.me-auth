package com.helpme.auth_ms;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthMsApplication {

	public static void main(String[] args) {
		// Check if running locally with a .env file
		Dotenv dotenv = null;
		try {
			dotenv = Dotenv.load();
		} catch (Exception e) {
			System.out.println(".env file not found, assuming environment variables are set externally.");
		}

		// If .env is loaded, use it; otherwise, rely on system environment variables
		if (dotenv != null) {
			System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
			System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
			System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
			System.setProperty("HOST", dotenv.get("HOST"));
			System.setProperty("HOST", dotenv.get("HOST"));
			System.setProperty("JWT_EXPIRATION_TIME_IN_MINUTES", dotenv.get("JWT_EXPIRATION_TIME_IN_MINUTES"));
		}

		String jdbcUrl = "jdbc:postgresql://" + System.getenv("HOST") + "/" + System.getenv("POSTGRES_DB");
		System.out.println("JDBC URL: " + jdbcUrl);

		SpringApplication.run(AuthMsApplication.class, args);
	}

}
