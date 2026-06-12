# 📘 HLD Fundamentals — Security & Privacy (Deep Dive)

---

# 1. Introduction

**Security** in distributed systems is about protecting data, services, and infrastructure from unauthorized access, tampering, and attacks.

**Privacy** is about ensuring that personal/sensitive data is collected, stored, and processed in a way that respects user consent and regulatory requirements.

> In system design, security is not an afterthought — it's a foundational concern that affects every layer from network transport to data storage to API design.

---

## Core Pillars (CIA Triad + Privacy)

| Pillar | Description |
|--------|-------------|
| **Confidentiality** | Only authorized parties can access data |
| **Integrity** | Data is not tampered with in transit or at rest |
| **Availability** | System remains accessible despite attacks |
| **Privacy** | User data is handled per consent and regulations |

---

# 2. Authentication

---

## 2.1 What is Authentication?

**Authentication (AuthN)** = Verifying **who** the user/service is.

> "Are you who you claim to be?"

---

## 2.2 Authentication Factors

| Factor | Type | Example |
|--------|------|---------|
| Something you **know** | Knowledge | Password, PIN, security question |
| Something you **have** | Possession | Phone (OTP), hardware key, smart card |
| Something you **are** | Inherence | Fingerprint, face ID, retina scan |

**Multi-Factor Authentication (MFA)** combines 2+ factors for stronger security.

---

## 2.3 Common Authentication Methods

| Method | Description | Use Case |
|--------|-------------|----------|
| Username + Password | Traditional credential-based | Web login |
| API Keys | Static secret token in headers | Server-to-server |
| OAuth 2.0 Tokens | Delegated access via access tokens | Third-party apps |
| JWT (JSON Web Token) | Self-contained signed token | Stateless auth |
| SAML | XML-based SSO protocol | Enterprise SSO |
| mTLS (Mutual TLS) | Both client and server present certificates | Service-to-service |
| FIDO2/WebAuthn | Passwordless hardware key authentication | Modern web apps |

---

## 2.4 Session-Based vs Token-Based Authentication

```
SESSION-BASED:
┌────────┐  credentials  ┌────────────┐  create session  ┌─────────┐
│ Client │──────────────►│   Server   │─────────────────►│  Store  │
└────────┘               └────────────┘                  │(Redis/DB)│
     ▲                         │                          └─────────┘
     │      Set-Cookie: sid=abc123
     └─────────────────────────┘
     
Subsequent requests: Cookie: sid=abc123 → Server looks up session in store

TOKEN-BASED:
┌────────┐  credentials  ┌────────────┐
│ Client │──────────────►│   Server   │  (generates JWT, no server state)
└────────┘               └────────────┘
     ▲                         │
     │      Token: eyJhbG...
     └─────────────────────────┘

Subsequent requests: Authorization: Bearer <token> → Server verifies signature
```

| Feature | Session-Based | Token-Based (JWT) |
|---------|--------------|-------------------|
| State | Stateful (server stores sessions) | Stateless (token is self-contained) |
| Scalability | Needs shared session store | Scales horizontally easily |
| Revocation | Easy (delete session) | Hard (token valid until expiry) |
| Storage | Server-side (Redis, DB) | Client-side (localStorage, cookie) |
| Cross-domain | Difficult (cookies are domain-bound) | Easy (token in header) |

---

# 3. Authorization

---

## 3.1 What is Authorization?

**Authorization (AuthZ)** = Determining **what** an authenticated user is allowed to do.

> "You are who you say you are — but are you allowed to do this?"

---

## 3.2 Authorization Models

---

### RBAC (Role-Based Access Control)

```
User → Role → Permissions

Example:
  User "Alice" → Role "Editor" → Permissions: [read, write, publish]
  User "Bob"   → Role "Viewer" → Permissions: [read]
```

* Most common model
* Easy to manage for moderate-size systems
* Used by: AWS IAM, Kubernetes RBAC, most SaaS apps

---

### ABAC (Attribute-Based Access Control)

