# 📘 HLD Fundamentals — Long Polling vs WebSockets vs SSE

---

# 1. Introduction

Modern applications often require real-time communication between clients and servers.

Examples:

* Chat applications
* Notifications
* Multiplayer games
* Live dashboards
* Stock market updates
* Collaborative editing

Traditional HTTP request-response communication is not ideal for realtime systems because the server cannot push updates instantly after the response completes.

To solve this problem, systems use:

* Long Polling
* WebSockets
* Server-Sent Events (SSE)

---

# 2. Traditional HTTP Communication

Traditional HTTP works as:

```text
Client → Request
Server → Response
Connection Closed
```

Problems for realtime systems:

* Client must repeatedly poll server
* High latency
* Wasteful network overhead
* Inefficient resource usage

---

# 3. Long Polling

## Definition

Long polling is a technique where the client sends a request and the server keeps the connection open until data becomes available.

Once the server responds:

* Client immediately sends another request.

This creates an illusion of realtime communication.

---

# 4. Long Polling Workflow

```text
Client → Request
Server waits
Server sends response when event occurs
Connection closes
Client reconnects
```

---

# 5. Long Polling Characteristics

* Uses standard HTTP
* Repeated reconnections
* Simulates server push
* Simpler than WebSockets

---

# 6. Advantages of Long Polling

* Easy to implement
* Works with existing HTTP infrastructure
* Compatible with older browsers and proxies

---

# 7. Limitations of Long Polling

* Repeated HTTP overhead
* Frequent TCP reconnections
* Higher latency compared to WebSockets
* Poor scalability for massive concurrent users
* Increased server resource consumption

---

# 8. Long Polling Use Cases

* Legacy systems
* Basic notification systems
* Simple realtime applications

---

# 9. WebSockets

## Definition

WebSocket is a protocol that provides a persistent, full-duplex communication channel between client and server.

After an initial HTTP handshake:

* Connection upgrades to WebSocket protocol
* Both sides can send messages anytime

---

# 10. WebSocket Workflow

```text
Client → HTTP Upgrade Request
Server → Upgrade Accepted
Persistent WebSocket Connection Established
Client ↔ Server communicate continuously
```

---

# 11. WebSocket Characteristics

* Persistent connection
* Bidirectional communication
* Very low latency
* Full duplex communication
* Efficient for realtime systems

---

# 12. Advantages of WebSockets

* Extremely low latency
* Minimal protocol overhead after connection established
* Real-time bidirectional communication
* Efficient network utilization

---

# 13. Limitations of WebSockets

* Stateful connections
* Harder scaling
* Load balancing complexity
* Sticky sessions often required
* Connection management overhead

---

# 14. WebSocket Use Cases

* Chat applications
* Multiplayer games
* Live collaboration tools
* Trading systems
* Realtime analytics

---

# 15. Server-Sent Events (SSE)

## Definition

SSE provides a persistent HTTP connection where:

* Server continuously streams events to client

Communication is:

```text
Server → Client only
```

---

# 16. SSE Workflow

```text
Client establishes HTTP connection
Server streams events continuously
Connection remains open
```

---

# 17. SSE Characteristics

* Unidirectional communication
* Built on standard HTTP
* Lightweight compared to WebSockets
* Automatic reconnection support

---

# 18. Advantages of SSE

* Simpler than WebSockets
* Native browser support
* Automatic reconnection
* Efficient for server push workloads
* HTTP-friendly

---

# 19. Limitations of SSE

* Server-to-client only
* Not suitable for bidirectional communication
* Limited binary data support

---

# 20. SSE Use Cases

* Notifications
* Live dashboards
* Analytics streaming
* Stock price updates
* News feeds

---

# 21. Long Polling vs WebSockets vs SSE

| Feature         | Long Polling         | WebSockets    | SSE                |
| --------------- | -------------------- | ------------- | ------------------ |
| Connection Type | Repeated HTTP        | Persistent    | Persistent HTTP    |
| Communication   | Mostly server→client | Bidirectional | Server→client      |
| Latency         | Higher               | Very low      | Low                |
| Overhead        | High                 | Low           | Low                |
| Scalability     | Poorer               | Complex       | Better for one-way |
| Complexity      | Simple               | Higher        | Moderate           |
| Protocol        | HTTP                 | WebSocket     | HTTP               |

---

# 22. Scalability Considerations

---

# Long Polling Scalability

Problems:

* Repeated connections
* High HTTP overhead
* Large thread/socket usage
* High CPU utilization

---

# WebSocket Scalability

Problems:

* Millions of open connections
* Stateful infrastructure
* Memory overhead per connection
* Sticky sessions may be required

Solutions:

* Event-driven servers
* Pub/Sub systems
* Distributed WebSocket gateways

---

# SSE Scalability

Better scalability for:

* One-way event streaming

Reason:

* Simpler protocol
* HTTP-native behavior

---

# 23. Real-world Architecture

```text
Users
 ↓
Load Balancer
 ↓
Realtime Gateway / WebSocket Servers
 ↓
Pub/Sub System (Kafka/Redis)
 ↓
Backend Services
```

---

# 24. Load Balancing Challenges

---

# Long Polling

Mostly stateless.
Easier to distribute.

---

# WebSockets

Persistent stateful connections.

Challenges:

* Sticky sessions
* Connection affinity
* Stateful scaling

---

# SSE

Persistent but simpler than WebSockets.

---

# 25. WebSocket Internals

Uses:

```text
ws://
wss://
```

Connection established using:

```text
HTTP Upgrade handshake
```

After upgrade:

* Communication no longer regular HTTP.

---

# 26. Choosing the Right Approach

---

# Use Long Polling When

* Simplicity important
* Legacy compatibility needed
* Low realtime requirements

---

# Use WebSockets When

* Bidirectional realtime communication needed
* Very low latency required

Examples:

* Chats
* Gaming
* Collaborative editing

---

# Use SSE When

* Only server pushes updates
* Simpler architecture preferred

Examples:

* Notifications
* Dashboards
* Event streams

---

# 27. Important Mental Models

* Long Polling = repeated waiting requests
* WebSocket = persistent realtime duplex channel
* SSE = lightweight server push stream

---

# 28. Common Interview Questions

---

## Why WebSockets scale differently?

Because they maintain long-lived stateful connections.

---

## Why SSE simpler than WebSockets?

Because SSE supports only one-way server push over HTTP.

---

## Why Long Polling inefficient?

Repeated HTTP setup and reconnections create overhead.

---

# 29. Interview Summary

Long polling, WebSockets, and Server-Sent Events are techniques used for realtime communication in distributed systems. Long polling simulates realtime updates through repeated HTTP requests, WebSockets provide persistent bidirectional communication with very low latency, and SSE enables efficient unidirectional server-to-client event streaming over HTTP. The choice depends on latency requirements, scalability needs, and communication direction.

---
