# IncidentIQ

## Offline Enterprise Incident Investigation Platform

IncidentIQ is a Java/Spring Boot application for investigating payment-service incidents using transaction data, service-health signals, deployment history, incident records, and locally stored operational runbooks.

The project is an **offline-first MVP** for an enterprise incident-intelligence workflow. It demonstrates explainable anomaly detection, deterministic investigation orchestration, keyword-based document retrieval, role checks, audit logging, and human approval requirements for high-impact recommendations.

> Example question:  
> “Why did payment failures increase this week, which services may be affected, and what should the operations team investigate next?”

---

## Problem

When payment failures increase, engineers may need to check multiple systems manually:

- Transaction dashboards and payment records
- Service-health status
- Recent deployment history
- Previous incidents
- Operational runbooks and troubleshooting guides

This investigation can be slow, inconsistent, and difficult to audit. IncidentIQ brings these steps into one evidence-backed workflow.

```text
Incident Question
      ↓
Role Validation + Audit Log
      ↓
Transaction and Service Investigation
      ↓
Anomaly Detection + Offline Document Retrieval
      ↓
Evidence-Backed Diagnosis
      ↓
Recommendation with Risk and Approval Requirement
```

---

## Current MVP Features

| Capability | Status | Current Implementation |
|---|---|---|
| Spring Boot REST API | Implemented | Java/Spring Boot API foundation |
| Local database | Implemented | H2 in-memory database for local development |
| Payment anomaly detection | Implemented | Explainable comparison of recent versus baseline failure rates |
| Enterprise incident data | Implemented | Synthetic customer, payment, service, deployment, and incident records |
| Investigation orchestration | Implemented | Deterministic multi-step Java service workflow |
| Document retrieval | Implemented | Offline in-memory keyword retrieval over runbooks and incident documents |
| Retrieval ranking | Basic | Keyword matching; no embedding or vector similarity ranking yet |
| RBAC | MVP implemented | Server-side role permission checks |
| Audit logging | MVP implemented | Investigation authorization events can be recorded |
| Human approval gate | MVP implemented | High-risk recommendations indicate when approval is required |
| Semantic/vector retrieval | Planned | PostgreSQL + pgvector or local embedding model |
| LLM synthesis/tool calling | Planned | Structured tool interfaces designed for future integration |
| OAuth2/JWT authentication | Planned | Spring Security plus enterprise identity-provider integration |
| External Jira/Slack actions | Planned | Recommendation-only workflow in current MVP |
| Automated tests and CI | In progress | Unit tests and GitHub Actions planned |

---

## Architecture

```text
                    API Client / Future Web UI
                                │
                                ▼
                    Spring Boot REST Controllers
                                │
                                ▼
                Investigation Agent / Orchestrator
                                │
         ┌──────────────────────┼──────────────────────┐
         ▼                      ▼                      ▼
  Anomaly Detection      Offline Retrieval        Enterprise Data Tools
         │                      │                      │
         ▼                      ▼                      ▼
 Payment Records       Runbooks / Incidents    Services / Deployments /
                                               Customers / Audit Logs
                                │
                                ▼
                  Evidence-Backed Recommendation
                                │
                                ▼
                     Human Approval Requirement
```

### Current investigation workflow

```text
1. User submits an incident-investigation request.
2. The system validates the user role.
3. The system writes an audit event.
4. The investigation service examines payment data.
5. The anomaly service compares current failures with the baseline.
6. The system checks service health and recent deployments.
7. The retrieval service searches offline runbooks and past incidents.
8. The system returns a diagnosis, evidence, risk, and recommendation.
9. High-risk recommendations require human approval before action.
```

---

## Technology Stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| API | Spring Web / REST |
| Data access | Spring Data JPA |
| Local database | H2 |
| Validation | Jakarta Bean Validation |
| Operations | Spring Boot Actuator |
| Build tool | Maven |
| Development environment | VS Code or IntelliJ IDEA |
| Retrieval | Offline keyword-based document retrieval |
| Future database | PostgreSQL |
| Future vector search | pgvector or another vector database |
| Future embeddings | Local embedding model or hosted embedding provider |
| Future AI orchestration | LLM tool calling with structured outputs |