```
Policy: IF user.department == "engineering" 
        AND resource.classification == "internal"
        AND time.hour BETWEEN 9 AND 17
        THEN ALLOW read
```

* Fine-grained, context-aware
* Evaluates attributes of user, resource, environment
* Used by: AWS IAM policies (conditions), Azure ABAC

---

### ReBAC (Relationship-Based Access Control)

```
User "Alice" → is_member_of → Team "Backend"
Team "Backend" → has_access_to → Repo "api-service"
∴ Alice has access to Repo "api-service"
```

* Models permissions as a graph of relationships
* Best for social/collaborative apps
* Used by: Google Zanzibar → SpiceDB, Auth0 FGA, Authzed

---

### ACL (Access Control List)

```
Resource: /documents/report.pdf
  ACL: [
    { user: "alice", permission: "read,write" },
    { user: "bob",   permission: "read" },
    { group: "admins", permission: "read,write,delete" }
  ]
```

* Permission list attached directly to resources
* Simple but doesn't scale well for large systems
* Used by: File systems, S3 bucket ACLs

---

## 3.3 Authorization Enforcement Points

```
┌────────┐     ┌──────────────┐     ┌────────────────┐     ┌──────────┐
│ Client │────►│ API Gateway  │────►│  Application   │────►│ Database │
└────────┘     │ (coarse AuthZ)│     │ (fine AuthZ)   │     │ (row-level)│
               └──────────────┘     └────────────────┘     └──────────┘
                Rate limiting         Business logic         Row-level security
                IP allowlists         Permission checks      Column masking
                Token validation      RBAC/ABAC rules        View-based access
```

---

# 4. OAuth 2.0

---

## 4.1 What is OAuth 2.0?

**OAuth 2.0** is an **authorization framework** that allows third-party applications to access a user's resources **without sharing credentials**.

> OAuth is about **delegated authorization**, NOT authentication (though OpenID Connect adds authentication on top).

---

## 4.2 Key Roles

| Role | Description | Example |
|------|-------------|---------|
| **Resource Owner** | The user who owns the data | End user |
| **Client** | App requesting access | Mobile app, SPA |
| **Authorization Server** | Issues tokens after consent | Google, Auth0, Okta |
| **Resource Server** | API that holds the data | Google Drive API |

---

## 4.3 OAuth 2.0 Authorization Code Flow (Most Secure)

```
┌──────────┐                          ┌───────────────────┐
│  Client  │                          │ Authorization     │
│  (App)   │                          │ Server            │
└────┬─────┘                          └────────┬──────────┘
     │                                         │
     │ 1. Redirect user to /authorize          │
     │────────────────────────────────────────►│
     │                                         │
     │         2. User logs in & consents      │
     │                                         │
     │ 3. Redirect back with authorization_code│
     │◄────────────────────────────────────────│
     │                                         │
     │ 4. Exchange code for tokens (backend)   │
     │────────────────────────────────────────►│
     │                                         │
     │ 5. Return access_token + refresh_token  │
     │◄────────────────────────────────────────│
     │                                         │
     │ 6. Use access_token to call Resource Server
     │──────────────────────────────────────────────► Resource Server
```

---

## 4.4 OAuth 2.0 Grant Types

| Grant Type | Use Case | Security |
|------------|----------|----------|
| **Authorization Code** | Server-side web apps | High (code exchanged server-side) |
| **Authorization Code + PKCE** | SPAs, mobile apps | High (prevents code interception) |
| **Client Credentials** | Machine-to-machine (no user) | High (service accounts) |
| **Device Code** | Smart TVs, CLI tools | Moderate |
| ~~**Implicit**~~ | ~~SPAs (deprecated)~~ | ❌ Deprecated — use PKCE instead |
| ~~**Password**~~ | ~~Trusted first-party apps~~ | ❌ Deprecated — avoid |

---

## 4.5 OAuth 2.0 Tokens

| Token | Purpose | Lifetime | Storage |
|-------|---------|----------|---------|
| **Access Token** | Authorize API requests | Short (5-60 min) | Memory |
| **Refresh Token** | Get new access tokens | Long (days-months) | Secure storage |
| **ID Token** (OpenID Connect) | User identity info | Short | Client-side |

