# 📘 HLD Fundamentals — Load Balancer Notes

---

## 1. What is a Load Balancer

A load balancer distributes incoming client requests across multiple backend servers to ensure:

* High availability
* Scalability
* Reliability

---

## 2. Why Load Balancers are Needed

### Problems Solved:

* **Single server overload**
* **High latency under traffic spikes**
* **Single point of failure**

### Benefits:

* Horizontal scaling
* Fault tolerance
* Better resource utilization

---

## 3. Basic Architecture

Client → Load Balancer → Multiple Servers

---

## 4. Types of Load Balancers

### 4.1 Layer 4 Load Balancer (Transport Layer)

* Operates at TCP/UDP level
* Routes based on IP + Port
* Faster (no deep inspection)

**Example:** AWS Network Load Balancer

---

### 4.2 Layer 7 Load Balancer (Application Layer)

* Operates at HTTP/HTTPS level
* Routes based on:

  * URL path
  * Headers
  * Cookies
* Supports advanced routing

**Example:** AWS Application Load Balancer

---

## 5. Load Balancing Algorithms

### 5.1 Round Robin

* Requests distributed sequentially
* Simple and widely used

---

### 5.2 Weighted Round Robin

* Servers assigned weights
* Higher weight → more traffic

---

### 5.3 Least Connections

* Request sent to server with fewest active connections
* Good for uneven workloads

---

### 5.4 Least Response Time

* Chooses server with fastest response time

---

### 5.5 Hash-Based Routing

* Based on:

  * IP
  * URL
  * Header
* Ensures same user goes to same server

---

## 6. Stateless vs Stateful Load Balancing

### 6.1 Stateless Load Balancing

* Each request is independent
* No session information stored at load balancer

**Advantages:**

* Highly scalable
* Easy to distribute traffic

**Use case:**

* REST APIs

---

### 6.2 Stateful Load Balancing (Session Affinity / Sticky Sessions)

* Same client routed to same server
* Maintains session state

**Methods:**

* Cookies
* IP-based routing

**Advantages:**

* Required for session-based apps

**Disadvantages:**

* Poor scalability
* Uneven load distribution

---

## 7. SSL/TLS Termination

* Load balancer handles HTTPS decryption
* Backend servers receive HTTP

**Benefits:**

* Offloads CPU work
* Centralized certificate management

---

## 8. Health Checks

* Load balancer monitors server health
* Removes unhealthy instances from rotation

---

## 9. Horizontal Scaling

* Load balancer distributes traffic across multiple instances
* New servers can be added dynamically

---

## 10. Key Concepts

* Reverse Proxy
* Sticky Sessions
* Health Checks
* Failover
* Auto Scaling

---

## 11. Interview Summary

Load balancers distribute traffic across multiple servers to improve scalability and availability. They can operate at Layer 4 or Layer 7 and use algorithms like round robin or least connections. Stateless load balancing is preferred for scalability, while stateful load balancing (sticky sessions) is used when session persistence is required.

---
