# 📘 HLD Fundamentals — API Gateway Notes

---

## 1. What is an API Gateway

An API Gateway is a **single entry point** for clients to access backend services (typically microservices) and handles cross-cutting concerns such as authentication, routing, and rate limiting.

---

## 2. Why API Gateway is Needed

### Problems Without Gateway:

* Clients must call multiple services directly
* Duplicate authentication logic in services
* Increased coupling between client and services
* Difficult to enforce security policies

### Benefits:

* Centralized authentication & security
* Simplified client interaction
* Reduced duplication
* Better observability

---

## 3. Core Responsibilities

### 3.1 Routing

* Routes requests to appropriate service

Example:

```
/api/users   → User Service
/api/orders  → Order Service
```

---

### 3.2 Authentication

* Validates user identity
* Supports:

  * JWT
  * OAuth
  * API Keys

---

### 3.3 Authorization (Partial)

* Can enforce basic access policies
* Fine-grained authorization handled by services

---

### 3.4 Rate Limiting

* Prevents abuse
* Protects backend services

---

### 3.5 Request Aggregation

* Combines multiple service responses into one

---

### 3.6 Logging & Monitoring

* Tracks API usage
* Metrics and tracing

---

### 3.7 Protocol Translation

* HTTP ↔ gRPC
* External ↔ internal formats

---

## 4. Architecture

```
Client → API Gateway → Microservices
```

---

## 5. API Gateway vs Load Balancer

| Feature                | API Gateway      | Load Balancer |
| ---------------------- | ---------------- | ------------- |
| Routing                | Yes (path-based) | Yes (basic)   |
| Authentication         | Yes              | No            |
| Rate limiting          | Yes              | Limited       |
| Request transformation | Yes              | No            |
| Load distribution      | No               | Yes           |

---

## 6. API Gateway vs Reverse Proxy

* Reverse proxy (e.g., NGINX) focuses on routing
* API Gateway adds API-specific features (auth, rate limiting, monitoring)

---

## 7. Types of API Gateway

### 7.1 Managed API Gateway

* Fully managed by cloud provider

Example:

* AWS API Gateway

---

### 7.2 Self-hosted API Gateway

* Deployed and managed by users

Examples:

* Kong
* NGINX (configured as gateway)

---

## 8. Authentication in API Gateway

### Methods:

#### 1. JWT Authorizer

* Validates token signature and expiry

#### 2. Lambda Authorizer

* Custom authentication logic

#### 3. API Keys

* Simple access control

#### 4. IAM Authentication

* Used for internal AWS services

---

## 9. JWT Authentication Flow

```
Client → API Gateway → (validate JWT) → Service
```

* Gateway verifies token
* Forwards request if valid

---

## 10. Migration to API Gateway Auth

### Before:

```
Client → Service → JWT validation
```

### After:

```
Client → API Gateway → Service
```

### Steps:

1. Configure JWT authorizer
2. Attach to routes
3. Remove validation from services
4. Services trust gateway

---

## 11. Request Flow

1. Client sends request
2. API Gateway authenticates
3. Applies rate limiting
4. Routes request
5. Forwards to service
6. Service responds

---

## 12. Scalability

* API Gateway is horizontally scalable
* Managed gateways auto-scale
* Self-hosted requires load balancer

---

## 13. Deployment Model

### Managed (AWS API Gateway):

* No servers
* Configuration-based

### Self-hosted:

* Requires deployment
* Needs scaling & maintenance

---

## 14. Key Concepts

* Single Entry Point
* Cross-cutting Concerns
* Stateless Design
* Centralized Security

---

## 15. Limitations

* Adds latency
* Can become bottleneck if not scaled
* Complex configuration in large systems

---

## 16. Interview Summary

API Gateway acts as a single entry point for microservices and handles cross-cutting concerns such as authentication, routing, and rate limiting. It simplifies client interaction and centralizes security, but does not replace load balancing or service-level authorization.

---
