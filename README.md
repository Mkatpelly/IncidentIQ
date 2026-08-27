# IncidentIQ

## Enterprise AI Investigation & Decision Intelligence Platform

IncidentIQ is a Java/Spring Boot application that helps engineering and operations teams investigate payment failures using transaction data, anomaly detection, service-health signals, deployment history, incident records, and operational runbooks.

It simulates a Forward Deployed AI Engineer / Applied AI Engineer engagement for a fictional enterprise customer, **ACME Payments**.

> Example question:  
> “Why did payment failures increase this week, which customers are affected, what is the likely cause, and what should we do?”

---

## Problem

When payment failures spike, engineers often investigate manually across dashboards, transaction databases, deployment logs, incident-management tools, and runbooks. This process is slow, inconsistent, and difficult to audit.

IncidentIQ turns that fragmented process into an evidence-backed workflow:

```text
User Question
      ↓
Payment and Service Investigation
      ↓
Anomaly Detection + RAG + Operational Data
      ↓
Evidence-Backed Diagnosis
      ↓
Recommended Action
      ↓
Human Approval for High-Impact Actions
```

### Business targets

| Metric | Current | Target |
|---|---:|---:|
| Investigation time | 35 minutes | Less than 10 minutes |
| Root-cause accuracy | 65% | Greater than 90% |
| Manual steps | 12 | Less than 5 |
| High-impact actions | Manual | Human-approved and auditable |

> These are project targets, not measured claims. Final results should be reported only after testing against historical or synthetic incident cases.

---

## Features

- **Payment anomaly detection:** compares current payment-failure rates with a historical baseline
- **Enterprise investigation tools:** retrieves customer, transaction, service-health, deployment, and incident information
- **RAG foundation:** searches runbooks, postmortems, troubleshooting guides, and architecture documents
- **Agent workflow:** orchestrates a multi-step investigation and returns a tool trace
- **Evidence-backed diagnosis:** returns sources, findings, confidence, risk level, and a recommended next action
- **Human-in-the-loop controls:** requires approval before high-impact actions are executed
- **RBAC:** controls what admins, analysts, support engineers, and viewers can do
- **Audit logging:** records who requested an investigation, what was accessed, and whether the request was authorized
- **REST API:** exposes investigation capabilities through a Spring Boot service

---

## Architecture

```text
                     Web Dashboard / API Client
                                │
                                ▼
                    Spring Boot REST API Layer
                                │
                                ▼
                 Investigation Agent / Orchestrator
                                │
         ┌──────────────────────┼──────────────────────┐
         ▼                      ▼                      ▼
  Anomaly Detection         RAG Service            Tool Registry
         │                      │                      │
         ▼                      ▼                      ▼
 Payment Transactions     Runbooks / Docs       Services / Incidents /
                                                Deployments / Customers
                                │
                                ▼
                    Human Approval Workflow
                                │
                                ▼
                Jira / Slack / Incident Actions
                  (future implementation)
```

### Investigation flow

```text
1. User submits an incident question.
2. System validates the user role.
3. System writes an audit log.
4. Agent queries payment transactions.
5. Agent detects failure-rate anomalies.
6. Agent checks service health and deployment history.
7. Agent searches past incidents and runbooks.
8. Agent returns a diagnosis, evidence, and recommendation.
9. High-impact actions require human approval.
```

Example tool trace:

```text
get_customer()
query_transactions()
detect_anomaly()
get_service_health()
get_deployment_history()
search_incidents()
search_docs()
```

---

## Technology Stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot 3 |
| API | Spring Web / REST |
| Persistence | Spring Data JPA |
| Local database | H2 |
| Production database | PostgreSQL, planned |
| Validation | Jakarta Bean Validation |
| Monitoring | Spring Boot Actuator |
| Containerization | Docker |
| Security | Spring Security, OAuth2/OIDC, planned |
| RAG | pgvector, OpenSearch, or vector DB, planned |
| LLM provider | OpenAI, Anthropic, AWS Bedrock, or Azure OpenAI, planned |

Spring Boot applications use `SpringApplication.run(...)` to start the application, and Maven projects can be run locally with `mvn spring-boot:run`. [web:65]

---

## Project Structure

```text
incidentiq/
├── pom.xml
├── README.md
├── Dockerfile
├── docker-compose.yml
│
├── src/main/java/com/acme/intelligence/
│   ├── EnterpriseAiApplication.java
│   ├── config/
│   │   └── DataSeeder.java
│   ├── controller/
│   │   └── InvestigationController.java
│   ├── domain/
│   │   ├── Customer.java
│   │   ├── Payment.java
│   │   ├── ServiceStatus.java
│   │   ├── Deployment.java
│   │   ├── Incident.java
│   │   └── AuditLog.java
│   ├── dto/
│   ├── repository/
│   ├── service/
│   │   ├── InvestigationAgent.java
│   │   ├── ToolRegistry.java
│   │   ├── AnomalyDetectionService.java
│   │   ├── RagService.java
│   │   ├── SecurityService.java
│   │   └── AuditService.java
│   └── support/
│       └── Role.java
│
└── src/main/resources/
    └── application.yml
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Git
- Docker Desktop, optional

Verify your setup:

```bash
java --version
mvn --version
```

### Run locally

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/incidentiq.git
cd incidentiq
mvn spring-boot:run
```

