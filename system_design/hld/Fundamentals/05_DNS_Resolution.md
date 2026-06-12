# 📘 HLD Fundamentals — DNS, Resolution Process, Load Balancing & High Availability

---

## 1. What is DNS

**DNS (Domain Name System)** translates human-readable domain names (e.g., `example.com`) into IP addresses (e.g., `93.184.216.34`).

* Acts like a distributed, hierarchical phonebook
* Runs over UDP (port 53) for most queries; TCP for large responses/zone transfers

---

## 2. DNS Hierarchy

```
Root (.)
 └── TLD (.com, .org, .in)
      └── Authoritative Name Servers (example.com)
           └── Records (A, AAAA, CNAME, MX, etc.)
```

### Key Components

* **Root servers**: Know TLD servers
* **TLD servers**: Know authoritative servers for a domain
* **Authoritative servers**: Hold actual DNS records for the domain
* **Recursive resolver**: Does lookups on behalf of the client (ISP or public resolver)

---

## 3. Common DNS Record Types

* **A** → IPv4 address
* **AAAA** → IPv6 address
* **CNAME** → Alias to another domain
* **MX** → Mail servers
* **NS** → Authoritative name servers
* **TXT** → Arbitrary text (SPF, verification)
* **SRV** → Service location (host/port)

---

## 4. DNS Resolution Process (Step-by-step)

### Scenario: User requests `www.example.com`

1. Browser checks **local cache**
2. OS checks **OS cache / hosts file**
3. Request goes to **recursive resolver** (ISP / public)
4. Resolver checks its cache
5. If not found:

   * Resolver → **Root server** → gets TLD info
   * Resolver → **TLD server (.com)** → gets authoritative NS
   * Resolver → **Authoritative server** → gets IP
6. Resolver returns IP to client
7. Client caches result (based on TTL)
8. Browser makes HTTP/HTTPS request to that IP

---

## 5. Caching & TTL

* **TTL (Time To Live)** defines how long a record is cached
* Caching layers:

  * Browser
  * OS
  * Recursive resolver

### Trade-offs

* Low TTL → faster updates, more DNS load
* High TTL → better performance, slower propagation

---

## 6. DNS Load Balancing

### 6.1 Round Robin DNS

```
example.com → 1.1.1.1
example.com → 2.2.2.2
example.com → 3.3.3.3
```

* Resolver returns different IPs in rotation
* Simple but no health awareness

---

### 6.2 Weighted Routing

* Assign weights to IPs
* More traffic to higher-weight servers

---

### 6.3 Geo-based Routing

* Route users to nearest region
* Improves latency

---

### 6.4 Latency-based Routing

* Chooses endpoint with lowest latency (measured by provider)

---

### 6.5 Health-check-based Routing

* DNS provider removes unhealthy endpoints from answers
* Requires active health checks

---

## 7. Limitations of DNS Load Balancing

* **Caching**: Clients may keep stale IPs until TTL expires
* **No connection awareness**: DNS decides before connection is made
* **Slow failover**: TTL delay

👉 Therefore, DNS LB is often combined with **application/load balancers**

---

## 8. High Availability with DNS

### Techniques

#### 8.1 Multiple Regions

* Deploy services in multiple regions
* DNS routes to healthy region

#### 8.2 Failover Routing

* Primary + secondary endpoints
* Switch on failure

#### 8.3 Anycast DNS

* Same IP advertised from multiple locations
* Requests routed to nearest node

#### 8.4 Redundant Name Servers

* Multiple NS records for a domain
* If one fails, others respond

---

## 9. Anycast (Important)

* Same IP address announced from multiple geographic locations
* Routers send user to nearest node

### Used in:

* Public DNS resolvers
* CDN edge networks

---

## 10. End-to-End Flow (Putting it together)

```
User → Browser cache → OS cache → Recursive resolver
     → Root → TLD → Authoritative
     → IP returned → Client connects to server/CDN
```

---

## 11. Best Practices

* Use CDN + DNS for global distribution
* Set appropriate TTL (not too high, not too low)
* Use health checks for failover
* Use multiple regions for HA

---

## 12. Interview Summary

DNS is a hierarchical, distributed system that resolves domain names to IP addresses. It uses caching and TTL to improve performance. DNS can provide basic load balancing (round robin, geo, latency-based), but has limitations due to caching. High availability is achieved using multi-region setups, health checks, Anycast routing, and redundant name servers.

---
