# 📘 HLD Fundamentals — Leader-Follower Pattern (Deep Dive)

---

# 1. Introduction

The **Leader-Follower** (also called **Leader-Replica** or **Master-Slave**) pattern is a distributed systems design where one node is designated as the **leader** (primary) and all other nodes are **followers** (replicas).

The leader handles all **write operations**, while followers replicate the leader's data and can serve **read operations**.

> This pattern provides a clear ownership model for writes, simplifying consistency guarantees while enabling horizontal read scalability.

---

## Core Purpose

* Provide a single source of truth for writes (avoid write conflicts)
* Enable horizontal scaling of read traffic
* Provide fault tolerance through automated failover
* Simplify consistency model (no multi-writer conflicts)

---

# 2. Why Leader-Follower is Needed

In distributed systems, we face fundamental challenges:

* **Concurrent writes** — multiple nodes writing simultaneously can cause conflicts
* **Data consistency** — ensuring all nodes see the same state
* **Availability** — system must remain operational despite failures
* **Read scalability** — single node cannot handle growing read load

The challenge:

> Allowing multiple nodes to accept writes leads to complex conflict resolution (last-write-wins, vector clocks, CRDTs). A leader-based approach eliminates write conflicts entirely.

Leader-Follower solves this by **funneling all writes through a single node** and replicating changes to followers.

---

# 3. How Leader-Follower Works

---

## 3.1 Basic Architecture

```
                    Writes
                      │
                      ▼
               ┌─────────────┐
               │   LEADER    │
               │  (Primary)  │
               └─────┬───────┘
                     │
          Replication Log (WAL / Binlog / Oplog)
                     │
         ┌───────────┼───────────┐
         ▼           ▼           ▼
   ┌──────────┐ ┌──────────┐ ┌──────────┐
   │ Follower │ │ Follower │ │ Follower │
   │    1     │ │    2     │ │    3     │
   └──────────┘ └──────────┘ └──────────┘
         │           │           │
         ▼           ▼           ▼
      Reads       Reads       Reads
```

---

## 3.2 Write Path

1. Client sends write request to the **Leader**
2. Leader writes to its local storage
3. Leader appends the change to its **replication log** (WAL/Binlog/Oplog)
4. Leader sends the log entries to all followers
5. Followers apply the changes to their local storage
6. Acknowledgment sent back (depending on consistency mode)

---

## 3.3 Read Path

**Option A — Read from Leader (strong consistency):**
```
Client → Leader → Response (always up-to-date)
```

**Option B — Read from Follower (eventual consistency, higher throughput):**
```
Client → Follower → Response (may be slightly stale)
```

---

## 3.4 Replication Modes

| Mode | Description | Trade-off |
|------|-------------|-----------|
| **Synchronous** | Leader waits for ALL followers to ACK before confirming write | Strong consistency, higher latency |
| **Semi-synchronous** | Leader waits for at least ONE follower to ACK | Balance of durability and latency |
| **Asynchronous** | Leader confirms immediately, replicates in background | Lowest latency, risk of data loss on leader failure |

---

# 4. Leader Election

---

## 4.1 Why Leader Election is Needed

* Leader can crash, become unresponsive, or get network-partitioned
* System must **automatically** detect failure and elect a new leader
* Must avoid **split-brain** — two nodes both thinking they're the leader

---

## 4.2 Common Leader Election Mechanisms

| Mechanism | How it Works | Used By |
|-----------|-------------|---------|
| **Bully Algorithm** | Highest-ID node becomes leader | Simple systems |
| **Raft Consensus** | Voting among candidates with term numbers | etcd, CockroachDB, TiKV |
| **Paxos** | Proposer/Acceptor/Learner model | Google Spanner, Chubby |
| **ZooKeeper ZAB** | Atomic broadcast with epoch numbers | Kafka, HBase, Solr |
| **Lease-based** | Leader holds a time-limited lease, must renew | HDFS NameNode, DynamoDB |

---

## 4.3 Raft Leader Election (Simplified)