The API will start at:

```text
http://localhost:8080
```

### Test health

```bash
curl http://localhost:8080/api/v1/health
```

Expected response:

```json
{
  "status": "ok"
}
```

If Spring Boot Actuator is enabled, check application health with:

```bash
curl http://localhost:8080/actuator/health
```

The Actuator health endpoint provides application health information at `/actuator/health`. [web:22]

---

## API Usage

### Investigate payment failures

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

Example response:

```json
{
  "diagnosis": "Payment failures are elevated above the expected baseline. The most likely driver is instability in Payment API, supported by anomaly metrics, service health, deployment history, incidents, and runbook guidance.",
  "evidence": [
    {
      "source": "ml.anomaly",
      "detail": "Current failure rate: 18.75%. Baseline: 7.5%."
    },
    {
      "source": "service.health",
      "detail": "Degraded services: Payment API."
    },
    {
      "source": "rag.runbook/payment-auth",
      "detail": "Inspect recent deployments, gateway health, regional spikes, and retry configuration changes."
    }
  ],
  "toolsUsed": [
    "get_customer",
    "query_transactions",
    "detect_anomaly",
    "get_service_health",
    "get_deployment_history",
    "search_incidents",
    "search_docs"
  ],
  "recommendation": {
    "summary": "Create a P1 incident, notify payment engineering, and review the most recent deployment.",
    "confidence": 0.93,
    "risk": "high",
    "approvalRequired": true
  }
}
```

---

## Roles and Safety

| Role | Investigate | Approve Actions | Jira / Slack Actions |
|---|---:|---:|---:|
| `ADMIN` | Yes | Yes | Yes |
| `ANALYST` | Yes | No | No |
| `SUPPORT_ENGINEER` | Yes | No | Yes |
| `VIEWER` | No | No | No |

The starter application accepts `userRole` in the request body only for local demonstration. In a production system, roles must come from verified JWT/OAuth2 or enterprise SSO claims.

High-impact actions should not run automatically:

```text
AI Diagnosis
      ↓
Evidence + Confidence
      ↓
Recommended Action
      ↓
Human Approval
      ↓
Jira / Slack / Incident API Execution
```

This design keeps the system useful while preserving governance, accountability, and enterprise safety.

---

## ML and RAG Design

### Anomaly detection

The initial detector compares payment failures in two seven-day windows:

```text
Current failure rate = failed payments / total payments
Deviation = current failure rate - historical baseline
```

| Deviation | Risk |
|---|---|
| Greater than 8 percentage points | High |
| Greater than 3 percentage points | Medium |
| 3 percentage points or below | Low |

The first implementation is intentionally simple and explainable. Future versions can use Isolation Forest, time-series forecasting, change-point detection, or supervised classification.

### RAG roadmap

```text
Documents
  ↓
Chunking + metadata
  ↓
Embeddings
  ↓
Vector search
  ↓
Hybrid retrieval + reranking
  ↓
Permission filtering
  ↓
LLM response with citations
```

RAG sources can include payment runbooks, incident postmortems, deployment notes, troubleshooting guides, support tickets, and architecture documentation.

---

## Roadmap

- **Data and ML:** migrate from H2 to PostgreSQL, add Flyway migrations, feature pipelines, and a trained anomaly model
- **RAG:** add document ingestion, embeddings, pgvector/OpenSearch, hybrid retrieval, reranking, citations, and permission filtering
- **Agentic AI:** add LLM tool calling, structured outputs, failure fallbacks, and approval-action endpoints
- **Security:** add Spring Security, OAuth2/OIDC, tenant isolation, secret management, prompt-injection tests, and data masking
- **Observability:** add OpenTelemetry, Prometheus metrics, Grafana dashboards, structured logs, and end-to-end traces
- **Deployment:** add Docker Compose, GitHub Actions CI/CD, cloud deployment, and external Jira/Slack integrations
- **Evaluation:** build a benchmark with 100–200 realistic incidents and measure diagnosis accuracy, retrieval quality, tool selection, latency, and cost

---

## Portfolio Talking Points

> Built an enterprise AI incident-intelligence platform using Java and Spring Boot that combines payment anomaly detection, RAG-based runbook retrieval, agentic tool orchestration, RBAC, audit logging, and human approval workflows.

> Designed the project as a Forward Deployed AI Engineer use case: start with an ambiguous customer problem, integrate enterprise data, create measurable outcomes, and deploy safe AI workflows.

> Separated read-only investigation tools from high-impact action tools so AI can recommend operational actions without bypassing human oversight.

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

Add your links before publishing:

```text
GitHub: https://github.com/YOUR_GITHUB_USERNAME
LinkedIn: https://linkedin.com/in/YOUR_LINKEDIN
Demo: YOUR_DEMO_URL
```
