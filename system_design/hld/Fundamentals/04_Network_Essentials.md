# 📘 HLD Fundamentals — Network Essentials (Deep Dive)

---

## 1. HTTP vs HTTPS

### HTTP (HyperText Transfer Protocol)

* Application layer protocol used for communication between client and server
* Data is sent in **plain text**

### HTTPS (HTTP Secure)

* HTTP over TLS/SSL encryption
* Provides **secure communication**

### Key Differences

| Feature        | HTTP            | HTTPS                |
| -------------- | --------------- | -------------------- |
| Security       | ❌ No encryption | ✅ Encrypted (TLS)    |
| Port           | 80              | 443                  |
| Data Integrity | ❌ No            | ✅ Yes                |
| Authentication | ❌ No            | ✅ Yes (certificates) |

### HTTPS Flow (Simplified)

1. Client initiates TLS handshake
2. Server sends certificate
3. Client verifies certificate
4. Session keys established
5. Encrypted communication begins

---

## 2. TCP vs UDP

### TCP (Transmission Control Protocol)

* Connection-oriented protocol
* Reliable data transfer

#### Features

* Guaranteed delivery
* Ordered packets
* Retransmission of lost packets
* Congestion control

#### Use Cases

* HTTP/HTTPS
* Database communication
* File transfer

---

### UDP (User Datagram Protocol)

* Connectionless protocol
* Faster but unreliable

#### Features

* No guarantee of delivery
* No ordering
* No retransmission
* Low latency

#### Use Cases

* Video streaming
* Online gaming
* DNS queries

---

### TCP vs UDP Comparison

| Feature     | TCP        | UDP            |
| ----------- | ---------- | -------------- |
| Connection  | Yes        | No             |
| Reliability | High       | Low            |
| Speed       | Slower     | Faster         |
| Ordering    | Guaranteed | Not guaranteed |

---

## 3. HTTP/1 vs HTTP/2 vs HTTP/3

### HTTP/1.1

* Text-based protocol
* One request per connection (limited parallelism)

#### Problems

* Head-of-line blocking
* Multiple connections needed

---

### HTTP/2

* Binary protocol
* Multiplexing (multiple requests on one connection)

#### Features

* Header compression
* Stream prioritization
* Reduced latency

---

### HTTP/3

* Built on UDP (uses QUIC protocol)

#### Features

* Eliminates TCP head-of-line blocking
* Faster connection setup
* Better performance in lossy networks

---

### Comparison

| Feature               | HTTP/1.1 | HTTP/2  | HTTP/3     |
| --------------------- | -------- | ------- | ---------- |
| Protocol              | TCP      | TCP     | UDP (QUIC) |
| Multiplexing          | ❌        | ✅       | ✅          |
| Head-of-line blocking | Yes      | Partial | No         |
| Performance           | Slow     | Faster  | Fastest    |

---

## 4. URI vs URL vs URN

### URI (Uniform Resource Identifier)

* General identifier for a resource

### URL (Uniform Resource Locator)

* Specifies **location** of resource

Example:
[https://example.com/users](https://example.com/users)

---

### URN (Uniform Resource Name)

* Specifies **name** of resource (not location)

Example:
urn:isbn:0451450523

---

### Relationship

All URLs and URNs are URIs, but not all URIs are URLs.

```
URI
 ├── URL
 └── URN
```

---

## 5. Key Takeaways

* HTTPS = HTTP + encryption (TLS)
* TCP = reliable, UDP = fast
* HTTP/2 and HTTP/3 improve performance significantly
* URI is a superset, URL and URN are types of URI

---

## 6. Interview Summary

Understanding network fundamentals like HTTP vs HTTPS, TCP vs UDP, and modern protocol improvements (HTTP/2, HTTP/3) is essential for designing scalable systems. These protocols directly impact latency, reliability, and performance.

---