Spring Boot applications can be run from Maven with `mvn spring-boot:run`, and the Maven plugin supports packaging and running executable Spring Boot applications. [web:132][web:166]

---

## Project Structure

```text
incidentiq/
├── pom.xml
├── README.md
├── .gitignore
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/acme/intelligence/
    │   │       ├── EnterpriseAiApplication.java
    │   │       │
    │   │       ├── controller/
    │   │       │   ├── HealthController.java
    │   │       │   └── InvestigationController.java
    │   │       │
    │   │       ├── domain/
    │   │       │   ├── Customer.java
    │   │       │   ├── Payment.java
    │   │       │   ├── ServiceStatus.java
    │   │       │   ├── Deployment.java
    │   │       │   ├── Incident.java
    │   │       │   └── AuditLog.java
    │   │       │
    │   │       ├── dto/
    │   │       │   ├── InvestigationRequest.java
    │   │       │   └── InvestigationResponse.java
    │   │       │
    │   │       ├── service/
    │   │       │   ├── AnomalyDetectionService.java
    │   │       │   ├── AuditService.java
    │   │       │   ├── InvestigationAgent.java
    │   │       │   ├── RagService.java
    │   │       │   └── SecurityService.java
    │   │       │
    │   │       └── support/
    │   │           └── Role.java
    │   │
    │   └── resources/
    │       └── application.properties
    │
    └── test/
        └── java/
            └── com/acme/intelligence/
```

---

## Run Locally

### Prerequisites

- Java 21 or later
- Maven 3.9 or later
- Git
- VS Code or IntelliJ IDEA

Verify your installation:

```bash
java --version
mvn --version
```

### Start the application

Clone the repository:

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/incidentiq.git
cd incidentiq
```

Run the project:

```bash
mvn spring-boot:run
```

For projects generated with the Maven wrapper:

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS/Linux**

```bash
./mvnw spring-boot:run
```

The API starts locally on:

```text
http://localhost:8080
```

---

## API Endpoints

### Application health

```http
GET /api/v1/health
```

Example response:

```json
{
  "status": "ok"
}
```

Test it:

```bash
curl http://localhost:8080/api/v1/health
```

### Spring Boot operational health

```http
GET /actuator/health
```

Test it:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

Spring Boot Actuator provides built-in production-oriented endpoints, including the application-health endpoint at `/actuator/health`. [web:22][web:171]

### Investigation endpoint

```http
POST /api/v1/investigate
Content-Type: application/json
```

Example request:

```bash
curl -X POST http://localhost:8080/api/v1/investigate \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Why did payment failures increase this week?",
    "customer": "ACME Payments",
    "userRole": "ANALYST"
  }'
```

Example response shape:

```json
{
  "diagnosis": "Payment failures are above the expected baseline. Payment API degradation and a recent deployment should be investigated.",
  "evidence": [
    {
      "source": "anomaly-detection",
      "detail": "The current payment failure rate exceeds the historical baseline."
    },
    {
      "source": "service-health",
      "detail": "Payment API is reporting degraded status."
    },
    {
      "source": "offline-runbook",
      "detail": "Review recent deployments, gateway health, regional errors, and retry configuration."
    }
  ],
  "toolsUsed": [
    "query_transactions",
    "detect_anomaly",
    "get_service_health",
    "get_deployment_history",
    "search_docs"
  ],
  "recommendation": {
    "summary": "Escalate to payment engineering and review the most recent deployment.",
    "risk": "high",
    "approvalRequired": true
  }
}
```

---

## Offline Retrieval Design

The current version does **not** call an embedding API, an LLM, or a cloud vector database.

Instead, it uses an offline document list containing synthetic operational knowledge, such as:

- Payment-failure troubleshooting runbooks
- Authorization error runbooks
- Past incident summaries
- Deployment notes
- Escalation guidance

The current `RagService` performs keyword-based matching against this local content.

```text
Question
   ↓
Normalize keywords
   ↓
