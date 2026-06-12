# 📘 HLD Fundamentals — Caching (Deep Dive)

---

## 1. What is Caching

Caching is the process of storing frequently accessed data in a faster storage layer to reduce latency and load on the primary data source.

---

## 2. Why Caching is Needed

### Problems Without Cache

* High latency (DB/network calls)
* High load on backend systems
* Poor scalability

### Benefits

* Reduced latency
* Reduced backend load
* Improved throughput

---

## 3. Types of Caching (by layer)

### 3.1 Client-side Cache

* Browser cache
* Stores static assets (JS, CSS, images)

---

### 3.2 CDN Cache

* Edge caching (CloudFront, etc.)
* Reduces geographic latency

---

### 3.3 Application Cache

* In-memory cache (Redis, Memcached)
* Used for API responses, computed data

---

### 3.4 Database Cache

* Query cache
* Buffer pool (e.g., MySQL InnoDB)

---

## 4. Cache Placement Strategies

### 4.1 Cache-Aside (Lazy Loading)

Flow:

1. Check cache
2. If miss → fetch from DB
3. Store in cache

Pros:

* Simple
* Efficient

Cons:

* Cache miss latency

---

### 4.2 Write-Through Cache

Flow:

1. Write to cache
2. Write to DB

Pros:

* Always consistent

Cons:

* Higher write latency

---

### 4.3 Write-Back (Write-Behind)

Flow:

1. Write to cache
2. Async write to DB

Pros:

* Fast writes

Cons:

* Risk of data loss

---

### 4.4 Read-Through Cache

* Cache fetches data automatically
* Application does not interact with DB directly

---

## 5. Cache Terminology

### Cache Hit

* Data found in cache

### Cache Miss

* Data not found → fetch from source

### Hit Ratio

* Hits / Total requests

### Eviction

* Removing data when cache is full

### TTL (Time To Live)

* Time after which cache expires

---

## 6. Eviction Policies

### LRU (Least Recently Used)

* Removes least recently accessed item

### LFU (Least Frequently Used)

* Removes least frequently accessed item

### FIFO (First In First Out)

* Removes oldest item

---

## 7. Cache Consistency

### Problem

* Cached data can become stale

### Solutions

* TTL expiration
* Write-through strategy
* Explicit invalidation

---

## 8. Cache Invalidation

### Hard Problem

"There are only two hard things: cache invalidation and naming things"

### Techniques

#### 1. TTL-based

* Data expires automatically

#### 2. Event-based

* Invalidate on update

#### 3. Versioning

```
/api/data?v=2
```

---

## 9. Cache Patterns

### 9.1 Hot Data Caching

* Cache frequently accessed data

### 9.2 Partial Caching

* Cache only expensive operations

### 9.3 Full Page Caching

* Cache entire response

---

## 10. Distributed Caching Challenges

* Cache coherence
* Data consistency
* Network latency
* Cache synchronization

---

## 11. Common Problems

### 11.1 Cache Stampede

* Many requests hit DB on cache miss

Solution:

* Request coalescing
* Locks

---

### 11.2 Cache Penetration

* Requests for non-existent data

Solution:

* Cache null values

---

### 11.3 Cache Avalanche

* Many keys expire at same time

Solution:

* Randomized TTL

---

## 12. Best Practices

* Cache only frequently accessed data
* Use appropriate TTL
* Monitor hit ratio
* Use distributed cache for scale

---

## 13. When NOT to Cache

* Highly dynamic data
* Strong consistency requirements
* Sensitive data (without proper controls)

---

## 14. Interview Summary

Caching improves system performance by reducing latency and backend load. It can be implemented at multiple layers and uses strategies like cache-aside or write-through. Key challenges include consistency and invalidation, and common problems include cache stampede and stale data.

---
