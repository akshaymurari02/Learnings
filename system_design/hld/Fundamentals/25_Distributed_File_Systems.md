# 📘 HLD Fundamentals — Distributed File Systems (Deep Dive)

---

# 1. Introduction

A **Distributed File System (DFS)** stores files across multiple machines (nodes), presenting them as a single unified filesystem to clients.

Unlike local file systems (ext4, NTFS) limited to a single disk, a DFS spans hundreds or thousands of machines, providing scalability, fault tolerance, and high throughput.

> In system design, distributed file systems are the foundation for big data processing, object storage, media serving, and any application that needs to store and retrieve massive amounts of unstructured data.

---

## Core Purpose

* **Scale beyond single machine** — store petabytes of data across commodity hardware
* **Fault tolerance** — data survives hardware failures through replication
* **High throughput** — parallel reads/writes across many nodes
* **Data locality** — process data where it's stored (move computation to data)
* **Transparency** — clients interact as if it's a single filesystem

---

# 2. Why Distributed File Systems are Needed

| Challenge | How DFS Solves It |
|-----------|-------------------|
| Single disk capacity limits | Spread data across thousands of disks |
| Single-machine failure = data loss | Replicate data to 3+ nodes |
| Single-machine I/O bottleneck | Parallel reads from multiple nodes |
| Geography (multi-region access) | Geo-replicated data near users |
| Big data processing | Collocate compute with data (MapReduce, Spark) |

---

# 3. Architecture Overview

---

## 3.1 General DFS Architecture

```
┌───────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                   │
│  │ Client 1 │  │ Client 2 │  │ Client 3 │                   │
│  └─────┬────┘  └─────┬────┘  └─────┬────┘                   │
└────────┼──────────────┼──────────────┼───────────────────────┘
         │              │              │
         ▼              ▼              ▼
┌───────────────────────────────────────────────────────────────┐
│                     METADATA LAYER                             │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Name Node / Master Server                  │  │
│  │  • File → Block mapping                                │  │
│  │  • Block → DataNode location mapping                   │  │
│  │  • Namespace (directory tree)                          │  │
│  │  • Replication management                              │  │
│  └────────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────┘
         │              │              │
         ▼              ▼              ▼
┌───────────────────────────────────────────────────────────────┐
│                       DATA LAYER                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │DataNode 1│  │DataNode 2│  │DataNode 3│  │DataNode N│    │
│  │ [B1][B4] │  │ [B1][B2] │  │ [B2][B3] │  │ [B3][B4] │    │
│  │ [B5]     │  │ [B5][B6] │  │ [B4][B6] │  │ [B5][B6] │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
│                                                               │
│  B = Block (64MB - 256MB chunks of a file)                   │
│  Each block replicated to 3 nodes (default)                  │
└───────────────────────────────────────────────────────────────┘
```

---

## 3.2 Master-Worker Pattern (Most Common)

| Component | Responsibility |
|-----------|---------------|
| **Master / NameNode** | Metadata management, namespace, block location tracking |
| **Workers / DataNodes** | Store actual data blocks, serve read/write requests |
| **Client** | Interacts with master for metadata, directly with workers for data |

---

# 4. Key Components

---

## 4.1 Metadata Server (NameNode / Master)

The **brain** of the DFS — manages all metadata without storing actual data.

| Responsibility | Description |
|----------------|-------------|
| Namespace management | Directory tree (paths, permissions, ownership) |
| File → Block mapping | Which blocks make up a file, in what order |
| Block → Node mapping | Which DataNodes hold each block replica |
| Replication monitoring | Ensure each block has N replicas; trigger re-replication if under-replicated |
| Heartbeat tracking | Monitor DataNode liveness |
| Block placement policy | Decide where to place new blocks (rack-awareness) |

```
Metadata Example:
/data/logs/app.log
  → Block 1: [DataNode 2, DataNode 5, DataNode 8]
  → Block 2: [DataNode 1, DataNode 3, DataNode 7]
  → Block 3: [DataNode 4, DataNode 6, DataNode 9]
```

**Challenge:** Master is a single point of failure (SPOF)

