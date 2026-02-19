# Marketplace Deal Monitor

Production-style backend system for monitoring marketplace deals.

## Tech Stack

- Java 21
- Spring Boot 3
- PostgreSQL
- JWT Authentication
- Docker & Docker Compose
- Swagger (OpenAPI)

## Features

- User registration & login (JWT)
- Watchlist management (CRUD)
- Scheduled deal monitoring (runs every minute)
- Deal persistence in PostgreSQL
- Containerized deployment

## Run locally

```bash
docker compose up --build
```
Swagger:
http://localhost:8080/swagger-ui/index.html