---

# 5. JWT (JSON Web Token)

---

## 5.1 What is JWT?

A **JWT** is a compact, self-contained token format for securely transmitting claims between parties.

```
Header.Payload.Signature

eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4ifQ.signature
```

---

## 5.2 JWT Structure

```
┌─────────────────────────────────────────────────────────┐
│                        HEADER                            │
│  { "alg": "RS256", "typ": "JWT" }                      │
├─────────────────────────────────────────────────────────┤
│                        PAYLOAD (Claims)                  │
│  {                                                       │
│    "sub": "user123",          ← Subject (user ID)       │
│    "iss": "auth.example.com", ← Issuer                  │
│    "aud": "api.example.com",  ← Audience                │
│    "exp": 1716000000,         ← Expiration              │
│    "iat": 1715990000,         ← Issued At               │
│    "roles": ["admin","user"]  ← Custom claims           │
│  }                                                       │
├─────────────────────────────────────────────────────────┤
│                        SIGNATURE                         │
│  RS256(base64(header) + "." + base64(payload), secret)  │
└─────────────────────────────────────────────────────────┘
```

---

## 5.3 JWT Signing Algorithms

| Algorithm | Type | Description | Use Case |
|-----------|------|-------------|----------|
| **HS256** | Symmetric | HMAC with SHA-256 (shared secret) | Single service |
| **RS256** | Asymmetric | RSA with SHA-256 (private/public key pair) | Microservices |
| **ES256** | Asymmetric | ECDSA with SHA-256 (smaller keys) | Mobile, IoT |

**Asymmetric (RS256/ES256)** is preferred in distributed systems:
* Auth server signs with **private key**
* Any service verifies with **public key** (no shared secret needed)

---

## 5.4 JWT Validation Flow

```
┌────────┐      Bearer eyJ...     ┌──────────────┐
│ Client │ ─────────────────────► │  API Server  │
└────────┘                        └──────┬───────┘
                                         │
                                  1. Decode header
                                  2. Fetch public key (JWKS endpoint or cached)
                                  3. Verify signature
                                  4. Check exp (not expired)
                                  5. Check iss (trusted issuer)
                                  6. Check aud (intended audience)
                                  7. Extract claims → authorize
```

---

## 5.5 JWT Pros and Cons

| Pros | Cons |
|------|------|
| Stateless — no server-side session store | Cannot be revoked before expiry |
| Scales horizontally | Payload is only base64-encoded (not encrypted) |
| Cross-service/cross-domain friendly | Token size can be large (1-2 KB) |
| Contains claims → fewer DB lookups | Vulnerable if secret key is leaked |
| Standard (RFC 7519) | Must handle token refresh logic |

---

## 5.6 JWT Revocation Strategies

Since JWTs are stateless, revoking them before expiry requires extra mechanisms:

| Strategy | How it Works | Trade-off |
|----------|-------------|-----------|
| Short expiry + refresh tokens | Access token expires in 5 min; use refresh to get new | Latency on refresh |
| Token blacklist | Store revoked token IDs in Redis/DB, check on each request | Adds state (defeats statelessness) |
| Token versioning | Store a `token_version` per user; reject old versions | Requires lookup |
| Logout + client deletion | Remove token from client storage | Doesn't prevent stolen token use |

---

# 6. OAuth 2.0 vs JWT — Key Distinction

| Aspect | OAuth 2.0 | JWT |
|--------|-----------|-----|
| **What is it?** | Authorization FRAMEWORK (protocol/flow) | Token FORMAT (data structure) |
| **Purpose** | Defines how to get tokens securely | Defines how to structure/sign a token |
| **Scope** | End-to-end flow (redirects, consent, token exchange) | Just the token itself |
| **Relationship** | OAuth can use JWT as its token format | JWT can be used without OAuth |
| **Analogy** | The process of getting a boarding pass | The boarding pass itself |