**Solutions:**
* Standby NameNode with shared edit log (HDFS HA)
* Raft-based replicated metadata (TiFS, JuiceFS)
* Distributed metadata (Ceph MDS cluster)

---

## 4.2 Data Nodes (Chunk Servers / Storage Nodes)

The **muscles** of the DFS — store and serve actual data blocks.

| Responsibility | Description |
|----------------|-------------|
| Store blocks | Write data blocks to local disk |
| Serve reads | Stream requested blocks to clients |
| Checksum verification | Validate data integrity on read/write |
| Heartbeat | Send periodic liveness signal to master |
| Block reports | Periodically report list of stored blocks to master |
| Block replication | Participate in replication pipeline |

```
DataNode Local Storage:
/data/
  ├── blk_001.data      (actual data)
  ├── blk_001.meta      (checksum + generation stamp)
  ├── blk_002.data
  ├── blk_002.meta
  └── ...
```

---

## 4.3 Blocks / Chunks

Files are split into fixed-size **blocks** (also called chunks).

| Property | Typical Value |
|----------|---------------|
| Block size | 64 MB (GFS), 128 MB (HDFS default), 256 MB |
| Replication factor | 3 (default) |
| Naming | Unique block ID + generation stamp |

**Why large blocks?**
* Amortize metadata overhead (fewer blocks per file)
* Reduce NameNode memory usage
* Sequential I/O optimization (large streaming reads)
* Fewer seeks (one seek per block, not per record)

```
File: video.mp4 (512 MB)
  → Block 0: bytes 0 - 128MB
  → Block 1: bytes 128MB - 256MB
  → Block 2: bytes 256MB - 384MB
  → Block 3: bytes 384MB - 512MB

Each block stored on 3 different DataNodes
```

---

## 4.4 Client Library

The client handles communication with both metadata and data layers.

```
Write Flow:
  1. Client → NameNode: "I want to create /data/file.csv"
  2. NameNode → Client: "Write Block 1 to DataNodes [DN3, DN7, DN9]"
  3. Client → DN3 → DN7 → DN9 (pipeline replication)
  4. ACK propagates back: DN9 → DN7 → DN3 → Client
  5. Client → NameNode: "Block 1 write complete"
  6. Repeat for remaining blocks

Read Flow:
  1. Client → NameNode: "Where are blocks of /data/file.csv?"
  2. NameNode → Client: "Block 1 on [DN3, DN7, DN9], Block 2 on [DN1, DN5, DN8]..."
  3. Client → Nearest DataNode: "Give me Block 1" (locality-aware)
  4. Client assembles file from blocks
```

---

## 4.5 Replication

Replication ensures data survives node failures.

```
Replication Factor = 3

Block Placement Policy (Rack-Aware):
  Replica 1: Same rack as writer (lowest latency)
  Replica 2: Different rack (survives rack failure)
  Replica 3: Same rack as Replica 2, different node

┌─────────────────┐     ┌─────────────────┐
│     Rack 1       │     │     Rack 2       │
│ ┌─────┐ ┌─────┐ │     │ ┌─────┐ ┌─────┐ │
│ │DN 1 │ │DN 2 │ │     │ │DN 3 │ │DN 4 │ │
│ │[B1] │ │     │ │     │ │[B1] │ │[B1] │ │
│ └─────┘ └─────┘ │     │ └─────┘ └─────┘ │
└─────────────────┘     └─────────────────┘

Rack 1 power failure → Block B1 still available from Rack 2
```

---

## 4.6 Namespace & Directory Tree

```
/
├── user/
│   ├── alice/
│   │   ├── documents/
│   │   │   └── report.pdf  (3 blocks)
│   │   └── photos/
│   │       └── vacation.jpg (1 block)
│   └── bob/
│       └── data.csv (5 blocks)
├── system/
│   └── logs/
│       └── app.log (100 blocks)
└── tmp/
    └── scratch.dat (2 blocks)
```

Metadata stored in-memory on NameNode for fast lookups:
* Inode table (file attributes, permissions)
* Block map (file → ordered list of block IDs)
* Block locations (block ID → list of DataNodes)

