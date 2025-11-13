# 🚀 Sky Auth API

This project contains a Spring Boot authentication/authorization API with user management, external project management, and JWT-based security.
It also includes a Docker setup for running Postgres easily.

### ⚠️ Notes & Scope Considerations

Some architectural and implementation decisions in this project were intentionally simplified due to the nature of the challenge and the expected time constraints. Since this is not a real production codebase, I prioritized delivering a clear, functional, and well-structured solution rather than polishing every small detail. Certain aspects — such as advanced validation rules, deeper domain modeling, more granular error codes, exhaustive integration tests, and production-grade hardening — were minimized or left out to keep the focus on the core objectives of the assessment.

Even with these intentional simplifications, the project still demonstrates solid architecture, clean code practices, security awareness, and a strong understanding of Spring Boot, REST, Docker, testing, and Java development at a senior level.

## 📦 Requirements

Docker & Docker Compose

JDK 21+ (only if running the API locally without Docker)

Insomnia (optional, for testing endpoints)

## 📁 Project Structure
```bazaar
project/
 ├── src/
 ├── docker-compose.yml
 ├── .env.example
 ├── README.md
```

## 🔧 Setup & Environment Variables

Rename `.env.example` to `.env`. ()

⚠️ The API will NOT start without `.env`, so make sure it exists.

## 🐳 Running with Docker Compose

▶️ Start the database:
```sh
docker compose up -d
```

⏹ Stop:
```sh
docker compose down
```

🗃 Check running containers:
```sh
docker ps
```

Api will be available at `http://localhost:8080` and Postgres at `localhost:5432
`

## 🔥 Testing With Insomnia

You can import the collection below (JSON included here in README).
Just copy the JSON, then inside Insomnia:

Application → Import → From Clipboard

The collection contains ready-to-use endpoints:

✔️ Auth

Login

Create new user

Me (get authenticated user)

✔️ Users

List users

User details (yours & others)

Update user

Delete user (admin only)

Change role

✔️ External Projects

Create project

List projects

Delete project

This makes it easy to test authentication, cookies, admin actions and more.

## 🧪 API Testing with Swagger UI

This project includes interactive API documentation powered by Swagger / OpenAPI using springdoc-openapi.
It allows you to explore, test, and validate all endpoints without needing external tools like Insomnia or Postman.

### 📌 Accessing Swagger UI

Once the application is running (via Docker or IntelliJ), open your browser and visit:

👉 http://localhost:8080/swagger-ui/index.html

### 🔐 Authentication via Swagger

After logging in using your /auth/login endpoint and receiving your JWT:

Click the Authorize button at the top right of Swagger UI

Enter your token like this:
```
Bearer <your-jwt>
```
Click Authorize and then Close

You are now authenticated, and Swagger will automatically include your JWT in every request.

## 📝 Additional Notes

JWT is stored in an HttpOnly cookie, so Insomnia automatically reuses it.

The /auth/logout endpoint clears the cookie.

Roles:

ADMIN → can delete users & manage roles

USER → restricted privileges

All protected routes require a valid JWT cookie.