```
State Machine: Follower → Candidate → Leader

1. All nodes start as FOLLOWER
2. If a follower doesn't hear from leader within election timeout:
   → Becomes CANDIDATE
   → Increments term number
   → Votes for itself
   → Requests votes from other nodes
3. If candidate receives majority votes:
   → Becomes LEADER
   → Starts sending heartbeats
4. If another leader is discovered with higher term:
   → Steps down to FOLLOWER
```

---

## 4.4 Split-Brain Problem

```
Network Partition:
┌─────────────────┐     ┌─────────────────┐
│  Partition A     │     │  Partition B     │
│  [Leader]        │  ✕  │  [New Leader?]   │
│  [Follower 1]   │     │  [Follower 2]    │
│  [Follower 2]   │     │  [Follower 3]    │
└─────────────────┘     └─────────────────┘
```

**Solutions:**
* **Quorum-based election** — need majority to elect leader (prevents minority partition from electing)
* **Fencing tokens** — monotonically increasing tokens; old leader's writes rejected
* **Lease expiry** — old leader's lease expires, it stops accepting writes

---

# 5. Replication Strategies

---

## 5.1 Statement-Based Replication

* Leader logs every write statement (e.g., SQL INSERT)
* Followers re-execute the same statements
* **Problem:** Non-deterministic functions (NOW(), RAND()) produce different results

---

## 5.2 Write-Ahead Log (WAL) Shipping

* Leader ships its physical WAL (byte-level changes)
* Followers apply WAL entries directly
* **Advantage:** Exact byte-for-byte replication
* **Disadvantage:** Tied to storage engine version
* **Used by:** PostgreSQL

---

## 5.3 Logical (Row-Based) Replication

* Leader logs logical changes (insert row X with values Y)
* Followers apply logical changes independently
* **Advantage:** Version-independent, cross-platform
* **Used by:** MySQL binlog (row format), Debezium CDC

---

## 5.4 Trigger-Based Replication

* Application-level triggers fire on changes
* Custom logic captures and forwards changes
* **Advantage:** Flexible, selective replication
* **Disadvantage:** Higher overhead, more complex

---

# 6. Consistency Guarantees

---

## 6.1 Read-After-Write Consistency

Guarantee that a user who wrote data will immediately see their own write.

```
User writes → Leader
User reads  → Must hit Leader OR a follower that has replicated the write
```

**Techniques:**
* Read from leader for recently written data (e.g., within last 10 seconds)
* Track replication lag; route to caught-up followers only
* Client remembers write timestamp; reject reads from followers behind that timestamp

---

## 6.2 Monotonic Reads

Guarantee that a user won't see data go "backward in time."

```
Read 1 → Follower A (sees version 5)
Read 2 → Follower B (sees version 3)  ❌ Violation!
```

**Solution:** Pin user's reads to the same follower (session affinity).

---

## 6.3 Consistent Prefix Reads

Guarantee that causally related writes are seen in order.

```
Write 1: "What's the score?" 
Write 2: "It's 2-1"

Reader must not see Write 2 before Write 1
```

**Solution:** Ensure causally related writes go to the same partition.

---

# 7. Replication Lag

---

## 7.1 What is Replication Lag?

The time difference between a write being committed on the leader and that write being available on a follower.

```
Leader:     Write at T=0
Follower 1: Applies at T=50ms  (lag = 50ms)
Follower 2: Applies at T=200ms (lag = 200ms)
Follower 3: Applies at T=2s    (lag = 2s — degraded)
```

---

## 7.2 Causes of Replication Lag

| Cause | Impact |
|-------|--------|
| Network latency | Delayed log delivery |
| Follower under heavy read load | Cannot keep up with applying writes |
| Large transactions | Single large write blocks replication |
| Geographic distance | Cross-region replication inherently slower |
| Follower recovery | Catching up after restart |

---

## 7.3 Mitigating Replication Lag

* **Monitor lag** — expose metrics, alert on thresholds
* **Adaptive routing** — route reads to least-lagged follower
* **Read-your-writes** — route user's reads to leader after their writes
* **Semi-synchronous replication** — at least one follower is always caught up
* **Parallel apply** — followers apply changes in parallel (MySQL 5.7+)

