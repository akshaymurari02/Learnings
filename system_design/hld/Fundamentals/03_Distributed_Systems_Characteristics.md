# 📘 HLD Fundamentals — Distributed Systems Characteristics (Deep Dive)

---

## 1. Scalability

### Definition

Ability of a system to handle increasing load by adding resources.

### Types

#### Vertical Scaling (Scale Up)

* Increase CPU, RAM of a single machine
* Limited by hardware constraints

#### Horizontal Scaling (Scale Out)

* Add more machines (nodes)
* Preferred in distributed systems

### Techniques

* Load balancing
* Stateless services
* Database sharding
* Auto-scaling groups

### Challenges

* Data consistency
* Coordination between nodes

---

## 2. Latency

### Definition

Time taken for a request to travel from client → server → response.

### Sources of Latency

* Network delay (geographical distance)
* Serialization/deserialization
* Multiple service hops
* Disk I/O

### Techniques to Reduce Latency

* CDN (edge caching)
* In-memory caching (Redis)
* Reduce service-to-service calls
* Data locality (deploy services close together)

### Trade-off

Lower latency may increase complexity or reduce consistency.

---

## 3. Availability

### Definition

System remains operational and accessible over time.

### Measurement

* Expressed as percentage (e.g., 99.99%)

### Techniques

* Redundancy (multiple instances)
* Load balancing
* Failover systems
* Multi-region deployment

### Example

If one server fails, another instance serves traffic.

---

## 4. Fault Tolerance

### Definition

System continues functioning even when components fail.

### Techniques

* Replication (data + services)
* Retry mechanisms
* Circuit breakers
* Graceful degradation

### Example

If payment service fails, allow order creation but mark payment as pending.

---

## 5. Error Handling / Resilience

### Definition

Ability to handle failures gracefully without crashing the system.

### Techniques

#### Retries

* Retry failed requests
* Use exponential backoff

#### Timeouts

* Avoid waiting indefinitely

#### Circuit Breaker

* Stop calling failing services

#### Idempotency

* Safe to retry same request multiple times

### Goal

Prevent cascading failures and improve system stability.

---

## 6. Monitoring & Observability

### Definition

Ability to understand system behavior in production.

### Pillars

#### Metrics

* CPU, memory, request latency, error rates

#### Logs

* Detailed event records

#### Traces

* Track request flow across services

### Benefits

* Debugging issues
* Performance optimization
* Alerting on failures

---

## 7. Consistency

### Definition

Ensures all nodes see the same data.

### Types

#### Strong Consistency

* All reads return latest data
* Higher latency

#### Eventual Consistency

* Data becomes consistent over time
* Better availability and performance

### Trade-off

Consistency vs availability vs latency

---

## 8. Partition Tolerance

### Definition

System continues operating despite network failures between nodes.

### Reality

* Network failures are inevitable
* Distributed systems must tolerate partitions

---

## 9. CAP Theorem

### Definition

A distributed system can only guarantee two of the following three:

* Consistency (C)
* Availability (A)
* Partition Tolerance (P)

### Scenarios

#### CP System

* Consistency + Partition tolerance
* May sacrifice availability

#### AP System

* Availability + Partition tolerance
* May return stale data

#### CA System

* Not practical in distributed systems (no partition tolerance)

---

## 10. Trade-offs Summary

| Characteristic  | Trade-off   |
| --------------- | ----------- |
| Scalability     | Complexity  |
| Latency         | Consistency |
| Availability    | Consistency |
| Fault Tolerance | Cost        |
| Consistency     | Performance |

---

## 11. Key Mental Models

* Distributed systems are about **trade-offs**
* Failures are **expected, not exceptions**
* Design for **horizontal scaling**
* Move computation **closer to users**

---

## 12. Interview Summary

Distributed systems are designed to handle scale, failures, and performance challenges. Key characteristics include scalability, latency, availability, fault tolerance, and observability. Additionally, concepts like consistency and partition tolerance introduce trade-offs, commonly explained using the CAP theorem.

---
