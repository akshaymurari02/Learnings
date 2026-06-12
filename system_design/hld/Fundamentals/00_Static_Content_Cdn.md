# 📘 HLD Fundamentals — Static Content Delivery & CDN


## 1. What is Static Content

* Files that don’t change per user

  * HTML, CSS, JavaScript
  * Images, videos
* No backend computation required

---

## 2. Basic Architecture

### Components:

* Origin storage (e.g., S3)
* CDN layer (e.g., CloudFront)
* Client (Browser)

---

## 3. Request Flow

1. User requests a file (e.g., `/logo.png`)
2. DNS routes request to CDN
3. CDN checks cache:

   * **Cache HIT** → return immediately
   * **Cache MISS** → fetch from origin → cache → return

---

## 4. Why CDN is Needed

### Problem 1: Latency

* Caused by distance between user and origin server
* High round-trip time

**Solution:**

* Cache content at edge locations near users

---

### Problem 2: Bandwidth Limitation

* Each server has finite bandwidth
* Parallel requests share bandwidth → slower responses

**Solution:**

* Distribute traffic across multiple edge servers

---

### Problem 3: Traffic Spikes (Thundering Herd)

* Many users request the same file simultaneously

**Solution:**

* CDN caching
* Request coalescing (single origin fetch)

---

## 5. Role of Each Component

### CDN

* Edge caching
* Routes users to nearest location
* Handles scaling
* Reduces origin load
* Provides DDoS protection

### Origin (S3)

* Stores static files
* Highly durable storage
* Serves content only on cache miss

**Key Point:**

* Origin does NOT handle user traffic at scale
* CDN absorbs most of the traffic

---

## 6. Multiple Users Requesting Same Content

### Case 1: Cache HIT

* All users served directly from CDN
* No request to origin

### Case 2: Cache MISS

* First request → goes to origin
* Other requests → wait briefly
* Response shared with all users

**Concept:** Request Coalescing

---

## 7. Why Parallel Requests Alone Are Not Enough

### Limitations:

* Bandwidth is shared
* CPU and disk are limited
* Increased connections → contention

**Impact:**

* Higher latency
* Possible failures

---

## 8. Distributed System Advantage

CDN provides:

* Geographic distribution
* Horizontal scaling
* Load distribution
* Reduced latency

---

## 9. Mental Models

* Origin = Warehouse (stores files)
* CDN = Delivery network (brings data closer to users)

---

## 10. Key Concepts

* Cache Hit / Cache Miss
* Edge Locations
* Request Coalescing
* Thundering Herd Problem
* Horizontal Scaling
* Latency vs Bandwidth

---

## 11. Final Summary

Static content delivery at scale requires solving latency and bandwidth problems.

* Latency is reduced using CDN edge caching
* Bandwidth limitations are solved via distributed systems
* Parallel connections alone do not scale due to shared resource constraints

**Conclusion:**
Distributed systems like CDNs are essential for handling large-scale traffic efficiently.

---
