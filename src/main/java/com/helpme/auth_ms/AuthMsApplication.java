package com.helpme.auth_ms;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthMsApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
		System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
		System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

		SpringApplication.run(AuthMsApplication.class, args);
	}

}
