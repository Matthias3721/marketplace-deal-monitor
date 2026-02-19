# Marketplace Deal Monitor

Production-style backend system for monitoring marketplace deals.

---

## Overview

Marketplace Deal Monitor is a Spring Boot backend application designed to monitor marketplace listings and notify users about relevant deals.

The project focuses on:

- Clean backend architecture
- Containerized deployment
- Scheduled background processing
- Secure authentication (JWT)
- Self-hosted production-like infrastructure

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- GitHub Actions (CI)
- Ubuntu LTS (self-managed server)
- Tailscale (private network access)

---

## Architecture

Client (Tailscale)
        ↓
Docker Host (Ubuntu LTS)
        ↓
Spring Boot App (8080 internal)
        ↓
PostgreSQL (internal Docker network)

- Database is not publicly exposed
- Containers restart automatically (`restart: unless-stopped`)
- Application runs 24/7 on home server

---

## Features

- User registration & login (JWT authentication)
- Watchlist management (CRUD)
- Scheduled background deal monitoring
- Marketplace integration (mock / ready for real adapter)
- Containerized PostgreSQL
- Health-based container startup

---

## CI

GitHub Actions:
- Build
- Run tests
- Validate project integrity

---

## Run Locally

```bash
docker compose up --build
```

Swagger:
http://localhost:8080/swagger-ui/index.html

## Production Deployment

The application is deployed on a self-managed Ubuntu LTS server using Docker Compose.
Deployment flow:

Windows → GitHub → Server (git pull) → docker compose up -d --build

The service:
-Runs 24/7
-Restarts automatically after reboot
-Is isolated within Docker network
-Is accessible via private Tailscale network




