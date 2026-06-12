# 📘 HLD Fundamentals — Quorum (Deep Dive)

---

# 1. Introduction

A **Quorum** is the minimum number of nodes in a distributed system that must agree on an operation (read or write) for that operation to be considered successful.

It ensures:

* Data consistency across replicas
* Fault tolerance when some nodes are unavailable
* Coordination without requiring all nodes to respond

> A quorum guarantees that every read will see the most recent write, as long as the overlap condition is met.

---

# 2. Why Quorum is Needed

In distributed systems, data is replicated across multiple nodes for:

* High availability
* Fault tolerance
* Low latency (serve from nearest replica)

But replication introduces a fundamental challenge:

> How do you ensure reads return the latest written data when replicas can be out of sync?

---

## Without Quorum

```
Client writes to Node A only
Client reads from Node B → stale data ❌
```

---

## With Quorum

```
Client writes to Node A, B (W=2)
Client reads from Node B, C (R=2)

Overlap with latest write guaranteed → fresh data ✅
```

---

# 3. Core Concept — The Quorum Formula

Given a replication factor **N** (total replicas), define:

* **W** = number of nodes that must acknowledge a **write**
* **R** = number of nodes that must acknowledge a **read**

### The Consistency Rule

$$W + R > N$$

When this condition holds, read and write sets **always overlap** — guaranteeing the read sees the most recent write.

---

## Why Does Overlap Work?

```
N = 3 replicas: [A, B, C]

Write acknowledged by: A, B   → W = 2
Read  acknowledged by: B, C   → R = 2

W + R = 4 > 3 (N)

Overlap node: B → has the latest value ✅
```

The system picks the value with the highest timestamp/version from the responding nodes.

---

# 4. Common Quorum Configurations

---

## 4.1 Strict Majority Quorum (Balanced)

```
N = 3, W = 2, R = 2
```

* W + R = 4 > 3 ✅
* Tolerates **1 node failure** for both reads and writes
* Most common configuration

---

## 4.2 Write-Heavy Optimization

```
N = 3, W = 1, R = 3
```

* W + R = 4 > 3 ✅
* Fast writes (only 1 acknowledgment needed)
* Slow reads (must contact all replicas)
* Use case: logging, event streaming

---

## 4.3 Read-Heavy Optimization

```
N = 3, W = 3, R = 1
```

* W + R = 4 > 3 ✅
* Fast reads (single node response sufficient)
* Slow writes (all replicas must acknowledge)
* Use case: read-heavy workloads, configuration stores

---

## 4.4 Weak Consistency (No Quorum)

```
N = 3, W = 1, R = 1
```

* W + R = 2 < 3 ❌
* No overlap guaranteed
* Eventual consistency — may read stale data
* Use case: best-effort reads, metrics/counters

---

## Configuration Comparison

| Config | W | R | W + R > N | Write Speed | Read Speed | Consistency |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| Balanced | 2 | 2 | ✅ | Medium | Medium | Strong |
| Write-Heavy | 1 | 3 | ✅ | Fast | Slow | Strong |
| Read-Heavy | 3 | 1 | ✅ | Slow | Fast | Strong |
| Weak | 1 | 1 | ❌ | Fast | Fast | Eventual |

---

# 5. Fault Tolerance

The maximum number of node failures a system can tolerate:

* **Write tolerance**: N − W failures
* **Read tolerance**: N − R failures

---

## Example

```
N = 5, W = 3, R = 3

Write tolerance = 5 - 3 = 2 node failures
Read  tolerance = 5 - 3 = 2 node failures
```

With 5 replicas and quorum of 3, the system survives **2 simultaneous failures**.

---

## General Rule

For a system with $N$ replicas, to tolerate $f$ failures:

$$N \geq 2f + 1$$

This ensures a majority can always be reached.

---

# 6. How Reads and Writes Work

---

## 6.1 Quorum Write

```
Client
  │
  ├──► Node A  → ACK ✅
  ├──► Node B  → ACK ✅
  └──► Node C  → (slow / down) ⏳

W = 2 → 2 ACKs received → Write SUCCESS
```

Steps:

1. Client sends write to **all N replicas** (or a coordinator does)
2. Each replica writes locally and acknowledges
3. Once **W** acknowledgments arrive → write is successful
4. Remaining replicas sync asynchronously (anti-entropy / read repair)

---

## 6.2 Quorum Read

```
Client
  │
  ├──► Node A  → value v2, timestamp=102
  ├──► Node B  → value v1, timestamp=101
  └──► Node C  → (slow / down) ⏳

R = 2 → 2 responses received
Pick latest: v2 (timestamp=102) ✅
```

Steps:

1. Client sends read to **all N replicas**
2. Wait for **R** responses
3. Pick the value with the **highest version/timestamp**
4. Optionally trigger **read repair** on stale nodes

---

# 7. Read Repair

When a quorum read detects stale values on some nodes, the system repairs them.

```
Read from A, B, C:
  A → v3 (latest)
  B → v2 (stale)
  C → v3 (latest)

Read repair: push v3 to Node B
```

### Benefits

* Passive consistency mechanism
* No extra background process needed
* Fixes inconsistency on the read path

---

# 8. Sloppy Quorum & Hinted Handoff

---

## 8.1 The Problem

Strict quorum requires W (or R) of the **designated** N replicas to respond.

If too many designated replicas are down, operations fail — even if other healthy nodes exist.

```
N = 3 → Replicas: A, B, C
Nodes A, B are down

Strict Quorum (W=2): No 2 designated nodes available → Write FAILS ❌
```