---

# 5. Read & Write Paths (Detailed)

---

## 5.1 Write Path

```
┌────────┐                  ┌───────────┐
│ Client │                  │ NameNode  │
└───┬────┘                  └─────┬─────┘
    │  1. Create file request      │
    │─────────────────────────────►│
    │                              │
    │  2. Returns: block locations │
    │◄─────────────────────────────│
    │                              │
    │  Assigned: DN1, DN5, DN8     │
    │                              │
    │  3. Pipeline write           │
    ▼                              │
┌──────┐    ┌──────┐    ┌──────┐  │
│ DN1  │───►│ DN5  │───►│ DN8  │  │
└──┬───┘    └──┬───┘    └──┬───┘  │
   │           │           │      │
   │◄──────────│◄──────────│      │
   │      ACK pipeline             │
   │                              │
   │  4. ACK to client            │
   │◄─────────────────────────────│
   │                              │
   │  5. Report block complete    │
   │─────────────────────────────►│
```

**Pipeline Replication:**
* Client sends data to first DataNode
* First DataNode simultaneously writes locally AND forwards to second
* Second DataNode writes locally AND forwards to third
* ACK travels back through the pipeline
* Maximizes network utilization

---

## 5.2 Read Path

```
┌────────┐                  ┌───────────┐
│ Client │                  │ NameNode  │
└───┬────┘                  └─────┬─────┘
    │  1. Open file /data/x.csv   │
    │─────────────────────────────►│
    │                              │
    │  2. Block locations:         │
    │     B0: [DN2, DN5, DN9]      │
    │     B1: [DN1, DN4, DN7]      │
    │◄─────────────────────────────│
    │                              │
    │  3. Read B0 from nearest DN  │
    │──────────────────────────────────► DN2 (same rack)
    │◄─────────── Block 0 data ──────── DN2
    │                              │
    │  4. Read B1 from nearest DN  │
    │──────────────────────────────────► DN4 (closest)
    │◄─────────── Block 1 data ──────── DN4
    │                              │
    │  5. Assemble file locally    │
```

**Locality-aware reading:** Client reads from the DataNode that is physically closest (same rack > same datacenter > cross-datacenter).

---

## 5.3 Delete Path

```
1. Client → NameNode: "Delete /data/old.csv"
2. NameNode: Moves file to trash (soft delete, configurable retention)
3. After retention period: NameNode removes metadata
4. NameNode notifies DataNodes to delete blocks (lazy/async)
5. DataNodes free disk space
```

---

# 6. Fault Tolerance & Recovery

---

## 6.1 DataNode Failure

```
Scenario: DN5 crashes (held blocks B1, B3, B7)

1. NameNode detects missing heartbeat (timeout: 10 min default)
2. NameNode marks DN5 as dead
3. Blocks B1, B3, B7 now under-replicated (only 2 replicas instead of 3)
4. NameNode schedules re-replication:
   - B1: copy from DN2 → DN6
   - B3: copy from DN8 → DN1
   - B7: copy from DN4 → DN9
5. Replication factor restored
```

---

## 6.2 NameNode Failure (SPOF Problem)

| Solution | Description |
|----------|-------------|
| **Active-Standby** | Standby NameNode replays edit log; takes over on failure |
| **Shared Edit Log** | Write edits to shared storage (NFS, QJM) for standby |
| **Quorum Journal Manager** | Edits written to majority of journal nodes (HDFS HA) |
| **Distributed Metadata** | No single master; metadata spread across cluster (Ceph) |
| **Checkpointing** | Secondary NameNode periodically merges edits into fsimage |

```
HDFS HA Architecture:
┌──────────────┐     Shared Edit Log     ┌──────────────┐
│Active NameNode│◄──────────────────────►│Standby NameNode│
└──────┬───────┘    (Quorum Journal)     └──────┬───────┘
       │                                        │
       │  ZooKeeper (leader election)           │
       │◄──────────────────────────────────────►│
       │                                        │
       ▼                                        ▼
┌──────────────────────────────────────────────────────┐
│                     DataNodes                         │
│  (send heartbeats and block reports to BOTH)         │
└──────────────────────────────────────────────────────┘
```

