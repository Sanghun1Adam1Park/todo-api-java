# REST API for To-Do Application 

### Main goal of this project
The main goal of this project is to learn about the basic of backend development rather than building deployable system. 

To learn about followings: 
- How pagination works 
- JWT
- Hashing

## Database Schema

### todos

| id | ownerId  | priority        | item | status | createdAt | updatedAt | completedAt | deadline |
|----|----------|-----------------|------|--------| -----------|-----------| ----------- | ----- |
| INT (PK, Unique) | INT (FK) | TEXT (NOT Null) | TEXT (Not Null) | TEXT | TIMESTAMP (Default NOW()) | TIMESTAMP (Default NOW()) | TIMESTAMP | TIMESTAMP |
- Constraints
  - (id, ownderId) must be unique
  - priority can only be following:
    - Critical
    - High
    - Moderate
    - Low
  - status can only be following:
    - Idel
    - Pending
    - Complete
- Index
---

### users

| userId | userName | password | joined |
|--------|----------|----------| --- | 
| INT (PK, Unique) | VARCHAR(255) (Not Null, Unique) | VARCHAR(255) (Not Null, stores hash) | TIMESTAMP (Default NOW()) |

## Endpoints

### Authentication
- `POST /api/register`
  - Create a new user (username, password, email).
- `POST /api/login`
  - Authenticate user and return token (e.g. JWT).
- `POST /api/logout`
  - Invalidate current token/session.

---

### Users
- `GET /api/users/:id`
  - Get user details (excluding password).
- `PUT /api/users/:id`
  - Update user info (username, password).
- `DELETE /api/users/:id`
  - Delete user account.

---

### Todos
- `GET /api/todos`
  - Get all todos for the logged-in user.
- `GET /api/todos/:id`
  - Get a single todo by ID.
- `POST /api/todos`
  - Create a new todo (priority, item, status).
- `PUT /api/todos/:id`
  - Update a todo (priority, item, status).
- `PATCH /api/todos/:id/status`
  - Update only the status (e.g. mark as done).
- `DELETE /api/todos/:id`
  - Delete a todo.

---

### GET /api/todos, Sorting and Pagination

#### Query params
- `sort` (optional): one of `priority`, `createdAt`, `updatedAt`, `status`, `id`
- `order` (optional): `asc` | `desc` (default: `asc`)
- `page` (optional): integer >= 1 (default: `1`)
- `limit` (optional): integer 1–100 (default: `20`)
- `status` (optional): e.g. `pending`, `done` (filter)
- `priority` (optional): integer (filter)
- `search` (optional): free text to match `item` (basic search)

#### Examples
- `/api/todos?sort=priority&order=desc&page=2&limit=10`
- `/api/todos?status=pending&sort=createdAt&order=desc`
- `/api/todos?priority=1&search=report`

#### Response (200)

```
{
  "data": [
    {
      "id": 42,
      "priority": 1,
      "item": "Write weekly report",
      "status": "pending",
      "createdAt": "2025-09-03T10:12:00Z",
      "updatedAt": "2025-09-03T10:12:00Z"
    }
  ],
  "meta": {
    "page": 2,
    "limit": 10,
    "totalItems": 57,
    "totalPages": 6,
    "sort": "priority",
    "order": "desc"
  }
}
```

## System Design

![System Design](system_design.png)

It is a rather simple design. The server responds to requests made via endpoints, some endpoints being exclusive to clients with valid JWTs.
•	Clients interact with the API via HTTP requests.
•	Authentication and authorization are handled via JWT.
•	Passwords are stored as hashes, never in plain text.
•	Database stores users and todos with timestamps for auditing and sorting.

## Technology

- **Programming Language**: Java 17
- **Framework**: Spring Boot 3.5.5
- **Core Dependencies**:
  - `spring-boot-starter-web` → Build REST APIs
  - `spring-boot-starter-data-jpa` → ORM and database access
  - `spring-boot-starter-security` → Authentication & authorization
  - `spring-boot-starter-validation` → Input validation with Bean Validation (`@Valid`, `@NotNull`, etc.)
- **Database**:
  - H2 (in-memory database for development and testing)
- **Authentication & Security**:
  - JSON Web Tokens (JWT) via `jjwt-api`, `jjwt-impl`, and `jjwt-jackson`
  - Password hashing via Spring Security (e.g., BCryptPasswordEncoder)
- **API Documentation**:
  - Springdoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`) → Swagger UI at `/swagger-ui.html`
- **Testing**:
  - `spring-boot-starter-test` (JUnit, Mockito)
  - `spring-security-test` (security-specific testing support)
  - H2 (used in-memory for fast testing)
- **Build & Tools**:
  - Maven (dependency management & build tool)
  - Lombok (reduce boilerplate code)
  - Spring Boot Maven Plugin (packaging and running the app)  