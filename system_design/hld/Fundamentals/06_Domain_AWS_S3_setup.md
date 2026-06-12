# 📘 HLD Fundamentals — Domain (GoDaddy) → AWS S3 + CloudFront Setup

---

## 1. Problem Statement

You bought a domain from a registrar (e.g., GoDaddy) and want to host a frontend using:

* S3 (static hosting)
* CloudFront (CDN)

Goal: When user opens `myapp.com`, it should load your frontend.

---

## 2. Components Involved

* Domain Registrar → GoDaddy
* DNS Service → Route 53 (or GoDaddy DNS)
* CDN → CloudFront
* Origin → S3 bucket

---

## 3. High-Level Architecture

```
User
 ↓
DNS (Route53 / GoDaddy)
 ↓
CloudFront (CDN)
 ↓
S3 (Static Website)
```

---

## 4. Step-by-Step Setup

### Step 1: Create S3 Bucket

* Bucket name = domain name (e.g., `myapp.com`)
* Enable static website hosting
* Upload frontend files (HTML, CSS, JS)

---

### Step 2: Create CloudFront Distribution

* Origin = S3 bucket
* Enable caching
* Configure default root object (`index.html`)

CloudFront gives a domain like:

```
d123abc.cloudfront.net
```

---

### Step 3: Add Custom Domain to CloudFront

* Add `myapp.com` as alternate domain (CNAME)
* Attach SSL certificate (via ACM)

---

### Step 4: Configure DNS

#### Option A: Using Route 53 (recommended)

1. Create hosted zone for `myapp.com`
2. Update nameservers in GoDaddy → point to Route53
3. Add record:

```
myapp.com → CloudFront (Alias record)
```

---

#### Option B: Using GoDaddy DNS

Add CNAME record:

```
www.myapp.com → d123abc.cloudfront.net
```

For root domain:

* Use forwarding or ALIAS/ANAME if supported

---

## 5. Request Flow

```
1. User enters myapp.com
2. DNS resolves to CloudFront
3. CloudFront edge receives request
4. Cache HIT → return
5. Cache MISS → fetch from S3 → cache → return
```

---

## 6. Important Concepts

### Nameserver Delegation

* GoDaddy manages domain
* Route53 manages DNS (after NS update)

---

### CNAME vs A Record

* CNAME → maps domain to another domain
* Alias (Route53) → maps to CloudFront directly

---

### SSL (HTTPS)

* Use ACM certificate
* Attach to CloudFront

---

## 7. Best Practices

* Use CloudFront instead of direct S3 access
* Keep S3 bucket private (use OAC/OAI)
* Use versioning for cache invalidation

---

## 8. Common Mistakes

* Not updating nameservers
* Using S3 public URL instead of CloudFront
* Forgetting SSL configuration

---

## 9. Interview Summary

A domain purchased from GoDaddy can be connected to an S3-hosted frontend using CloudFront. DNS is either managed by GoDaddy or delegated to Route53. CloudFront acts as a CDN and serves content from edge locations, while S3 acts as the origin storage.

---