Search local runbooks and incident documents
   ↓
Return matching document snippets
   ↓
Attach snippets as investigation evidence
```

### Why offline first?

The offline MVP keeps development simple, reproducible, and free of API-key or cloud-service dependencies. It also makes it easier to demonstrate the investigation workflow before introducing external model providers or sensitive enterprise data.

### Current limitations

Offline keyword retrieval does not understand semantic meaning as well as embeddings. For example, a document about “authorization rejections” may not be found for a query using only “payment failures” unless the relevant terms overlap.

---

## Roles and Human Approval

| Role | Can Investigate | Can Approve High-Risk Actions | Can Manage Documents |
|---|---:|---:|---:|
| `ADMIN` | Yes | Yes | Planned |
| `ANALYST` | Yes | No | No |
| `SUPPORT_ENGINEER` | Yes | No | No |
| `VIEWER` | No | No | No |

The MVP accepts a role as part of the request for local demonstration. This is not production authentication.

High-risk recommendations are intentionally separated from action execution:

```text
Investigation
      ↓
Evidence and Risk Classification
      ↓
Recommended Operational Action
      ↓
Human Approval Required
      ↓
Future Jira, Slack, or Incident-Management Integration
```

This design prevents an automated system from directly taking high-impact operational actions.

---

## Anomaly Detection

The current anomaly detector is intentionally transparent.

```text
Current failure rate = failed payments / total payments
Baseline failure rate = failed payments / total historical payments
Deviation = current failure rate - baseline failure rate
```

| Failure-rate deviation | Risk classification |
|---|---|
| Greater than 8 percentage points | High |
| Greater than 3 percentage points | Medium |
| 3 percentage points or lower | Low |

Future model options include:

- Isolation Forest
- Time-series anomaly detection
- Seasonal baselines
- Bayesian change-point detection
- Supervised payment-failure classification
- Drift detection and model monitoring

---

## Roadmap

### Semantic retrieval

- Add document-ingestion workflow
- Add text chunking and metadata
- Add local embedding model support
- Add PostgreSQL with pgvector
- Add semantic similarity search and reranking
- Return ranked, source-attributed citations

pgvector is an open-source PostgreSQL extension for vector similarity search and can store vectors alongside application data. [web:116][web:168]

### Security

- Add Spring Security
- Add OAuth2/OIDC and JWT validation
- Add tenant-aware authorization filters
- Add secrets management
- Add prompt-injection test cases
- Add tool allowlists and rate limiting

### Agentic workflow

- Add structured LLM tool calling
- Add tool-selection evaluation
- Add fallback behavior and retries
- Add clarification questions
- Add human action-approval endpoints

### Quality and delivery

- Add unit and integration tests
- Add GitHub Actions CI
- Add Docker Compose
- Add PostgreSQL migrations with Flyway
- Add observability with metrics, traces, and structured logs
- Add a benchmark suite of synthetic incident cases

---

## Portfolio Scope

IncidentIQ is designed as an FDE / Applied AI portfolio project. It focuses on how an AI-enabled platform should be scoped for enterprise operations:

- Start with an ambiguous operational problem
- Connect relevant business and technical data
- Produce evidence-backed investigation results
- Separate read-only investigation from high-impact actions
- Apply access controls and audit logs
- Use human approval for consequential actions
- Define a realistic roadmap for semantic RAG, LLM orchestration, security, and production deployment

### Honest implementation statement

> The current repository implements an offline Spring Boot MVP with deterministic investigation orchestration, explainable anomaly detection, local keyword-based retrieval, role checks, and an approval-required recommendation contract. Vector search, embeddings, LLM tool calling, OAuth2/JWT authentication, and external action integrations are planned future enhancements.

---

## Author

**Munvith Katpelly**

Target roles:

```text
Forward Deployed AI Engineer
Applied AI Engineer
AI Solutions Engineer
Machine Learning Engineer
Enterprise AI Platform Engineer
```

```text
GitHub: https://github.com/Mkatpelly
LinkedIn: https://linkedin.com/in/Munvith_Katpelly
```