---

# 8. Failover Process

---

## 8.1 Steps in Automatic Failover

```
1. DETECT    → Leader missed heartbeats / lease expired
2. ELECT     → Choose new leader (most up-to-date follower)
3. RECONFIGURE → Update routing; followers point to new leader
4. FENCE     → Prevent old leader from accepting writes (fencing)
5. CATCH UP  → New leader applies any uncommitted entries
```

---

## 8.2 Failover Challenges

| Challenge | Description |
|-----------|-------------|
| **Data loss** | Async replication → unreplicated writes on old leader are lost |
| **Split-brain** | Two leaders accepting writes simultaneously |
| **Cascading failures** | Failover triggers load spike on remaining nodes |
| **Client reconnection** | Clients must discover and connect to new leader |
| **Conflict on old leader rejoin** | Old leader has writes not on new leader |

---

## 8.3 Handling Unreplicated Writes

When the old leader comes back, it may have writes that never reached the new leader:

* **Discard** — drop unreplicated writes (data loss, simplest)
* **Manual reconciliation** — operator reviews and merges
* **Conflict queue** — store conflicting writes for application-level resolution

---

# 9. Variants of Leader-Follower

---

## 9.1 Single Leader

```
1 Leader + N Followers
All writes → Leader only
```
* Simplest model
* No write conflicts
* Used by: PostgreSQL, MySQL, MongoDB (replica sets), Redis Sentinel

---

## 9.2 Multi-Leader (Leader-Leader)

```
Multiple Leaders, each accepts writes
Leaders replicate to each other
```
* Needed for multi-datacenter setups
* Write conflicts must be resolved
* Used by: MySQL Group Replication, CockroachDB, Cassandra (coordinator per key)

---

## 9.3 Leaderless (Quorum-Based)

```
No designated leader
Any node accepts writes
Quorum (W + R > N) ensures consistency
```
* No single point of failure
* Requires conflict resolution
* Used by: Cassandra, DynamoDB, Riak

---

## 9.4 Chain Replication

```
Client → Leader → Follower1 → Follower2 → ... → Tail
Reads served from Tail (strongly consistent)
```
* High throughput
* Strong consistency without reading from leader
* Used by: Microsoft Azure Storage, HDFS (pipeline replication)

---

# 10. Real-World Implementations

---

## 10.1 PostgreSQL Streaming Replication

* Leader streams WAL to followers over TCP
* Supports synchronous and asynchronous modes
* `pg_stat_replication` shows replication lag
* Followers can serve read queries (hot standby)

---

## 10.2 MySQL Replication

* **Binlog** — leader writes binary log of changes
* **Row-based** or **statement-based** replication
* **GTID** (Global Transaction ID) for consistent failover
* Semi-synchronous mode: `rpl_semi_sync_master_wait_for_slave_count`

---

## 10.3 MongoDB Replica Sets

* One **Primary** (leader) + multiple **Secondaries** (followers)
* **Oplog** — operations log replicated to secondaries
* Automatic failover via Raft-like election
* Read preference: primary, primaryPreferred, secondary, nearest
* Write concern: `w: majority` for durability

---

## 10.4 Apache Kafka

* Each partition has one **Leader Broker**
* Followers are **In-Sync Replicas (ISR)**
* Producers write to partition leader
* Consumers can read from leader or followers (KIP-392)
* `acks=all` → waits for all ISR to acknowledge
* Controller handles leader election on broker failure

---

## 10.5 Redis Sentinel

* One master + multiple replicas
* Sentinel processes monitor master health
* On failure: Sentinels vote to elect new master
* Async replication → possible data loss on failover
* `WAIT` command for semi-synchronous behavior

---

## 10.6 etcd / Raft

* Strongly consistent key-value store
* Raft consensus for leader election and log replication
* Reads can be linearizable (via leader) or serializable (via followers)
* Used as coordination service for Kubernetes

---

# 11. Leader-Follower vs Other Patterns

