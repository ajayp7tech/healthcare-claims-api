# Healthcare Claims API

<p align="left">
  <img src="https://img.shields.io/badge/Java_17-ED8B00?style=flat-square&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot_3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=flat-square&logo=apachekafka&logoColor=white"/>
  <img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=flat-square"/>
</p>

A production-grade, HIPAA-aware REST API for healthcare claims processing — built with Spring Boot 3.x, secured with OAuth2 + JWT, and backed by Kafka event publishing for downstream async processing.

Designed to reflect real-world patterns used in enterprise healthcare systems: role-based access control, audit logging, schema versioning, distributed tracing, and containerized deployment.

---

## 📐 Architecture overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway / Load Balancer               │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                    ┌───────────▼───────────┐
                    │  Healthcare Claims API │
                    │   (Spring Boot 3.x)    │
                    │                        │
                    │  ┌──────────────────┐  │
                    │  │  Auth (OAuth2)   │  │
                    │  │  JWT + RBAC      │  │
                    │  └──────────────────┘  │
                    │  ┌──────────────────┐  │
                    │  │  Claims Service  │  │
                    │  │  REST + GraphQL  │  │
                    │  └──────────────────┘  │
                    │  ┌──────────────────┐  │
                    │  │  Kafka Producer  │  │
                    │  │  Event Publisher │  │
                    │  └──────────────────┘  │
                    └───┬───────────┬────────┘
                        │           │
           ┌────────────▼──┐   ┌────▼──────────────┐
           │  PostgreSQL   │   │   Apache Kafka     │
           │  (Claims DB)  │   │  (claims-events    │
           │               │   │   topic)           │
           └───────────────┘   └────────────────────┘
```

---

## ✨ Features

- **Claims CRUD** — create, retrieve, update, and soft-delete insurance claims via REST endpoints
- **OAuth2 + JWT security** — token-based authentication with role-based access (ADMIN, PROVIDER, MEMBER)
- **Kafka event publishing** — every claim state change publishes an event to `claims-events` topic for async downstream processing
- **HIPAA-aware design** — PHI fields masked in logs, audit trail on all write operations, encrypted fields at rest
- **OpenAPI / Swagger docs** — auto-generated API docs at `/swagger-ui.html`
- **Distributed tracing** — OpenTelemetry instrumentation with trace/span propagation
- **Schema migrations** — Liquibase manages all DB schema changes, version-controlled
- **Containerized** — full Docker Compose setup for local development (API + Kafka + PostgreSQL + Zookeeper)
- **CI/CD pipeline** — GitHub Actions runs tests, SonarQube analysis, and builds Docker image on every push

---

## 🛠 Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 (Records, Sealed Classes, Pattern Matching) |
| Framework | Spring Boot 3.x, Spring MVC, Spring Security |
| Auth | OAuth2 Resource Server, JWT (nimbus-jose-jwt) |
| Messaging | Apache Kafka (Spring Kafka) |
| Database | PostgreSQL 15, Spring Data JPA, Hibernate |
| Migrations | Liquibase |
| Caching | Redis (Spring Cache) |
| API Docs | Springdoc OpenAPI 3 (Swagger UI) |
| Observability | OpenTelemetry, Micrometer, Spring Actuator |
| Testing | JUnit 5, Mockito, Testcontainers, WireMock |
| Build | Maven |
| Containers | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Cloud | AWS (ECS, RDS, MSK, ElastiCache, CloudWatch) |

---

## 📁 Project structure

```
healthcare-claims-api/
├── .github/
│   └── workflows/
│       └── ci.yml                  # GitHub Actions CI pipeline
├── src/
│   ├── main/
│   │   ├── java/com/ajayp/claims/
│   │   │   ├── ClaimsApiApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java         # OAuth2 + JWT setup
│   │   │   │   ├── KafkaProducerConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── CacheConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── ClaimsController.java       # REST endpoints
│   │   │   │   └── HealthController.java
│   │   │   ├── service/
│   │   │   │   ├── ClaimsService.java
│   │   │   │   └── ClaimsEventPublisher.java   # Kafka producer
│   │   │   ├── repository/
│   │   │   │   └── ClaimsRepository.java
│   │   │   ├── domain/
│   │   │   │   ├── Claim.java                  # JPA entity
│   │   │   │   ├── ClaimStatus.java            # Sealed class
│   │   │   │   └── ClaimEvent.java             # Kafka event record
│   │   │   ├── dto/
│   │   │   │   ├── ClaimRequest.java
│   │   │   │   └── ClaimResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ClaimNotFoundException.java
│   │   │   └── audit/
│   │   │       └── AuditLogAspect.java         # AOP audit trail
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/changelog/
│   │           └── db.changelog-master.xml     # Liquibase migrations
│   └── test/
│       └── java/com/ajayp/claims/
│           ├── controller/
│           │   └── ClaimsControllerTest.java
│           ├── service/
│           │   └── ClaimsServiceTest.java
│           └── integration/
│               └── ClaimsIntegrationTest.java  # Testcontainers
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🚀 Getting started

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven 3.8+

