<h1>AI Website Generator — Backend</h1>

<p>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20AI-LLM%20Integration-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/Grafana-Monitoring-F46800?style=for-the-badge&logo=grafana&logoColor=white"/>
</p>

An **AI-powered backend** that generates complete multi-page websites from plain text prompts.
Built with Spring Boot 3, Spring AI, JWT-based RBAC, full observability stack (Prometheus + Grafana + Kibana),
and containerized with Docker Compose — demonstrating production-grade backend architecture.

---

## ✨ Features

- 🤖 **AI website generation** — generates complete website code from text prompts via LLM integration (Spring AI)
- 🔐 **JWT authentication + RBAC** — role-based access control with fine-grained API permissions
- 🗄️ **PostgreSQL + MinIO** — relational data persistence + object storage for generated website assets
- 📊 **Full observability** — Prometheus metrics, Grafana dashboards, Kibana log analysis
- 🐳 **Docker Compose** — one command spins up the entire stack
- 🏗️ **MVC layered architecture** — Controller → Service → Repository, clean separation of concerns

---

## 🏗️ Architecture

            Client Request
                 │
                 ▼
      ┌─────────────────────┐
      │    API Gateway /    │
      │   REST Controllers  │  ← JWT verified on every request
      └──────────┬──────────┘
                 │
      ┌──────────▼──────────┐
      │    Service Layer    │  ← Business logic, AI prompt handling, RBAC
      └──────┬──────┬───────┘
             │      │
      ┌──────▼──┐ ┌─▼──────────────┐
      │ JPA /   │ │  Spring AI      │
      │PostgreSQL│ │  (LLM calls)   │
      │(metadata)│ │  OpenRouter/   │
      └──────────┘ │  Ollama        │
                   └────────────────┘
             │
      ┌──────▼──────┐
      │    MinIO    │  ← Stores generated website files
      └─────────────┘Observability Stack (Monitoring/)
      ┌──────────────┐  ┌─────────┐  ┌─────────┐
      │  Prometheus  │  │ Grafana │  │ Kibana  │
      │  (metrics)   │  │(dashbrd)│  │ (logs)  │
      └──────────────┘  └─────────┘  └─────────┘

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.x, Spring MVC |
| AI Integration | Spring AI, OpenRouter / Ollama (LLM) |
| Auth & Security | Spring Security, JWT, RBAC |
| Database | PostgreSQL + Spring Data JPA |
| File Storage | MinIO (S3-compatible object storage) |
| Observability | Prometheus, Grafana, Logstash, Kibana |
| Containerization | Docker, Docker Compose |
| Build | Maven |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Docker + Docker Compose
- An LLM API key (OpenRouter free tier works)

### 1. Clone the repo
```bash
git clone https://github.com/warisamir/AI-website-generator-backend.git
cd AI-website-generator-backend
```

### 2. Create environment file
Create `.env` in the root directory:
```env
# Database
DB_URL=jdbc:postgresql://postgres:5432/ai_generator
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRY=86400000

# AI / LLM
OPENROUTER_API_KEY=your_openrouter_key
OLLAMA_BASE_URL=http://localhost:11434

# MinIO
MINIO_URL=http://minio:9000
MINIO_ACCESS_KEY=your_minio_access_key
MINIO_SECRET_KEY=your_minio_secret_key
```

### 3. Start the full stack
```bash
docker compose up --build
```

| Service | URL |
|---------|-----|
| Backend API | `http://localhost:8080` |
| Grafana dashboard | `http://localhost:3000` |
| MinIO console | `http://localhost:9001` |
| Prometheus | `http://localhost:9090` |

---

## 📡 API Endpoints

### Auth

POST   /api/auth/register    — Register new user
POST   /api/auth/login       — Login, returns JWT token

### Website Generation
POST   /api/generate         — Generate website from text prompt (JWT required)
GET    /api/generate/{id}    — Get previously generated website
GET    /api/generate/history — Get user's generation history

### Admin (ADMIN role required)
GET    /api/admin/users      — List all users
DELETE /api/admin/users/{id} — Delete user
GET    /api/admin/logs       — View system logs

---

## 🔑 Key Technical Concepts

- **Spring AI integration** — abstracts LLM provider calls (OpenRouter/Ollama), handles prompt engineering and response parsing
- **RBAC** — Spring Security with custom `@PreAuthorize` annotations, roles stored in JWT claims
- **MinIO as object storage** — generated HTML/CSS/JS files stored as objects, retrieved by presigned URL
- **Prometheus + Grafana** — custom metrics for generation latency, request count, error rates
- **Logstash + Kibana** — structured JSON logs shipped to ELK for searchable log analysis

---

## ⚠️ Security Note

Never commit real credentials. The `.env` file in this repo is for reference only — replace all values before running. Add `.env` to your `.gitignore` to prevent accidental commits.

---

## 📁 Project Structure
      AI-website-generator-backend/
      ├── src/
      │   └── main/java/
      │       ├── controller/     ← REST endpoints
      │       ├── service/        ← Business logic + AI calls
      │       ├── repository/     ← Spring Data JPA
      │       ├── model/          ← JPA entities
      │       ├── security/       ← JWT filter, RBAC config
      │       └── config/         ← Spring AI, MinIO, Prometheus config
      ├── Monitoring/             ← Grafana + Prometheus config files
      ├── lovable/log/            ← Log samples
      ├── docker-compose.yml      ← Full stack orchestration
      └── pom.xml

---

## 👤 Author

**Waris Amir** — Java Backend Engineer, Bangalore
[LinkedIn](https://linkedin.com/in/waris-amir-0387461b3) · [Portfolio](https://portfolio-git-main-warisamirs-projects.vercel.app/) · [GitHub](https://github.com/warisamir)