```
OAuth 2.0 is the PROTOCOL → "How do I get a token?"
JWT is the FORMAT       → "What does the token look like?"

They work together:
  OAuth Authorization Code Flow → issues a JWT access token
```

---

## When to Use What

| Scenario | Recommendation |
|----------|---------------|
| Third-party access (Google login, GitHub app) | OAuth 2.0 + JWT |
| Internal microservice auth | JWT (signed by internal auth service) |
| Machine-to-machine | OAuth 2.0 Client Credentials → JWT |
| Simple stateless API auth | JWT (self-issued) |
| Need token revocation | Session-based OR JWT + blacklist |
| SSO across enterprise apps | SAML or OpenID Connect (OAuth + JWT) |

---

# 7. OpenID Connect (OIDC)

**OIDC** = OAuth 2.0 + Identity Layer

```
OAuth 2.0 → "This app can access your Google Drive" (authorization)
OIDC      → "This app knows you are john@gmail.com" (authentication)
```

OIDC adds:
* **ID Token** (JWT with user identity claims)
* **UserInfo endpoint** (fetch user profile)
* **Standard scopes:** `openid`, `profile`, `email`

```
Flow: Same as OAuth Authorization Code, but also returns an id_token

id_token payload:
{
  "sub": "user_12345",
  "email": "alice@example.com",
  "name": "Alice Smith",
  "iss": "https://accounts.google.com",
  "aud": "your-app-client-id",
  "exp": 1716000000
}
```

---

# 8. Encryption

---

## 8.1 Why Encryption?

Encryption ensures **confidentiality** — data is unreadable to unauthorized parties even if intercepted.

---

## 8.2 Encryption at Rest vs In Transit

| Type | Protects Against | Mechanism |
|------|-----------------|-----------|
| **In Transit** | Eavesdropping, MITM attacks | TLS/SSL (HTTPS) |
| **At Rest** | Stolen disks, unauthorized DB access | AES-256, disk encryption |
| **In Use** | Memory dumps, side-channel attacks | Secure enclaves, homomorphic encryption |

---

## 8.3 Symmetric Encryption

Same key for encryption and decryption.

```
Plaintext ──► [AES-256 + Key] ──► Ciphertext
Ciphertext ──► [AES-256 + Key] ──► Plaintext
```

| Algorithm | Key Size | Speed | Use Case |
|-----------|----------|-------|----------|
| AES-128 | 128 bits | Very Fast | General purpose |
| AES-256 | 256 bits | Fast | High security (at rest) |
| ChaCha20 | 256 bits | Fast (software) | TLS on mobile/IoT |

**Use case:** Encrypting data at rest (databases, S3, disk encryption).

---

## 8.4 Asymmetric Encryption

Different keys: public key encrypts, private key decrypts.

```
Plaintext ──► [RSA + Public Key] ──► Ciphertext
Ciphertext ──► [RSA + Private Key] ──► Plaintext
```

| Algorithm | Key Size | Speed | Use Case |
|-----------|----------|-------|----------|
| RSA-2048 | 2048 bits | Slow | Key exchange, signatures |
| RSA-4096 | 4096 bits | Very Slow | High-security signatures |
| ECDSA (P-256) | 256 bits | Moderate | TLS, JWT signing |
| Ed25519 | 256 bits | Fast | SSH keys, modern TLS |

**Use case:** TLS handshakes, JWT signatures, key exchange.

---

## 8.5 TLS/SSL (Transport Layer Security)

```
TLS Handshake (simplified):
┌────────┐                              ┌────────────┐
│ Client │                              │   Server   │
└───┬────┘                              └─────┬──────┘
    │  1. ClientHello (supported ciphers)     │
    │────────────────────────────────────────►│
    │                                         │
    │  2. ServerHello + Certificate (pub key) │
    │◄────────────────────────────────────────│
    │                                         │
    │  3. Client verifies cert (CA chain)     │
    │  4. Key Exchange (generate session key) │
    │────────────────────────────────────────►│
    │                                         │
    │  5. Both derive symmetric session key   │
    │  6. All subsequent data encrypted with  │
    │     symmetric key (AES-256-GCM)         │
    │◄───────────── Encrypted ───────────────►│
```