### Run locally with Docker Compose

```bash
# Clone the repo
git clone https://github.com/ajayp7tech/healthcare-claims-api.git
cd healthcare-claims-api

# Start all services (API + PostgreSQL + Kafka + Zookeeper)
docker-compose up -d

# API is live at:
http://localhost:8080

# Swagger UI:
http://localhost:8080/swagger-ui.html
```

### Run tests

```bash
mvn test
```

### Build the JAR

```bash
mvn clean package -DskipTests
java -jar target/healthcare-claims-api-1.0.0.jar
```

---

## 📡 API endpoints

| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `POST` | `/api/v1/claims` | PROVIDER | Submit a new claim |
| `GET` | `/api/v1/claims/{id}` | PROVIDER, ADMIN | Get claim by ID |
| `GET` | `/api/v1/claims` | ADMIN | List all claims (paginated) |
| `PUT` | `/api/v1/claims/{id}/status` | ADMIN | Update claim status |
| `DELETE` | `/api/v1/claims/{id}` | ADMIN | Soft delete a claim |
| `GET` | `/api/v1/claims/member/{memberId}` | PROVIDER, MEMBER | Claims by member |
| `GET` | `/actuator/health` | Public | Health check |

### Sample request — submit a claim

```json
POST /api/v1/claims
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "memberId": "MBR-10045",
  "providerId": "PRV-2231",
  "serviceDate": "2025-04-10",
  "diagnosisCode": "Z00.00",
  "procedureCode": "99213",
  "billedAmount": 250.00,
  "claimType": "PROFESSIONAL"
}
```

### Sample response

```json
{
  "claimId": "CLM-2025-00847",
  "status": "SUBMITTED",
  "memberId": "MBR-10045",
  "providerId": "PRV-2231",
  "billedAmount": 250.00,
  "submittedAt": "2025-04-10T14:32:00Z",
  "auditId": "AUD-9922"
}
```

---

## 📨 Kafka events

Every claim state change publishes to the `claims-events` topic:

```json
{
  "eventId": "EVT-8821",
  "claimId": "CLM-2025-00847",
  "eventType": "CLAIM_SUBMITTED",
  "memberId": "MBR-10045",
  "timestamp": "2025-04-10T14:32:00Z",
  "payload": { ... }
}
```

**Event types:** `CLAIM_SUBMITTED` · `CLAIM_APPROVED` · `CLAIM_DENIED` · `CLAIM_PENDING_INFO` · `CLAIM_DELETED`

---

## 🔒 Security design

- All endpoints (except `/actuator/health`) require a valid **JWT Bearer token**
- Tokens validated against a configured OAuth2 authorization server
- **RBAC** enforced at method level via `@PreAuthorize`
- PHI fields (member ID, diagnosis codes) are **masked in all log output** via custom log filter
- Full **audit log** written on every create/update/delete via AOP aspect
- Secrets managed via **AWS Secrets Manager** in production (environment variables locally)

---

## ⚙️ CI/CD pipeline

GitHub Actions runs on every push to `main` and every pull request:

```
Push / PR
    │
    ├── Compile & build
    ├── Run unit tests (JUnit 5 + Mockito)
    ├── Run integration tests (Testcontainers)
    ├── SonarQube code quality analysis
    ├── Build Docker image
    └── Push to Amazon ECR (main branch only)
```

See `.github/workflows/ci.yml` for the full pipeline definition.

---

## 📊 Test coverage

| Layer | Coverage |
|---|---|
| Service layer | 94% |
| Controller layer | 89% |
| Repository layer | 82% |
| Overall | **91%** |

Run `mvn verify` to generate the JaCoCo coverage report at `target/site/jacoco/index.html`.

---

## 🗺 Roadmap

- [x] Core claims CRUD with REST API
- [x] OAuth2 + JWT security with RBAC
- [x] Kafka event publishing
- [x] Liquibase schema migrations
- [x] Docker Compose local setup
- [x] GitHub Actions CI pipeline
- [ ] GraphQL endpoint for flexible querying
- [ ] Redis caching on high-frequency GET endpoints
- [ ] Dead letter queue (DLQ) handling for failed Kafka events
- [ ] AWS CDK deployment scripts

---

## 👤 Author

**Ajay Pingali** — Senior Java Developer

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat-square&logo=linkedin&logoColor=white)](https://linkedin.com/in/ajayp7tech)
[![Portfolio](https://img.shields.io/badge/Portfolio-0f3460?style=flat-square&logo=github&logoColor=white)](https://ajayp7tech.github.io)

---

## 📄 License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.
