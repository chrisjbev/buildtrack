# BuildTrack

BuildTrack is a REST API for managing a construction business. It keeps track of **customers**, the **projects** you build for them, and the **tasks** inside each project. Access is secured with JWT, and roles separate admins from regular users.

## Features

- **Customers:** create, view, update and delete customers, and list each customer's projects.
- **Projects:** linked to a customer, with address, start date, expected completion date and status.
- **Tasks:** linked to a project, with due date and status.
- **JWT authentication:** register and log in to receive a token that is valid for 24 hours.
- **Role-based access:**
  - Any logged-in user can read data.
  - Only `ADMIN` users can create, update or delete it.
- **Validation:** request bodies are checked, and errors come back as clear JSON messages.
- **Interactive API docs:** Swagger UI, generated with springdoc-openapi.
- **Tests:** unit, controller and security integration tests.

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (Web MVC, Data JPA, Validation, Security) |
| Database | PostgreSQL, accessed through Hibernate / JPA |
| Authentication | JWT (jjwt 0.12), BCrypt password hashing |
| API docs | springdoc-openapi 3 (Swagger UI) |
| Build | Maven (wrapper included) |
| Testing | JUnit 5, Spring Boot Test, MockMvc |

## Project structure

```
src/main/java/com/buildtrack/buildtrack
├── config/        OpenAPI / Swagger configuration
├── controller/    REST endpoints
├── dto/           Request and response objects
├── exception/     Global error handling
├── model/         JPA entities and enums
├── repository/    Spring Data repositories
├── security/      JWT filter, JWT service, security rules
└── service/       Business logic
```

## Getting started

### Prerequisites

- Java 21
- PostgreSQL running on `localhost:5432`
- Maven is optional, because the project includes `./mvnw`

### 1. Create the database

```sql
CREATE DATABASE buildtrack;
```

Hibernate creates the tables automatically on the first run (`ddl-auto=update`).

### 2. Add local settings and secrets

Create a file named `local.properties` in the project root. It is already listed in `.gitignore`, so it will not be committed.

```properties
# Your PostgreSQL password
DB_PASSWORD=your_db_password
# Base64-encoded key of at least 256 bits. Generate one with: openssl rand -base64 64
JWT_SECRET=your_base64_secret
# Optional. Must be an existing username, which is promoted to ADMIN on startup.
ADMIN_USERNAME=
```

`DB_URL` (default `jdbc:postgresql://localhost:5432/buildtrack`) and `DB_USERNAME` (default `postgres`) can also be set here when your setup differs. Environment variables with the same names override these values.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

You can also run `BuildtrackApplication` from IntelliJ IDEA.

The API starts at `http://localhost:8080`.

### 4. Create an admin user

1. Register a user with `POST /api/auth/register`. New users get the `USER` role.
2. Set `ADMIN_USERNAME` to that username in `local.properties`.
3. Restart the app. On startup, that user is promoted to `ADMIN`.

> The app fails to start if `ADMIN_USERNAME` names a user that does not exist yet. Register the user first.

## Running with Docker

Docker Compose starts both PostgreSQL and the app, so you don't need either installed locally.

1. Create a file named `.env` in the project root. It is listed in `.gitignore`. Docker Compose reads it automatically.

   ```properties
   # Base64-encoded key. Generate one with: openssl rand -base64 64
   JWT_SECRET=your_base64_secret
   ```

2. Build and start the containers:

   ```bash
   docker compose up --build
   ```

| Service | Address |
|---|---|
| API | http://localhost:8081 |
| Swagger UI | http://localhost:8081/swagger-ui/index.html |
| PostgreSQL | `localhost:5433` (database `buildtrack`, user `postgres`) |

The Docker setup uses different ports from a local run (8081 and 5433), so it can run alongside a local PostgreSQL. Data is kept in the `postgres_data` volume.

To stop the containers, run `docker compose down`. To also delete the database volume, add `-v`.

## API documentation (Swagger)

With the app running, open:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

To call protected endpoints from Swagger:

1. Run `POST /api/auth/login` and copy the `token` from the response.
2. Click **Authorize** and paste the token. Swagger adds the `Bearer ` prefix for you.

## Authentication

Send the token in the `Authorization` header on every protected request:

```
Authorization: Bearer <token>
```

| Situation | Response |
|---|---|
| No token, or an invalid token | `401 Unauthorized` |
| Valid token, but the role is not allowed | `403 Forbidden` |
| Wrong username or password at login | `401 Unauthorized` |

## API endpoints

### Auth (public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user (role `USER`) |
| POST | `/api/auth/login` | Log in and receive a JWT |

### Health

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/health` | Authenticated |

### Customers

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/customers` | Authenticated |
| GET | `/api/customers/{id}` | Authenticated |
| GET | `/api/customers/{customerId}/projects` | Authenticated |
| POST | `/api/customers` | ADMIN |
| PUT | `/api/customers/{id}` | ADMIN |
| DELETE | `/api/customers/{id}` | ADMIN |

### Projects

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/projects` | Authenticated |
| GET | `/api/projects/{id}` | Authenticated |
| POST | `/api/projects/customer/{customerId}` | ADMIN |
| PUT | `/api/projects/{id}` | ADMIN |
| DELETE | `/api/projects/{id}` | ADMIN |

### Tasks

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/tasks` | Authenticated |
| GET | `/api/tasks/{id}` | Authenticated |
| GET | `/api/tasks/project/{projectId}` | Authenticated |
| POST | `/api/tasks/project/{projectId}` | ADMIN |
| PUT | `/api/tasks/{id}` | ADMIN |
| DELETE | `/api/tasks/{id}` | ADMIN |

### Admin

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/admin/dashboard` | ADMIN |

## Example requests

**Register**

```json
POST /api/auth/register
{
  "username": "jane",
  "email": "jane@example.com",
  "password": "StrongPass123!"
}
```

**Create a customer** (ADMIN)

```json
POST /api/customers
{
  "name": "Smith Family",
  "email": "smith@example.com",
  "phone": "555-123-4567"
}
```

**Create a project for customer 1** (ADMIN)

```json
POST /api/projects/customer/1
{
  "name": "Kitchen Remodel",
  "description": "Full kitchen renovation",
  "address": "123 Main St",
  "startDate": "2026-01-15",
  "expectedCompletionDate": "2026-04-30",
  "status": "PLANNING"
}
```

**Create a task for project 1** (ADMIN)

```json
POST /api/tasks/project/1
{
  "name": "Demolition",
  "description": "Remove old cabinets and countertops",
  "status": "NOT_STARTED",
  "dueDate": "2026-01-25"
}
```

### Status values

- **Project:** `PLANNING`, `IN_PROGRESS`, `ON_HOLD`, `COMPLETED`, `CANCELLED`
- **Task:** `NOT_STARTED`, `IN_PROGRESS`, `BLOCKED`, `COMPLETED`, `CANCELLED`

## Running tests

```bash
./mvnw test
```

The integration tests start the full application against the configured PostgreSQL database. PostgreSQL must be running before you run the tests.
