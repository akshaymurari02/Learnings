# 🌍 Content Delivery Network (CDN)

## 📌 What is a CDN?
A **Content Delivery Network (CDN)** is a distributed system of servers that deliver content to users from the **nearest geographical location**, reducing latency and improving performance.

---

# 🧠 Core Components

## 🏢 POP (Point of Presence)
- Physical data center location
- Distributed globally
- Contains multiple edge servers
- Closest point to the end user

---

## ⚡ Edge Server
- Located inside POP
- Stores cached content
- Serves user requests directly
- First point of contact for user request

---

## 🏠 Origin Server
- Source of truth (backend / storage)
- Examples:
  - AWS S3
  - Application servers
- Used when cache miss occurs

---

# 🔁 CDN Request Flow

1. User requests content (e.g., image/video/API)
2. DNS routes request to nearest POP
3. Edge server checks:
   - ✅ Cache Hit → Serve directly
   - ❌ Cache Miss → Fetch from origin
4. Cache content at edge
5. Return response to user

---

# 🔥 Key Concepts

## 📦 Cache Hit vs Cache Miss

- **Cache Hit**
  - Content served from edge
  - Low latency

- **Cache Miss**
  - Request goes to origin
  - Higher latency

---

## 🔥 Cache Warming
- Preloading content into CDN before users request it
- Avoids initial cache misses
- Useful for:
  - Product launches
  - Viral content

---

## ⏳ TTL (Time To Live)
- Defines how long content stays in cache
- Example:



---

# 🚀 Benefits of CDN

## ⚡ 1. Reduced Latency
- Content served from nearest location

---

## 🌐 2. Reduced Load on Origin
- Edge servers handle majority of traffic

---

## 📈 3. High Scalability
- Handles millions of concurrent users

---

## 🛡️ 4. High Availability
- Multiple POPs ensure fault tolerance

---

## 🔒 5. Security
- DDoS protection
- WAF (Web Application Firewall)
- Rate limiting

---

## 💰 6. Cost Optimization
- Reduced bandwidth usage from origin

---

# 🔄 Push CDN vs Pull CDN

## 📥 Pull CDN (Lazy Loading)
- CDN fetches content **only when requested**
- Origin pulls content on cache miss

### ✅ Pros:
- Easy setup
- No manual uploads

### ❌ Cons:
- First request is slow (cache miss)

---

## 📤 Push CDN (Preload)
- Content is **uploaded to CDN in advance**

### ✅ Pros:
- Faster delivery (no cache miss)
- Full control over content

### ❌ Cons:
- Requires manual management
- Storage overhead

---

# 🏗️ CDN Architecture

## 🌍 High-Level Architecture


---

## 🔧 Components

### 1. DNS Routing
- Routes user to nearest POP
- Uses:
  - Geo-routing
  - Anycast IP

---

### 2. Load Balancer
- Distributes traffic across edge servers

---

### 3. Edge Cache Layer
- Stores static content
- Handles majority of requests

---

### 4. Origin Layer
- Central storage
- Used for cache misses

---

### 5. Cache Control Layer
- TTL management
- Cache invalidation
- Cache policies

---

# 🧠 Advanced Concepts (Interview Level)

## 🌐 Geo Routing
- Routes user based on location

---

## 🔁 Anycast Routing
- Same IP across multiple locations
- Routes to nearest server automatically

---

## 📊 Static vs Dynamic Content

| Type | CDN Usage |
|------|----------|
| Static | Cached easily |
| Dynamic | Needs smart caching |

---

## ⚖️ Trade-off


- Longer TTL → better performance
- Shorter TTL → fresher data

---

# 🔥 Real CDN Providers

- https://aws.amazon.com/cloudfront/
- https://www.cloudflare.com/
- https://www.akamai.com/

---

# 🎯 Interview One-Liner

> A CDN is a globally distributed system that caches content at edge servers in POPs to reduce latency, improve scalability, and offload traffic from origin servers.

---

Push vs Pull CDN 

| Feature         | Push CDN   | Pull CDN          |
| --------------- | ---------- | ----------------- |
| Content loading | Pre-upload | On-demand         |
| First request   | Fast       | Slow (cache miss) |
| Setup           | Manual     | Automatic         |
| Control         | High       | Medium            |
