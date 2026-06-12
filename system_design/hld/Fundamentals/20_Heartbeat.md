# 📘 HLD Fundamentals — Heartbeat (Deep Dive)

---

# 1. Introduction

A **Heartbeat** is a periodic signal sent between nodes in a distributed system to indicate that a node is alive and functioning.

It is the simplest and most widely used **failure detection mechanism**.

> If a node stops sending heartbeats, other nodes assume it has failed and take corrective action.

---

## Core Purpose

* Detect node failures
* Maintain cluster membership
* Trigger failover and recovery
* Monitor health of services

---

# 2. Why Heartbeats are Needed

In distributed systems, nodes can fail due to:

* Hardware crashes
* Network partitions
* Software bugs
* Resource exhaustion (CPU, memory, disk)

The challenge:

> A node cannot distinguish between a slow node and a dead node without a probing mechanism.

Heartbeats solve this by providing a **continuous liveness signal**.

---

# 3. How Heartbeats Work

---

## 3.1 Basic Mechanism

```
Node A ──── heartbeat ────► Node B
Node A ──── heartbeat ────► Node B
Node A ──── heartbeat ────► Node B
Node A ──── ❌ (no signal) ──► Node B

Node B: "No heartbeat from A for 3 intervals → A is presumed dead"
```

Steps:

1. Each node sends a heartbeat message at a fixed **interval** (e.g., every 1 second)
2. The receiver tracks the **last received timestamp** per sender
3. If no heartbeat arrives within the **timeout** threshold → node is marked as failed
4. Corrective action is triggered (failover, rebalancing, alerting)

---

## 3.2 Key Parameters

| Parameter | Description | Typical Value |
| :--- | :--- | :--- |
| **Heartbeat Interval** | How often the signal is sent | 1–5 seconds |
| **Timeout** | How long to wait before declaring failure | 3–10 seconds |
| **Missed Count** | Number of missed heartbeats before failure | 2–5 |

### Timeout Formula

$$\text{Timeout} = \text{Heartbeat Interval} \times \text{Missed Count}$$

Example:

```
Interval = 2s, Missed Count = 3
Timeout  = 2 × 3 = 6 seconds
```

---

# 4. Types of Heartbeat Mechanisms

---

## 4.1 Push-Based Heartbeat

The monitored node **actively sends** heartbeat signals to observers.

```
Worker Node ──► heartbeat ──► Master / Monitor
Worker Node ──► heartbeat ──► Master / Monitor
Worker Node ──► heartbeat ──► Master / Monitor
```

### Characteristics

* Sender is responsible for liveness signals
* Simple to implement
* Observer passively listens
* Used in: master-worker architectures

---

## 4.2 Pull-Based Heartbeat (Polling)

The monitor **actively polls** each node for health status.

```
Master ──► "Are you alive?" ──► Worker Node
Master ◄── "Yes, I'm alive" ◄── Worker Node
```

### Characteristics

* Monitor controls the schedule
* Higher overhead on the monitor side
* Easier to centralize health tracking
* Used in: load balancers, health-check endpoints

---

## 4.3 Gossip-Based Heartbeat

Nodes **gossip** heartbeat information to random peers — no central monitor needed.

```
Node A → tells Node B: "I'm alive, and C was alive 2s ago"
Node B → tells Node D: "A is alive, C was alive 4s ago"
```

### Characteristics

* Decentralized — no single point of failure
* Scalable to large clusters
* Eventual detection (not instant)
* Used in: Cassandra, Consul, SWIM protocol

---

## Comparison

| Approach | Central Monitor | Scalability | Detection Speed | Complexity |
| :--- | :--- | :--- | :--- | :--- |
| Push-Based | Yes | Medium | Fast | Low |
| Pull-Based | Yes | Medium | Fast | Low |
| Gossip-Based | No | High | Slower | Medium |

---

# 5. Failure Detection Challenges

---

## 5.1 False Positives

A node is incorrectly declared dead when it is actually alive.

### Causes

* Network congestion (heartbeat delayed but node is healthy)
* GC pauses (Stop-the-World in JVM)
* CPU spikes (heartbeat thread starved)

### Consequences

* Unnecessary failover
* Wasted resources
* Split-brain scenarios

