# 📘 HLD Fundamentals — Databases (Deep Dive)

---

# 1. Introduction

Databases are systems used to store, retrieve, manage, and process data.

Distributed system design heavily depends on:

* Data model
* Scalability requirements
* Consistency requirements
* Query patterns

---

# 2. SQL Databases

## Definition


---Relational databases that store data in tables with predefined schemas.


## Characteristics

* Structured schema
* ACID compliance
* Joins and relationships
* Strong consistency

---

## Examples

* MySQL
* PostgreSQL
* Oracle
* SQL Server

---

## Example Table

| user_id | name   | email                           |
| ------- | ------ | ------------------------------- |
| 1       | Akshay | [a@test.com](mailto:a@test.com) |

---

# 3. NoSQL Databases

## Definition

Non-relational databases designed for scalability, flexibility, and distributed workloads.

---

## Characteristics

* Flexible schema
* Horizontal scaling
* High availability
* Eventual consistency common

---

## Types of NoSQL Databases

### 3.1 Key-Value

* Redis
* DynamoDB

---

### 3.2 Document Databases

* MongoDB
* CouchDB

---

### 3.3 Column-family

* Cassandra
* HBase

---

### 3.4 Graph Databases

* Neo4j

---

# 4. SQL vs NoSQL

| Feature      | SQL             | NoSQL                        |
| ------------ | --------------- | ---------------------------- |
| Schema       | Fixed           | Flexible                     |
| Scaling      | Vertical        | Horizontal                   |
| Consistency  | Strong          | Eventual common              |
| Transactions | Strong ACID     | Limited/varies               |
| Joins        | Strong support  | Limited                      |
| Use Cases    | Structured data | Large-scale distributed data |

---

# 5. When to Use SQL

Use SQL when:

* Strong consistency required
* Complex joins needed
* Transactions critical
* Structured data

Examples:

* Banking
* Payment systems
* ERP systems

---

# 6. When to Use NoSQL

Use NoSQL when:

* Massive scalability needed
* Flexible schema required
* High write throughput
* Distributed systems

Examples:

* Social media feeds
* Real-time analytics
* IoT systems

---

# 7. ACID Properties

Used mainly in relational databases.

---

# 7.1 Atomicity

All operations succeed or none succeed.

Example:

```
Debit + Credit must both complete
```

---

# 7.2 Consistency

Database moves from one valid state to another.

Constraints remain valid.

---

# 7.3 Isolation

Concurrent transactions do not interfere.

---

# 7.4 Durability

Committed data survives crashes.

---

# 8. BASE Properties

Common in distributed NoSQL systems.

---

# 8.1 Basically Available

System remains available.

---

# 8.2 Soft State

Data may change over time due to replication.

---

# 8.3 Eventual Consistency

Replicas eventually become consistent.

---

# 9. ACID vs BASE

| Feature      | ACID                  | BASE     |
| ------------ | --------------------- | -------- |
| Consistency  | Strong                | Eventual |
| Availability | Lower during failures | High     |
| Transactions | Strong                | Relaxed  |
| Scaling      | Harder                | Easier   |

---

# 10. Real-world Examples

---

## SQL Example

### Banking System

Requirements:

* Strong consistency
* Transactions
* No data loss

Use:
👉 PostgreSQL / MySQL

---

## NoSQL Example

### Social Media Feed

Requirements:

* Massive scale
* Fast reads/writes
* Eventual consistency acceptable

Use:
👉 Cassandra / DynamoDB

---

# 11. Normalization

## Definition

Process of organizing data to reduce redundancy.

---

## Goals

* Reduce duplication
* Improve consistency
* Save storage

---

## Example

Instead of:

| Order | User Name |
| ----- | --------- |
| 1     | Akshay    |
| 2     | Akshay    |

Separate tables:

Users table
Orders table

---

## Advantages

* Better consistency
* Less redundancy

---

## Disadvantages

* More joins
* Slower reads

---

# 12. Denormalization

## Definition

Intentionally duplicating data to improve read performance.

---

## Example

Store:

```
Order + User Name together
```

Avoids joins.

---

## Advantages

* Faster reads
* Better performance

---

## Disadvantages

* Data duplication
* Update complexity

---

# 13. Normalization vs Denormalization

| Feature          | Normalization | Denormalization |
| ---------------- | ------------- | --------------- |
| Redundancy       | Low           | High            |
| Read Performance | Slower        | Faster          |
| Write Complexity | Lower         | Higher          |
| Consistency      | Easier        | Harder          |

---

# 14. In-memory Databases

## Definition

Store data primarily in RAM.

---

## Characteristics

* Extremely fast
* Low latency
* Limited by memory size

---

## Examples

* Redis
* Memcached

---

## Use Cases

* Caching
* Session storage
* Real-time analytics

---

# 15. Disk-based Databases

## Definition

Store data primarily on disk/SSD.

---

## Characteristics

* Durable storage
* Larger capacity
* Slower than RAM

---

## Examples

* MySQL
* PostgreSQL
* MongoDB

---

# 16. In-memory vs Disk Databases

| Feature    | In-memory        | Disk-based |
| ---------- | ---------------- | ---------- |
| Speed      | Very Fast        | Slower     |
| Durability | Limited/optional | Strong     |
| Capacity   | Limited          | Large      |
| Cost       | Higher           | Lower      |

---

# 17. Data Replication

## Definition

Copying data across multiple nodes.

---

## Goals

* High availability
* Fault tolerance
* Read scalability

---

## Example

```
Primary DB
  ├── Replica 1
  └── Replica 2
```

---

# 18. Data Mirroring

## Definition

Maintaining an exact synchronized copy of data.

Often synchronous and real-time.

---

## Example

Primary disk ↔ mirrored disk

---

# 19. Replication vs Mirroring

| Feature      | Replication          | Mirroring       |
| ------------ | -------------------- | --------------- |
| Sync         | Sync or async        | Usually sync    |
| Purpose      | Availability/scaling | Exact backup    |
| Performance  | Flexible             | Higher overhead |
| Read Scaling | Yes                  | Limited         |

---

# 20. Database Federation

## Definition

Splitting databases by function/business domain.

---

## Example

```
User DB
Order DB
Inventory DB
```

---

## Goals

* Reduce coupling
* Independent scaling
* Better ownership

---

## Advantages

* Scalability
* Isolation
* Independent optimization

---

## Challenges

* Cross-database joins difficult
* Distributed transactions
* Data duplication

---

# 21. Federation vs Sharding

| Feature     | Federation          | Sharding                 |
| ----------- | ------------------- | ------------------------ |
| Split Basis | Business domain     | Same dataset partition   |
| Example     | User DB vs Order DB | User 1-1000 vs 1001-2000 |

---

# 22. Important Trade-offs

| Goal               | Trade-off            |
| ------------------ | -------------------- |
| Strong consistency | Higher latency       |
| Scalability        | Complexity           |
| Faster reads       | Data duplication     |
| Availability       | Eventual consistency |

---

# 23. Mental Models

* SQL = structure + consistency
* NoSQL = scalability + flexibility
* Normalization = reduce duplication and improve data intigrity
* Denormalization = optimize reads
* Replication = copies for availability
* Federation = split by domain

---

# 24. Interview Summary

Database design in distributed systems involves balancing consistency, scalability, availability, and performance. SQL databases prioritize strong consistency and structured schemas, while NoSQL systems prioritize scalability and flexibility. Concepts such as normalization, replication, denormalization, and federation help optimize systems for different workloads and access patterns.

---