---

## 6.3 Data Corruption

* **Checksum per block** — CRC-32C computed on write, verified on read
* **Background block scanner** — DataNodes periodically scan stored blocks
* **Corrupt block detected** → report to NameNode → re-replicate from healthy replica
* **End-to-end checksums** — client validates checksum after reading

---

## 6.4 Network Partitions

* NameNode can't reach DataNodes → marks them dead → triggers re-replication
* Risk: **over-replication** if partition heals (too many replicas)
* Solution: Decommission excess replicas after partition heals

---

# 7. Consistency Models

| Model | Description | Used By |
|-------|-------------|---------|
| **Write-once, read-many** | Files immutable after close; only append | HDFS (original) |
| **Strong consistency** | All reads see latest write | GFS (single master enforces) |
| **Eventual consistency** | Replicas converge over time | S3 (historically), Ceph (configurable) |
| **Sequential consistency** | Operations appear in some total order | GFS lease-based |

---

## HDFS Consistency

```
Write:
  - File visible in namespace after create()
  - Data NOT visible to readers until:
    1. hflush() → data visible but not durable
    2. hsync() → data durable on DataNode disk
    3. close() → all blocks finalized and visible

Read:
  - Always reads committed (closed) blocks
  - Cannot read currently-being-written block (unless using hflush)
```

---

# 8. HDFS (Hadoop Distributed File System)

---

## 8.1 Overview

* Open-source implementation inspired by Google File System (GFS)
* Designed for batch processing (large sequential reads/writes)
* Part of the Hadoop ecosystem (MapReduce, Spark, Hive, HBase)

---

## 8.2 Key Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| Block size | 128 MB | `dfs.blocksize` |
| Replication factor | 3 | `dfs.replication` |
| Heartbeat interval | 3 sec | DataNode → NameNode heartbeat |
| Dead node threshold | 10 min 30 sec | Time before node marked dead |
| Rack awareness | On | Placement policy considers rack topology |

---

## 8.3 HDFS Optimizations

| Optimization | Description |
|--------------|-------------|
| **Data locality** | Schedule computation on node holding the data (MapReduce) |
| **Short-circuit reads** | Client reads local blocks directly (no network) |
| **Block caching** | Frequently accessed blocks cached in DataNode memory |
| **Erasure coding** | EC (Reed-Solomon) instead of replication for cold data (1.5x overhead vs 3x) |
| **Heterogeneous storage** | Hot (SSD) → Warm (HDD) → Cold (Archive) tiering |
| **Federation** | Multiple NameNodes each managing a namespace volume |

---

## 8.4 HDFS Federation

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│NameNode 1    │  │NameNode 2    │  │NameNode 3    │
│ /user/       │  │ /data/       │  │ /system/     │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                  │
       ▼                 ▼                  ▼
┌──────────────────────────────────────────────────────┐
│              Shared DataNode Pool                     │
│  (All DataNodes store blocks for all NameNodes)      │
└──────────────────────────────────────────────────────┘

Benefits:
  - Scales namespace (more memory for metadata)
  - Isolates workloads (different NameNodes for different teams)
  - No single NameNode memory bottleneck
```

---

## 8.5 HDFS Erasure Coding

```
Traditional Replication (3x overhead):
  Data: [Block]  → Storage: [Block][Block][Block] = 3x storage

Erasure Coding (1.5x overhead for RS-6-3):
  Data: [D1][D2][D3][D4][D5][D6] → Parity: [P1][P2][P3]
  Total: 9 blocks, can tolerate any 3 failures
  Overhead: 9/6 = 1.5x (vs 3x for replication)

Trade-off:
  + 50% less storage
  - Higher CPU (encoding/decoding)
  - Higher network for recovery (read 6 blocks to reconstruct 1)
  Best for: cold/archival data (rarely read)
