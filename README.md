# RuralXperience — Spring Boot API

Backend API for the RuralXperience immersive rural experience booking platform.

**Stack:** Spring Boot 3.4 · Java 21 · PostgreSQL 16 · Redis 7 · JWT Auth

---

## Quick Start

### 1. Prerequisites
- Java 21 (use SDKMAN: `sdk install java 21.0.5-tem`)
- Maven 3.9+
- Docker Desktop

### 2. Start the infrastructure
```bash
docker compose up -d
```
This starts PostgreSQL on `5432`, Redis on `6379`, and MailHog on `1025` (UI at http://localhost:8025).

### 3. Run the API
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
The API starts on **http://localhost:8080**. Flyway automatically runs all migrations on startup.

---

## Default credentials

| Account | Email | Password | Role |
|---------|-------|----------|------|
| Admin   | admin@ruralxperience.com | Admin@2026! | ADMIN |

Register new EXPLORER or HOST accounts via `POST /api/v1/auth/register`.

---

## Key Endpoints

### Auth
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/v1/auth/register` | Public | Register explorer or host |
| POST | `/api/v1/auth/login` | Public | Login, get JWT tokens |
| POST | `/api/v1/auth/refresh` | Public | Refresh access token |

### Experiences
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/api/v1/experiences` | Public | Search & filter experiences |
| GET | `/api/v1/experiences/{id}` | Public | Get experience details |
| POST | `/api/v1/experiences` | HOST | Create experience |
| PUT | `/api/v1/experiences/{id}` | HOST | Update experience |
| POST | `/api/v1/experiences/{id}/submit` | HOST | Submit for admin review |

### Bookings
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/v1/bookings` | EXPLORER | Create booking request |
| GET | `/api/v1/bookings/my` | EXPLORER | My bookings |
| GET | `/api/v1/bookings/host` | HOST | Incoming bookings |
| POST | `/api/v1/bookings/{id}/confirm` | HOST | Confirm booking |
| POST | `/api/v1/bookings/{id}/decline` | HOST | Decline booking |
| POST | `/api/v1/bookings/{id}/cancel` | Any | Cancel booking |

### Reviews
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/v1/reviews` | EXPLORER | Post review (completed bookings only) |
| GET | `/api/v1/reviews/experience/{id}` | Public | Get reviews for experience |
| POST | `/api/v1/reviews/{id}/reply` | HOST | Add host reply |

### Admin
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/api/v1/admin/experiences/pending` | ADMIN | Moderation queue |
| POST | `/api/v1/admin/experiences/{id}/approve` | ADMIN | Approve experience |
| POST | `/api/v1/admin/experiences/{id}/reject` | ADMIN | Reject with reason |
| GET | `/api/v1/admin/users` | ADMIN | List all users |
| POST | `/api/v1/admin/users/{id}/disable` | ADMIN | Disable user |
| GET | `/api/v1/admin/stats` | ADMIN | Platform statistics |

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/ruralxperience` | PostgreSQL URL |
| `DB_USER` | `rxp_user` | DB username |
| `DB_PASSWORD` | `rxp_secret` | DB password |
| `JWT_SECRET` | *(dev default)* | Must be 32+ chars in prod |
| `REDIS_HOST` | `localhost` | Redis host |
| `CORS_ORIGINS` | `http://localhost:4200` | Allowed Angular origin |
| `EMAIL_ENABLED` | `true` | Set `false` in tests |

---

## Angular Integration (Chapter 5+)

The API is ready for the Angular book starting from Chapter 5.
Base URL: `http://localhost:8080`

All endpoints return `application/json`. Authentication uses `Bearer <access_token>` in the `Authorization` header.

CORS is pre-configured for `http://localhost:4200` (Angular dev server).
