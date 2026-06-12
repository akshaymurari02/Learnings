# 🔁 Consistent Hashing & Virtual Nodes — Complete Guide

---

## 📌 Problem: Hash-Based Sharding Limitation

Traditional hashing uses a simple modulo operation:
```text
shard = hash(key) % N
```

### ❌ The Issue
If the number of shards ($N$) changes (e.g., $N = 3 \rightarrow N = 2$), **almost all keys get remapped**.

**Example:**

| Key | Old (mod 3) | New (mod 2) |
| :--- | :--- | :--- |
| k1 | B | C |
| k2 | A | A |
| k3 | C | A |

**🚨 Result:**
* Full data reshuffle ❌
* High migration cost ❌
* Cache invalidation ❌

---

## 🚀 Solution: Consistent Hashing

### 🧠 Core Idea
Map both keys and nodes onto a circular hash space (a "hash ring").

### 🔁 Step-by-Step Process
1. **Create Hash Ring:** `0 ----------------------> MAX_HASH` (wraps around circularly)
2. **Place Nodes on Ring:**
   * Node A $\rightarrow$ `hash(A)`
   * Node B $\rightarrow$ `hash(B)`
   * Node C $\rightarrow$ `hash(C)`
3. **Place Keys:** `key` $\rightarrow$ `hash(key)`
4. **Assignment Rule:** A key is assigned to the **next node in the clockwise direction**.

### 🔢 Example (3 Nodes)
**Ring:** `0 ---- A ---- B -------- C ---- (wrap)`

**Key Mapping:**
* `key1` $\rightarrow$ goes to B
* `key2` $\rightarrow$ goes to C
* `key3` $\rightarrow$ goes to A

---

## ❌ Scenario: Remove Node B

**New Ring:** `0 ---- A ----------- C ---- (wrap)`

### 🔥 What Happens?
* **Keys previously on B (`key1`):** Move to the next node $\rightarrow$ **C**
* **Keys on A:** Remain on A ✅
* **Keys on C:** Remain on C ✅

**🎯 Result:** Only B’s data moves!

**⚠️ The New Problem:** 
Node C now handles its own data **plus** B’s data. This leads to **Load Imbalance ❌**.

---

## 🚀 Solution: Virtual Nodes (VNodes)

### 🧠 Core Idea
Instead of 1 position per physical node, assign multiple virtual positions:
* **A** $\rightarrow$ A1, A2, A3
* **B** $\rightarrow$ B1, B2, B3
* **C** $\rightarrow$ C1, C2, C3

### 🔍 Ring with Virtual Nodes
`0 ---- A1 ---- B1 ---- C1 ---- A2 ---- B2 ---- C2 ---- A3 ---- B3 ---- C3 ---- (wrap)`

**🔁 Key Assignment:** 
`key` $\rightarrow$ nearest vnode clockwise $\rightarrow$ mapped to its physical node.

### ❌ Scenario: Remove Node B (with VNodes)
Remove `B1`, `B2`, `B3` from the ring.

**🔥 Redistribution:**
Each virtual node transfers its load independently:
* `B1` $\rightarrow$ next vnode (`C1`)
* `B2` $\rightarrow$ next vnode (`A3`)
* `B3` $\rightarrow$ next vnode (`C2`)

**🎯 Result:** Load is distributed across **both** A and C, avoiding a single point of concentration.

### ⚖️ Comparison

| Scenario | Behavior |
| :--- | :--- |
| **No virtual nodes** | C becomes overloaded ❌ |
| **With virtual nodes** | Load is evenly distributed ✅ |

---

## 🧠 Key Insights
1. **Insight 1:** Consistent hashing minimizes data movement during scaling.
2. **Insight 2:** Virtual nodes improve load balancing and prevent hotspots.
3. **Insight 3:** Hashing distributes *keys*, not necessarily *traffic* (if one key is highly active).

---

## 📊 Why Virtual Nodes Work
* **Without vnodes:** 1 node = 1 large range $\rightarrow$ uneven load distribution.
* **With vnodes:** 1 node = many small ranges interleaved $\rightarrow$ evenly distributed load.

### ⚙️ Practical Details
* **Number of Virtual Nodes:** Typically 100–200 vnodes per physical node.
* **Benefits:** Better load balancing, smooth scaling, reduced hotspots.
* **Trade-offs:** More metadata to maintain, slightly higher complexity.

---

## 🌐 Real-World Systems
* **Cassandra:** Uses virtual nodes heavily.
* **DynamoDB:** Uses a similar consistent hashing approach.
* **Redis Cluster:** Uses hash slots (a form of fixed partitions mapping to nodes).

---

## 🎯 Interview One-Liners

* **Consistent Hashing:** "Distributes keys on a hash ring so that adding or removing nodes affects only a small, localized subset of data rather than triggering a global reshuffle."
* **Virtual Nodes:** "Represent a physical node multiple times on the hash ring to ensure uniform data distribution and significantly better load balancing during scaling."

---

## ⚡ Final Takeaway
* **Modulo hashing** $\rightarrow$ Global reshuffle ❌
* **Consistent hashing** $\rightarrow$ Local adjustment ✅
* **Consistent hashing + vnodes** $\rightarrow$ Perfectly balanced system ✅