---

## 5.2 False Negatives

A dead node is incorrectly believed to be alive.

### Causes

* Timeout set too high
* Network buffering

### Consequences

* Requests routed to dead node
* Data loss
* Degraded availability

---

## 5.3 The Trade-off

```
Short Timeout → Fast detection ✅ but more false positives ⚠️
Long  Timeout → Fewer false positives ✅ but slow detection ⚠️
```

> Tuning heartbeat interval and timeout is a balancing act between **responsiveness** and **accuracy**.

---

# 6. Advanced Failure Detection

---

## 6.1 Phi Accrual Failure Detector

Instead of a binary alive/dead decision, this approach outputs a **suspicion level** (φ).

```
φ = 0   → no suspicion (healthy)
φ = 1   → mild suspicion
φ = 5   → high suspicion
φ = 10  → almost certainly dead
φ > threshold → declare failure
```

### How It Works

1. Track the **inter-arrival time** of heartbeats
2. Build a statistical distribution (mean, variance)
3. When a heartbeat is late, compute probability of failure
4. Output φ (phi) — a continuous value

### Benefits

* Adapts to network conditions automatically
* Reduces false positives
* Used in: **Akka, Cassandra**

---

## 6.2 SWIM Protocol (Scalable Weakly-consistent Infection-style Membership)

A gossip-based failure detection protocol optimized for large clusters.

### Steps

```
1. Node A picks random Node B
2. A sends "ping" to B
3. If B responds → B is alive ✅
4. If B does not respond:
   a. A asks Nodes C, D to "ping-req" B
   b. If C or D gets a response → B is alive ✅
   c. If no response → B is marked suspect → eventually declared dead
```

### Benefits

* Bounded false positive rate
* Constant message load per node (O(1))
* Scalable to thousands of nodes
* Used in: **HashiCorp Memberlist, Consul, Serf**

---

# 7. Heartbeat in Leader Election

Heartbeats play a critical role in distributed consensus and leader election.

---

## 7.1 Raft Protocol

```
Leader ──► heartbeat ──► Follower A
Leader ──► heartbeat ──► Follower B
Leader ──► heartbeat ──► Follower C
```

* Leader sends periodic heartbeats (AppendEntries with no data)
* If followers don't receive heartbeat within **election timeout** → they start a new election
* Heartbeat interval << election timeout (to avoid unnecessary elections)

---

## 7.2 ZooKeeper (Zab Protocol)

* Clients maintain a **session** with the ZooKeeper server
* Session is kept alive by heartbeats (pings)
* If session heartbeat times out → session is expired → ephemeral nodes are deleted

---

# 8. Heartbeat in Real-World Systems

---

## 8.1 Kubernetes

| Component | Heartbeat Mechanism |
| :--- | :--- |
| **Kubelet → API Server** | Node sends periodic status updates (NodeStatus) |
| **Lease objects** | Lightweight heartbeat every 10s |
| **Liveness Probes** | Kubelet polls container health endpoints |
| **Readiness Probes** | Kubelet checks if container can serve traffic |

### Node Failure Detection

```
Kubelet sends heartbeat → API Server
No heartbeat for 40s → Node marked "NotReady"
After grace period → Pods rescheduled to other nodes
```

---

## 8.2 Apache Cassandra

* Uses **Gossip protocol** for failure detection
* Each node gossips its heartbeat state periodically (every 1s)
* Uses **Phi Accrual Failure Detector** for suspicion levels
* Nodes marked DOWN if φ exceeds threshold

---

## 8.3 Apache ZooKeeper

* Clients send heartbeats (pings) to maintain sessions
* Session timeout is negotiated at connect time
* If heartbeat not received within session timeout → session expired
* Ephemeral znodes tied to that session are removed

---

## 8.4 Amazon ELB / ALB

* Performs periodic **health checks** (HTTP/TCP) against targets
* If a target fails multiple consecutive checks → marked unhealthy
* Traffic is routed only to healthy targets

```
ALB ──► GET /health ──► Target
ALB ◄── 200 OK ◄── Target       → Healthy ✅

ALB ──► GET /health ──► Target
ALB ◄── timeout / 500  ◄── Target → Unhealthy ❌ (after threshold)
```