**Key insight:** Asymmetric crypto is used ONLY for the handshake (key exchange). Actual data encryption uses faster symmetric crypto.

---

## 8.6 Encryption in Distributed Systems

| Layer | What's Encrypted | Mechanism |
|-------|-----------------|-----------|
| Client ↔ Server | All API traffic | TLS 1.3 (HTTPS) |
| Service ↔ Service | Internal traffic | mTLS (mutual TLS) |
| Application ↔ Database | DB connections | TLS + connection encryption |
| Data at rest (DB) | Stored records | AES-256 (TDE or app-level) |
| Data at rest (Disk) | Full disk | LUKS, BitLocker, AWS EBS encryption |
| Data at rest (Object Store) | Files/blobs | S3 SSE-S3, SSE-KMS |
| Secrets | API keys, passwords | Vault (HashiCorp), AWS KMS |

---

## 8.7 Key Management

```
┌──────────────────────────────────────────────────────────┐
│                   Key Management Service                   │
│                                                           │
│  Master Key (HSM-protected, never leaves hardware)        │
│       │                                                   │
│       ▼                                                   │
│  Data Encryption Keys (DEKs) — encrypted by Master Key   │
│       │                                                   │
│       ▼                                                   │
│  Envelope Encryption:                                     │
│    1. Generate DEK                                        │
│    2. Encrypt data with DEK                               │
│    3. Encrypt DEK with Master Key                         │
│    4. Store encrypted data + encrypted DEK together       │
│    5. To decrypt: unwrap DEK with Master Key, then data   │
│                                                           │
└──────────────────────────────────────────────────────────┘
```

**Services:** AWS KMS, Google Cloud KMS, Azure Key Vault, HashiCorp Vault

---

## 8.8 Hashing (One-Way)

Hashing is NOT encryption (cannot be reversed), but critical for security:

| Use Case | Algorithm | Example |
|----------|-----------|---------|
| Password storage | bcrypt, scrypt, Argon2 | Store hash, never plaintext |
| Data integrity | SHA-256 | Verify file hasn't been tampered |
| Deduplication | SHA-256, MD5 | Detect duplicate content |
| Token generation | HMAC-SHA256 | Generate CSRF tokens |

```
Password Flow:
  Registration: password → bcrypt(password + salt) → store hash
  Login:        password → bcrypt(password + stored_salt) → compare with stored hash
```

> **Never** use MD5 or SHA-1 for passwords. Use bcrypt/scrypt/Argon2 (slow by design to resist brute force).

---

# 9. DDoS Attacks (Distributed Denial of Service)

---

## 9.1 What is a DDoS Attack?

A **DDoS attack** overwhelms a system with massive traffic from many distributed sources, making it unavailable to legitimate users.

```
                    ┌── Bot 1 ──┐
                    ├── Bot 2 ──┤
Attacker ──C&C────►├── Bot 3 ──┼──────► Target Server (overwhelmed)
                    ├── Bot 4 ──┤
                    ├── ...     │
                    └── Bot N ──┘
                    
              Botnet (thousands/millions of compromised devices)
```

---

## 9.2 Types of DDoS Attacks

---

### Layer 3/4 (Network/Transport Layer)

| Attack | Mechanism | Target |
|--------|-----------|--------|
| **SYN Flood** | Send millions of TCP SYN packets without completing handshake | Server connection table |
| **UDP Flood** | Blast random UDP packets to overwhelm bandwidth | Network bandwidth |
| **ICMP Flood (Ping)** | Flood with ping requests | Network/CPU |
| **Amplification** | Spoof victim's IP; send small request to reflector that sends large response to victim | Bandwidth (DNS, NTP, Memcached) |

```
SYN Flood:
  Attacker → SYN → Server (allocates connection state)
  Attacker → SYN → Server (allocates more state)
  × 1,000,000 → Server connection table full → legitimate clients rejected
```