```

---

# 9. Google File System (GFS)

---

## 9.1 Overview

* Google's internal DFS (2003 paper) — inspired HDFS
* Designed for: large files, append-heavy, sequential reads
* Single master, multiple chunkservers

---

## 9.2 Key Design Choices

| Choice | Rationale |
|--------|-----------|
| Large chunk size (64 MB) | Reduce master metadata; optimize for streaming |
| Single master | Simplifies consistency; not a bottleneck for data (only metadata) |
| Append-heavy | Workloads produce data continuously; rarely modify existing |
| Relaxed consistency | Trade consistency for performance in append operations |
| Co-located compute | Process data where it's stored |

---

## 9.3 GFS Lease Mechanism

```
For concurrent appends:
1. Master grants LEASE to one chunkserver (primary) for a chunk
2. Primary assigns serial numbers to all mutations
3. Primary tells secondaries the order to apply mutations
4. All replicas apply mutations in same order

Lease duration: 60 seconds, renewable
If primary fails → lease expires → master grants new lease to another replica
```

---

## 9.4 GFS → Colossus

Google evolved GFS into **Colossus** (GFS v2):
* Distributed master (no SPOF)
* Smaller chunk sizes (1 MB) for lower latency
* Erasure coding for efficiency
* Reed-Solomon instead of triple replication
* Underlies Google Cloud Storage, BigTable, Spanner

---

# 10. Other Distributed File Systems

---

## 10.1 Ceph (RADOS)

```
┌────────────────────────────────────────────────┐
│                    CEPH                         │
│                                                │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │CephFS   │  │ RBD     │  │ RGW     │       │
│  │(POSIX)  │  │(Block)  │  │(Object/ │       │
│  │         │  │         │  │ S3 API) │       │
│  └────┬────┘  └────┬────┘  └────┬────┘       │
│       │            │            │              │
│       ▼            ▼            ▼              │
│  ┌─────────────────────────────────────────┐  │
│  │               RADOS                      │  │
│  │  (Reliable Autonomic Distributed         │  │
│  │   Object Store)                          │  │
│  └───────────────────┬─────────────────────┘  │
│                      │                         │
│  ┌────────┐  ┌────────┐  ┌────────┐          │
│  │ OSD 1  │  │ OSD 2  │  │ OSD N  │          │
│  └────────┘  └────────┘  └────────┘          │
│                                                │
│  Monitors (MON): Cluster state, consensus     │
│  MDS: Metadata servers for CephFS             │
│  No single master — uses CRUSH algorithm      │
└────────────────────────────────────────────────┘
```

**Key differentiator:** No central metadata server for data placement. Uses **CRUSH algorithm** (deterministic hash) to compute where data lives.

| Feature | HDFS | Ceph |
|---------|------|------|
| Metadata | Centralized (NameNode) | Distributed (CRUSH + MON) |
| Interface | File only | File + Block + Object |
| POSIX compliance | No | Yes (CephFS) |
| Scaling metadata | Federation | Distributed MDS |
| Best for | Big data / Hadoop | Cloud storage, VMs, containers |

---

## 10.2 Amazon S3 (Object Storage)

Not a traditional file system but a distributed **object store**:

```
PUT /bucket/key → Object stored across multiple AZs
GET /bucket/key → Object retrieved

Internally:
  - Objects split into chunks
  - Replicated across 3+ Availability Zones
  - Metadata in separate highly-available index
  - 11 nines (99.999999999%) durability