---

## 8.5 etcd / Consul (Raft-based)

* Leader sends heartbeats to followers
* Heartbeat interval ~ 100–500 ms
* Election timeout ~ 1000–5000 ms
* Missed heartbeats trigger leader re-election

---

## 8.6 HDFS (Hadoop Distributed File System)

```
DataNode ──► heartbeat ──► NameNode (every 3s)
```

* DataNodes report block health and capacity
* If NameNode receives no heartbeat for 10 minutes → DataNode marked dead
* Blocks on dead DataNode are re-replicated to other nodes

---

# 9. Heartbeat vs Health Check

| Aspect | Heartbeat | Health Check |
| :--- | :--- | :--- |
| **Direction** | Node pushes signal | Monitor pulls / probes |
| **Content** | "I'm alive" (minimal) | Detailed status (CPU, memory, dependencies) |
| **Scope** | Liveness only | Liveness + readiness + dependencies |
| **Overhead** | Very low | Moderate |
| **Use case** | Cluster membership, leader election | Load balancer routing, monitoring dashboards |

---

# 10. Design Considerations

---

## 10.1 Choosing Heartbeat Interval

* Too frequent → network overhead, CPU usage
* Too infrequent → slow failure detection

### Guideline

```
Interval = based on acceptable detection latency
Timeout  = 3× to 5× the interval
```

---

## 10.2 Payload — Lightweight vs Rich

### Lightweight (Preferred)

```json
{ "node_id": "A", "timestamp": 1715500000 }
```

### Rich (When Needed)

```json
{
  "node_id": "A",
  "timestamp": 1715500000,
  "cpu": 45,
  "memory": 72,
  "disk": 60,
  "active_connections": 230
}
```

Use lightweight for failure detection, rich payloads for monitoring/autoscaling.

---

## 10.3 Avoiding Split-Brain

When a network partition occurs, both sides may believe the other is dead.

### Mitigation

* Use **quorum-based** failure detection (majority must agree)
* Use **fencing** — revoke access from the old leader
* Require a **minimum cluster size** to operate

---

## 10.4 Jitter

Add randomized jitter to heartbeat intervals to avoid **thundering herd** — all nodes heartbeating at the exact same moment.

```
Actual Interval = Base Interval + random(0, jitter_range)
```

---

# 11. Advantages of Heartbeats

* **Simple** — easy to implement and understand
* **Universal** — works across all distributed architectures
* **Low overhead** — minimal network and CPU cost
* **Enables automation** — triggers failover, rebalancing, alerting
* **Foundation** — underpins leader election, consensus, and cluster management

---

# 12. Disadvantages of Heartbeats

* **False positives** — network delays can mimic node failure
* **Detection delay** — failure is detected only after timeout expires
* **Not diagnostic** — a missed heartbeat doesn't explain *why* the node failed
* **Scalability concerns** — centralized heartbeat collection can become a bottleneck in large clusters
* **Tuning complexity** — choosing the right interval and timeout requires careful analysis

---

# 13. Summary

| Concept | Description |
| :--- | :--- |
| **Heartbeat** | Periodic liveness signal between nodes |
| **Interval** | How often the signal is sent |
| **Timeout** | Duration of silence before declaring failure |
| **Push-Based** | Node sends heartbeat to monitor |
| **Pull-Based** | Monitor polls node for health |
| **Gossip-Based** | Nodes share heartbeat info with random peers |
| **Phi Accrual** | Probabilistic failure suspicion (continuous φ value) |
| **SWIM** | Scalable gossip protocol with indirect probing |
| **False Positive** | Healthy node incorrectly marked dead |
| **Jitter** | Randomized offset to prevent synchronized bursts |

---

# 14. Key Takeaways

* Heartbeats are the foundational failure detection mechanism in distributed systems
* The timeout = interval × missed count trade-off balances speed vs accuracy
* Gossip-based heartbeats scale better than centralized approaches
* Phi Accrual Failure Detector adapts to network conditions and reduces false positives
* Real systems (Kubernetes, Cassandra, ZooKeeper, Raft) all rely on heartbeats
* Always add jitter and use quorum-based decisions to avoid split-brain scenarios