---

## 8.2 Sloppy Quorum

Allows writes to **any** W nodes in the cluster — not just the designated replicas.

```
N = 3 → Designated: A, B, C
Nodes A, B are down

Sloppy Quorum: Write to C + D (temporary)
W = 2 satisfied → Write SUCCESS ✅
```

### Trade-off

* Higher availability ✅
* Weaker consistency guarantee ⚠️ (reads from designated nodes may miss this write)

---

## 8.3 Hinted Handoff

When a temporary node (D) receives a write on behalf of a down node (A):

1. D stores the value with a **hint** → "This belongs to A"
2. When A recovers → D forwards the data to A
3. D deletes the hinted copy

```
Node A goes down
  ↓
Node D accepts write with hint: "for A"
  ↓
Node A comes back
  ↓
Node D → pushes data to A
  ↓
Node D deletes hinted data
```

---

# 9. Anti-Entropy with Merkle Trees

To detect and resolve inconsistencies across replicas, systems use **Merkle trees**.

---

## How It Works

1. Each replica builds a **hash tree** over its data
2. Replicas compare root hashes
3. If roots differ → traverse the tree to find divergent branches
4. Sync only the differing data

```
Replica A                    Replica B
    Root: abc123                Root: abc456
       /    \                      /    \
    Left   Right              Left   Right
   (match)  (differ)         (match)  (differ)
                ↓                        ↓
         Sync only this subtree
```

### Benefits

* Minimal data transfer
* Efficient detection of inconsistencies
* Used by: Cassandra, DynamoDB, Riak

---

# 10. Quorum and CAP Theorem

Quorum plays a direct role in the CAP trade-off:

---

## CP System — Strong Consistency

```
W + R > N

Reads always see latest writes
May sacrifice availability if quorum cannot be formed
```

Example: W=2, R=2, N=3 → CP behavior

---

## AP System — High Availability

```
W + R ≤ N  OR  sloppy quorum

System stays available even during partitions
May return stale data
```

Example: W=1, R=1, N=3 → AP behavior

---

## Tunable Consistency

Many modern systems let you **tune** W and R per operation:

* Critical operations → strict quorum (W=2, R=2)
* Non-critical operations → relaxed (W=1, R=1)

This is called **tunable consistency**.

---

# 11. Quorum in Consensus Protocols

Quorum is a foundational concept in distributed consensus:

| Protocol | Quorum Used For |
| :--- | :--- |
| **Paxos** | Majority of acceptors must agree on a proposal |
| **Raft** | Majority vote for leader election + log replication |
| **Zab (ZooKeeper)** | Majority acknowledgment for writes |
| **2PC** | All participants must agree (strict quorum) |
| **3PC** | Majority-based decisions to avoid blocking |

---

# 12. Real-World Systems Using Quorum

---

## 12.1 Apache Cassandra

* Default: `N=3, W=QUORUM, R=QUORUM`
* QUORUM = $\lfloor N/2 \rfloor + 1$
* Supports tunable consistency levels: `ONE`, `QUORUM`, `ALL`, `LOCAL_QUORUM`

---

## 12.2 Amazon DynamoDB

* Uses sloppy quorum + hinted handoff
* Favors availability (AP system)
* Anti-entropy via Merkle trees

---

## 12.3 Apache ZooKeeper

* Uses strict quorum for writes
* Majority of nodes must acknowledge
* Strong consistency (CP system)

---

## 12.4 etcd / Consul (Raft-based)

* Leader-based replication
* Writes committed when majority acknowledges
* Strongly consistent reads through leader

---

## 12.5 Riak

* Tunable N, R, W per bucket
* Supports sloppy quorum
* Conflict resolution via vector clocks or CRDTs

---

# 13. Advantages of Quorum

* **Consistency control** — tune W and R to match needs
* **Fault tolerance** — survives minority node failures
* **No single point of failure** — no master required in leaderless designs
* **Flexible trade-offs** — adjust latency vs. consistency per operation
* **Proven model** — backed by decades of distributed systems research

---

# 14. Disadvantages of Quorum

* **Latency overhead** — must wait for multiple nodes to respond
* **Network dependency** — requires reliable communication between nodes
* **Conflict resolution** — concurrent writes may create conflicts (last-write-wins, vector clocks)
* **Sloppy quorum risks** — may violate consistency guarantees temporarily
* **Complexity** — tuning W, R, N correctly requires careful analysis

---

# 15. Summary

| Concept | Description |
| :--- | :--- |
| **Quorum** | Minimum nodes needed to agree on an operation |
| **Formula** | $W + R > N$ for strong consistency |
| **W** | Write acknowledgments required |
| **R** | Read acknowledgments required |
| **N** | Total replicas |
| **Sloppy Quorum** | Write to any available nodes (not just designated) |
| **Hinted Handoff** | Temporary node holds data until original recovers |
| **Read Repair** | Fix stale replicas during read operations |
| **Anti-Entropy** | Background sync using Merkle trees |
| **Tunable Consistency** | Adjust W, R per operation for trade-off control |

---

# 16. Key Takeaways

* Quorum is the backbone of consistency in leaderless replication systems
* $W + R > N$ guarantees overlap between readers and writers
* Sloppy quorum + hinted handoff trades consistency for availability
* Real systems like Cassandra and DynamoDB rely heavily on quorum
* Quorum connects directly to CAP and PACELC trade-offs
* Choose W, R values based on your consistency and latency requirements