---

### Layer 7 (Application Layer)

| Attack | Mechanism | Target |
|--------|-----------|--------|
| **HTTP Flood** | Send millions of legitimate-looking HTTP requests | Web server / application |
| **Slowloris** | Open connections, send headers very slowly, keep connections alive | Connection pool exhaustion |
| **API abuse** | Hit expensive endpoints (search, reports) repeatedly | Application CPU/DB |
| **Cache bypass** | Random query params to bypass CDN cache | Origin server |

```
HTTP Flood:
  Bot → GET /search?q=random1 → Server (expensive DB query)
  Bot → GET /search?q=random2 → Server (another expensive query)
  × 100,000 concurrent → Server CPU/DB saturated
```

---

### DNS Attacks

| Attack | Mechanism |
|--------|-----------|
| **DNS Flood** | Overwhelm DNS servers with queries |
| **DNS Amplification** | Small query → large response directed at victim |
| **NXDOMAIN Attack** | Query non-existent domains to exhaust DNS resolver cache |

---

## 9.3 DDoS Mitigation Strategies

---

### Infrastructure Level

| Strategy | Description |
|----------|-------------|
| **CDN / Edge Network** | Distribute traffic globally; absorb attacks at edge (CloudFlare, AWS CloudFront, Akamai) |
| **Anycast Routing** | Route traffic to nearest data center; distributes attack across many PoPs |
| **Over-provisioning** | Maintain excess capacity to absorb traffic spikes |
| **Auto-scaling** | Automatically scale up resources during attack |
| **Scrubbing Centers** | Route traffic through DDoS mitigation providers that filter malicious traffic |

---

### Network Level

| Strategy | Description |
|----------|-------------|
| **Rate Limiting** | Limit requests per IP / per user / per endpoint |
| **IP Blocklisting** | Block known malicious IPs (threat intelligence feeds) |
| **Geo-blocking** | Block traffic from unexpected geographic regions |
| **SYN Cookies** | Stateless SYN flood mitigation (no connection state until handshake completes) |
| **BGP Blackholing** | Null-route attack traffic at network edge (last resort, drops all traffic to target) |

---

### Application Level

| Strategy | Description |
|----------|-------------|
| **WAF (Web Application Firewall)** | Filter malicious HTTP requests (patterns, signatures) |
| **CAPTCHA / Challenge** | Prove human during suspicious activity |
| **Bot Detection** | Fingerprint, behavior analysis, JavaScript challenges |
| **Request Throttling** | Limit expensive operations per user/session |
| **Queue-based processing** | Buffer requests in queue; process at sustainable rate |
| **Caching** | Serve cached responses; reduce origin load |
| **Circuit Breaker** | Shed load when system is overwhelmed |

---

## 9.4 DDoS Mitigation Architecture

```
                         Internet
                            │
                            ▼
                   ┌─────────────────┐
                   │  DNS (Anycast)  │
                   └────────┬────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │   CDN / Edge    │  ← Absorb volumetric attacks
                   │  (CloudFlare)   │  ← Cache static content
                   └────────┬────────┘  ← Bot detection
                            │
                            ▼
                   ┌─────────────────┐
                   │      WAF        │  ← Filter L7 attacks
                   │                 │  ← Rate limiting rules
                   └────────┬────────┘  ← IP reputation
                            │
                            ▼
                   ┌─────────────────┐
                   │  Load Balancer  │  ← Connection limits
                   │                 │  ← Health checks
                   └────────┬────────┘  ← SYN flood protection
                            │
                            ▼
                   ┌─────────────────┐
                   │  Application    │  ← Request throttling
                   │  Servers (ASG)  │  ← Circuit breakers
                   └─────────────────┘  ← Auto-scaling
```

---

## 9.5 Rate Limiting Algorithms

| Algorithm | Description | Best For |
|-----------|-------------|----------|
| **Fixed Window** | Count requests in fixed time windows (e.g., 100/min) | Simple APIs |
| **Sliding Window** | Rolling window for smoother limiting | Most APIs |
| **Token Bucket** | Tokens refill at fixed rate; request consumes token | Bursty traffic |
| **Leaky Bucket** | Requests queue and process at fixed rate | Smoothing traffic |