```

| Feature | Description |
|---------|-------------|
| Interface | HTTP REST API (not POSIX) |
| Namespace | Flat (bucket + key), no true directories |
| Consistency | Strong read-after-write (since Dec 2020) |
| Durability | 99.999999999% (11 nines) |
| Storage classes | Standard, IA, Glacier, Deep Archive |

---

## 10.3 MinIO

* Open-source, S3-compatible object storage
* Designed for high-performance, cloud-native deployments
* Erasure coding for data protection
* Kubernetes-native
* Often used as self-hosted S3 alternative

---

## 10.4 GlusterFS

* Open-source distributed file system
* No metadata server (elastic hashing for placement)
* POSIX-compliant
* Scale-out NAS replacement
* Good for: media streaming, cloud storage, VMs

---

## 10.5 Comparison Table

| System | Architecture | Interface | Best For | Consistency |
|--------|-------------|-----------|----------|-------------|
| **HDFS** | Master-Worker | File API | Big data / batch | Write-once |
| **GFS/Colossus** | Master-Worker | Internal | Google infrastructure | Relaxed (leases) |
| **Ceph** | Distributed (CRUSH) | File + Block + Object | Cloud, VMs, containers | Strong (configurable) |
| **S3** | Multi-AZ Object Store | REST API | Cloud object storage | Strong |
| **GlusterFS** | Distributed hash | POSIX File | NAS replacement | Eventual/Strong |
| **MinIO** | Distributed | S3 API | Self-hosted object store | Strong |

---

# 11. Key Design Decisions in DFS

---

## 11.1 Block Size

| Small Blocks (4-16 KB) | Large Blocks (64-256 MB) |
|------------------------|-------------------------|
| Less internal waste | Less metadata overhead |
| Better for small files | Optimized for streaming I/O |
| More NameNode memory | Fewer seeks per file |
| More network RPCs | Worse for small files (space waste) |

---

## 11.2 Replication vs Erasure Coding

| Factor | Replication (3x) | Erasure Coding (1.5x) |
|--------|-----------------|----------------------|
| Storage overhead | 200% (3 copies) | 50% (RS-6-3) |
| Read performance | Fast (read any replica) | Slower (reconstruct from K blocks) |
| Write performance | Pipeline write | Encode + distribute |
| Recovery speed | Fast (copy one block) | Slow (read K blocks, decode) |
| CPU usage | Low | Higher (encode/decode) |
| Best for | Hot data (frequently accessed) | Cold data (archival) |

---

## 11.3 Centralized vs Distributed Metadata

| Centralized (HDFS) | Distributed (Ceph CRUSH) |
|--------------------|-----------------------|
| Simple consistency | No SPOF for metadata |
| Single point of failure | More complex consistency |
| Memory-bound (all metadata in RAM) | Scales linearly |
| Fast lookups (in-memory) | Compute-based lookups |
| Needs HA solution (standby) | Built-in fault tolerance |

---

## 11.4 Consistency vs Performance

```
Strong Consistency:
  + Always read latest data
  - Higher latency (wait for replication)
  - Lower throughput

Relaxed/Eventual Consistency:
  + Lower latency
  + Higher throughput
  - Stale reads possible
  - Requires application-level handling