| Feature | Leader-Follower | Multi-Leader | Leaderless |
|---------|----------------|--------------|------------|
| Write throughput | Limited (single leader) | Higher | Highest |
| Write conflicts | None | Must resolve | Must resolve |
| Consistency | Strong (if reading from leader) | Eventual | Eventual (tunable) |
| Failover complexity | Moderate | Low | None (no leader) |
| Read scalability | High (add followers) | High | High |
| Latency (geo-distributed) | High (writes route to leader) | Low | Low |
| Complexity | Low | High | Moderate |

---

# 12. When to Use Leader-Follower

---

## ✅ Use When:

* Strong consistency for writes is required
* Read-heavy workload (read:write ratio > 10:1)
* You want simple conflict-free write semantics
* Single-region deployments (low write latency to leader)
* Need clear data ownership and audit trail

---

## ❌ Avoid When:

* Write-heavy workloads (leader becomes bottleneck)
* Multi-region with low-latency write requirements
* Zero-downtime failover is critical (failover has a gap)
* System cannot tolerate any data loss (async replication risk)

---

# 13. Performance Considerations

| Factor | Impact |
|--------|--------|
| Number of followers | More followers = more replication overhead on leader |
| Sync vs Async | Sync increases write latency; Async risks data loss |
| Read routing | Smart routing reduces load on leader |
| Connection pooling | Reduces overhead of follower connections |
| Parallel replication | Followers apply changes faster with multi-threaded apply |
| Network bandwidth | Replication log shipping consumes bandwidth |

---

# 14. Best Practices

1. **Monitor replication lag** — set alerts for lag exceeding acceptable thresholds
2. **Use semi-synchronous replication** — balance between durability and performance
3. **Implement proper fencing** — prevent split-brain with fencing tokens or leases
4. **Test failover regularly** — chaos engineering to validate failover works
5. **Use read replicas wisely** — route time-insensitive queries to followers
6. **Configure appropriate timeouts** — too short = false failovers; too long = extended downtime
7. **Handle stale reads explicitly** — design application logic to tolerate or detect staleness
8. **Plan for leader capacity** — leader handles ALL writes + replication overhead
9. **Use connection routing layers** — ProxySQL, PgBouncer, or service discovery for transparent failover
10. **Keep replication log retention adequate** — so recovered followers can catch up without full resync

---

# 15. Interview Key Points

* Leader-Follower eliminates **write conflicts** by funneling all writes through one node
* **Replication can be sync, semi-sync, or async** — each with different consistency/latency trade-offs
* **Leader election** uses consensus (Raft/Paxos) or coordination services (ZooKeeper)
* **Split-brain** is the #1 risk — solved by quorum-based election and fencing
* **Replication lag** causes stale reads — mitigated by read-your-writes and monotonic reads
* **Failover** involves detection → election → reconfiguration → fencing
* **Chain replication** is a variant that provides strong consistency for reads from tail
* Trade-off: **consistency and simplicity** vs **write throughput and geo-latency**

---

# 16. Summary Diagram — Complete Leader-Follower Lifecycle

```
┌─────────────────────────────────────────────────────────┐
│                    NORMAL OPERATION                       │
│                                                          │
│   Client ──write──► Leader ──replicate──► Followers      │
│   Client ──read───► Leader OR Followers                  │
│                                                          │
└─────────────────────────────────────────────────────────┘
                          │
                    Leader Fails
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    FAILOVER                               │
│                                                          │
│   1. Followers detect missing heartbeats                 │
│   2. Election triggered (Raft/ZAB/Lease)                 │
│   3. Most up-to-date follower elected as new leader      │
│   4. Old leader fenced (epoch/term incremented)          │
│   5. Clients redirected to new leader                    │
│   6. Old leader rejoins as follower (if recovered)       │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

---

> **Key Takeaway:** The Leader-Follower pattern is the backbone of most production databases and distributed systems. It trades write scalability for simplicity and strong consistency. Understanding its replication modes, failover mechanisms, and consistency guarantees is essential for designing reliable distributed architectures.