```
Token Bucket Example:
  Bucket capacity: 100 tokens
  Refill rate: 10 tokens/second
  
  Burst: Can handle 100 requests instantly
  Sustained: 10 requests/second
  
  If bucket empty → reject/queue request (429 Too Many Requests)
```

---

## 9.6 Real-World DDoS Numbers

| Metric | Scale |
|--------|-------|
| Largest recorded attack | 3.47 Tbps (Microsoft Azure, 2022) |
| Typical botnet size | 100K - 1M+ devices |
| Common amplification factor | 50-70x (Memcached: 51,000x) |
| Time to detect | Seconds to minutes with automated detection |
| Cost of attack | As low as $20/hour on dark web |

---

# 10. Additional Security Concerns in System Design

---

## 10.1 Common Attack Vectors

| Attack | Layer | Mitigation |
|--------|-------|------------|
| **SQL Injection** | Application | Parameterized queries, ORM |
| **XSS** | Client | Output encoding, CSP headers |
| **CSRF** | Application | Anti-CSRF tokens, SameSite cookies |
| **SSRF** | Server | Allowlist outbound URLs, network isolation |
| **Man-in-the-Middle** | Network | TLS everywhere, certificate pinning |
| **Credential Stuffing** | Authentication | Rate limiting, MFA, breached password detection |
| **Privilege Escalation** | Authorization | Principle of least privilege, input validation |

---

## 10.2 Security Headers

```http
Strict-Transport-Security: max-age=31536000; includeSubDomains
Content-Security-Policy: default-src 'self'; script-src 'self'
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: strict-origin-when-cross-origin
Permissions-Policy: camera=(), microphone=()
```

---

## 10.3 Zero Trust Architecture

> "Never trust, always verify" — no implicit trust based on network location.

```
Traditional: Trust everything inside the corporate network
Zero Trust:  Verify every request regardless of source

Principles:
  1. Verify explicitly (AuthN + AuthZ on every request)
  2. Least privilege access
  3. Assume breach (encrypt, segment, monitor)
```

---

# 11. Privacy in System Design

---

## 11.1 Key Privacy Regulations

| Regulation | Region | Key Requirements |
|------------|--------|-----------------|
| **GDPR** | EU | Consent, right to erasure, data portability, breach notification |
| **CCPA/CPRA** | California | Right to know, delete, opt-out of sale |
| **HIPAA** | US (Healthcare) | PHI protection, access controls, audit trails |
| **PCI-DSS** | Global (Payments) | Card data encryption, network segmentation |

---

## 11.2 Privacy by Design Principles

| Principle | Implementation |
|-----------|---------------|
| Data minimization | Collect only what's needed |
| Purpose limitation | Use data only for stated purpose |
| Storage limitation | Delete data when no longer needed (TTL) |
| Encryption | Encrypt PII at rest and in transit |
| Pseudonymization | Replace identifiers with tokens |
| Access control | Restrict who can access PII |
| Audit logging | Log all access to sensitive data |
| Right to erasure | Support data deletion requests |

---

## 11.3 Data Classification

```
┌─────────────────────────────────────────────────┐
│  PUBLIC        │ Marketing content, docs         │
├─────────────────────────────────────────────────┤
│  INTERNAL      │ Internal tools, non-sensitive   │
├─────────────────────────────────────────────────┤
│  CONFIDENTIAL  │ PII, business data              │
├─────────────────────────────────────────────────┤
│  RESTRICTED    │ Passwords, payment cards, PHI   │
└─────────────────────────────────────────────────┘

Higher classification → stricter encryption, access, and audit requirements
```

---

# 12. Security in Microservices