```

---

# 12. Performance Optimization Techniques

| Technique | Description |
|-----------|-------------|
| **Data locality** | Process data on the node storing it; avoid network transfer |
| **Short-circuit reads** | Client reads from local disk directly if data is on same node |
| **Caching (block cache)** | DataNodes cache frequently accessed blocks in memory |
| **Prefetching** | Read ahead for sequential access patterns |
| **Batched operations** | Group multiple small file operations |
| **Compression** | Compress blocks (Snappy, LZ4, Zstd) to reduce I/O |
| **Sequential I/O** | Append-only design maximizes disk throughput |
| **Parallel reads** | Client reads different blocks from different nodes simultaneously |
| **Tiered storage** | SSD for hot data, HDD for warm, archive for cold |
| **Erasure coding** | Replace replication for cold data to save space |

---

# 13. Small File Problem

Most DFS are optimized for **large files**. Small files cause:

| Problem | Impact |
|---------|--------|
| Metadata bloat | 1 million 1KB files = 1 million block entries in NameNode memory |
| Seek-dominated I/O | Each small file requires separate seek |
| Under-utilization | 128MB block used for 1KB file = wasted space |

**Solutions:**

| Solution | Description |
|----------|-------------|
| **HAR (Hadoop Archive)** | Pack small files into a single archive |
| **SequenceFile** | Key-value flat file (filename → content) |
| **CombineFileInputFormat** | Merge small files for MapReduce processing |
| **HBase** | Store small objects in a distributed table |
| **Ozone** | Hadoop-native object store for small files |

---

# 14. Security in Distributed File Systems

| Layer | Mechanism |
|-------|-----------|
| Authentication | Kerberos (HDFS), tokens, mTLS (Ceph) |
| Authorization | POSIX permissions, ACLs, Ranger policies |
| Encryption in transit | TLS/SASL between nodes |
| Encryption at rest | AES-256 transparent data encryption (TDE) |
| Audit | Access logging for compliance (who accessed what, when) |
| Network isolation | Separate data and management networks |

---

# 15. DFS in System Design Interviews

---

## When to Use a DFS

* Store and process **large unstructured data** (logs, media, backups)
* Backend for **data lake / big data analytics** (Spark, Hive)
* **Object storage** layer for cloud applications
* **Media storage** (videos, images) with high-throughput streaming
* **Archival / backup** with high durability requirements

---

## When NOT to Use

* Low-latency random access (use a database instead)
* Small transactional data (use RDBMS)
* Real-time key-value lookups (use Redis, DynamoDB)
* POSIX-style file operations with strict semantics (use NFS or Ceph)

---

# 16. Interview Key Points

* **Block-based storage** — files split into large blocks (64-256 MB), each replicated
* **Master-worker** — metadata server (NameNode) + data servers (DataNodes); client talks to both
* **Replication** — 3x default, rack-aware placement for fault tolerance
* **Pipeline write** — client → DN1 → DN2 → DN3 (efficient network use)
* **NameNode is SPOF** — solved with HA (standby + shared edit log / QJM)
* **Consistency** — HDFS: write-once-read-many; close() makes data visible
* **Data locality** — key performance optimization; bring compute to data
* **Erasure coding** — 1.5x overhead vs 3x replication; for cold data
* **Small file problem** — DFS not ideal for millions of tiny files; use archives or object stores
* **HDFS vs S3** — HDFS for Hadoop ecosystem / batch; S3 for cloud-native / REST API
* **Ceph** — no single master, CRUSH algorithm, supports file + block + object

---

# 17. Summary Diagram — Complete DFS Request Lifecycle

```
┌─────────────────── WRITE ───────────────────────────────────┐
│                                                              │
│  Client                                                      │
│    │ 1. createFile(/data/x.csv)                              │
│    ▼                                                         │
│  NameNode                                                    │
│    │ 2. Allocate blocks, choose DataNodes (rack-aware)       │
│    │    Returns: B1→[DN2,DN5,DN8], B2→[DN1,DN4,DN7]         │
│    ▼                                                         │
│  Client → DN2 → DN5 → DN8 (pipeline write Block 1)          │
│         ← ACK ← ACK ← ACK                                   │
│  Client → DN1 → DN4 → DN7 (pipeline write Block 2)          │
│         ← ACK ← ACK ← ACK                                   │
│    │                                                         │
│    │ 3. close() → NameNode finalizes file                    │
│                                                              │
└──────────────────────────────────────────────────────────────┘

┌─────────────────── READ ────────────────────────────────────┐
│                                                              │
│  Client                                                      │
│    │ 1. open(/data/x.csv)                                    │
│    ▼                                                         │
│  NameNode                                                    │
│    │ 2. Returns block locations (sorted by proximity)        │
│    ▼                                                         │
│  Client → DN2: read Block 1 (closest)                        │
│  Client → DN4: read Block 2 (closest)                        │
│    │                                                         │
│    │ 3. Assemble blocks → return complete file               │
│                                                              │
└──────────────────────────────────────────────────────────────┘

┌─────────────────── FAILURE RECOVERY ────────────────────────┐
│                                                              │
│  DN5 crashes                                                 │
│    │                                                         │
│  NameNode: no heartbeat for 10 min → mark DN5 dead          │
│    │                                                         │
│  Blocks on DN5 now under-replicated                          │
│    │                                                         │
│  NameNode: schedule re-replication from surviving replicas   │
│    │                                                         │
│  DN2 → DN9 (copy Block B1 to restore replication factor)    │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

> **Key Takeaway:** Distributed file systems enable petabyte-scale storage on commodity hardware by splitting files into large blocks, replicating them across multiple nodes with rack awareness, and separating metadata management from data storage. Understanding the master-worker architecture, replication strategies, consistency trade-offs, and fault tolerance mechanisms is essential for designing any large-scale storage system.