```
┌────────────────────────────────────────────────────────┐
│                    API Gateway                          │
│  • TLS termination                                     │
│  • Rate limiting                                       │
│  • JWT validation                                      │
│  • Request sanitization                                │
└──────────────────────┬─────────────────────────────────┘
                       │ mTLS
         ┌─────────────┼─────────────┐
         ▼             ▼             ▼
   ┌──────────┐  ┌──────────┐  ┌──────────┐
   │Service A │──│Service B │──│Service C │
   │          │  │          │  │          │
   └──────────┘  └──────────┘  └──────────┘
         │ mTLS        │ mTLS        │ mTLS
         ▼             ▼             ▼
   ┌──────────┐  ┌──────────┐  ┌──────────┐
   │   DB A   │  │   DB B   │  │   DB C   │
   │(encrypted)│  │(encrypted)│  │(encrypted)│
   └──────────┘  └──────────┘  └──────────┘

Service Mesh (Istio/Linkerd): automates mTLS between all services
```

---

# 13. Best Practices Summary

1. **Use HTTPS everywhere** — TLS 1.3 for all external and internal traffic
2. **Implement proper authentication** — OAuth 2.0 + PKCE for user-facing, mTLS for services
3. **Use short-lived tokens** — Access tokens expire in minutes; use refresh tokens
4. **Encrypt data at rest** — AES-256 with proper key management (KMS)
5. **Apply least privilege** — Minimum necessary permissions for every identity
6. **Rate limit everything** — per user, per IP, per endpoint
7. **Deploy DDoS protection** — CDN + WAF + auto-scaling
8. **Never store secrets in code** — use Vault, KMS, or environment-specific secret managers
9. **Log security events** — authentication failures, permission denials, anomalies
10. **Validate all input** — at system boundaries (APIs, message consumers)
11. **Use parameterized queries** — prevent SQL injection
12. **Implement security headers** — CSP, HSTS, X-Frame-Options
13. **Regular security audits** — penetration testing, dependency scanning
14. **Plan for breach** — incident response plan, encryption limits blast radius

---

# 14. Interview Key Points

* **Authentication = WHO** (identity); **Authorization = WHAT** (permissions)
* **OAuth 2.0 is a framework** (protocol for getting tokens); **JWT is a format** (token structure)
* OAuth + OIDC together provide both authorization and authentication
* **JWT is stateless** — great for scale, but revocation is hard (use short expiry + refresh)
* **Asymmetric signing (RS256)** preferred in distributed systems (verify without shared secret)
* **Encryption in transit** = TLS; **at rest** = AES-256; **key management** = KMS/Vault
* **DDoS mitigation** is multi-layered: CDN → WAF → Rate Limiting → Auto-scaling
* **Rate limiting** protects against both DDoS and API abuse (Token Bucket most common)
* **Zero Trust** = verify every request, regardless of network location
* **mTLS** for service-to-service authentication in microservices
* Always mention **defense in depth** — security at every layer, not just the perimeter

---

# 15. Quick Reference Table

| Concern | Solution | Tools/Services |
|---------|----------|---------------|
| User authentication | OAuth 2.0 + OIDC | Auth0, Okta, Keycloak |
| Service authentication | mTLS, JWT | Istio, cert-manager |
| Authorization | RBAC, ABAC, ReBAC | OPA, Casbin, SpiceDB |
| Encryption in transit | TLS 1.3 | Let's Encrypt, ACM |
| Encryption at rest | AES-256 | AWS KMS, Vault |
| Secret management | Centralized vault | HashiCorp Vault, AWS Secrets Manager |
| DDoS protection | CDN + WAF | CloudFlare, AWS Shield, Akamai |
| Rate limiting | Token bucket / sliding window | Redis, API Gateway |
| Input validation | WAF + application-level | ModSecurity, AWS WAF |
| Monitoring | SIEM, anomaly detection | Splunk, Datadog, Sentinel |

---

> **Key Takeaway:** Security in distributed systems is about **defense in depth** — layering authentication, authorization, encryption, rate limiting, and monitoring so that no single failure compromises the entire system. OAuth 2.0 + JWT provide scalable, standard-based identity; encryption protects data everywhere; and DDoS mitigation ensures availability under attack.