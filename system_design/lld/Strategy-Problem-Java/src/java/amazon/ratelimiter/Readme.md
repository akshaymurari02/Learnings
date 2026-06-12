RateLimiter 

FR: 

control requests to allow or disallow access to a resource.
expecting to implement a rate limiter which can allow burst requests and also can control the average request rate.


NFR:
1. The rate limiter should allow burst requests up to a certain limit.
2. The rate limiter should control the average request rate over a specified time window.
3. The rate limiter should be thread-safe and efficient in handling concurrent requests.
4. The rate limiter should provide a way to configure the burst limit and the average request rate.
5. The rate limiter should provide a way to reset the counters after a certain time window.
6. implement rate limiter as strategy pattern, so that we can easily switch between different rate limiting algorithms (e.g., token bucket, leaky bucket, fixed window, sliding window).
7. Assuming each request has a unique identifier, the rate limiter should be able to track and manage requests based on user identifiers to prevent abuse and ensure fair usage.

Classes

IRateLimiter: 
    methods: 
        - allowRequest(userId: String): boolean
        - updateConfig(capacity: int, refillRate: int): void


TokenBucketRateLimiter implements IRateLimiter:
    properties:
        - userTier: Map<String, Tier>
        - tierConfig: Map<Tier, TokenBucketRateLimiterConfig>
        - userBuckets: Map<String, TokenBucket>
    methods:
        - allowRequest(userId: String): boolean
        - updateConfig(config: TokenBucketRateLimiterConfig): void

TokenBucket:
    properties:
        - tokens: int
        - lastRefillTimestamp: long
    methods:
        - refill(): void
        - tryConsume(): boolean

TokenBucketRateLimiterConfig:
properties:
- capacity: int
- refillRate: int

Tier:
    - FREE
    - BASIC
    - PREMIUM


RateLimiterType: 
    - TOKEN_BUCKET
    - FIXED_WINDOW

RateLimiterFactory:
    methods:
        - createRateLimiter(type: RateLimiterType, config: TokenBucketRateLimiterConfig): IRateLimiter


Relationships:

1. IRateLimiter is an interface that defines the contract for rate limiting functionality.
2. TokenBucketRateLimiter implements the IRateLimiter interface and provides a specific implementation using the
3. token bucket algorithm.
4. TokenBucket is a helper class used by TokenBucketRateLimiter to manage the tokens for each user.
5. TokenBucketRateLimiterConfig is a configuration class that holds the parameters for the token bucket rate limiter.
6. RateLimiterFactory is a factory class that creates instances of IRateLimiter based on the specified type and configuration.
7. The RateLimiterFactory allows for easy switching between different rate limiting algorithms by abstracting the creation process and providing a common interface for clients to interact with.
8. The IRateLimiter interface allows for flexibility and extensibility, enabling the addition of new rate limiting algorithms in the future without modifying existing code.
9. The TokenBucketRateLimiter maintains a mapping of user identifiers to their respective token buckets, allowing for individualized rate limiting based on user activity.
10. The allowRequest method in TokenBucketRateLimiter checks if a request from a specific user can be allowed based on the current state of their token bucket, while the updateConfig method allows for dynamic updates to the rate limiter's configuration parameters.
11. The TokenBucket class manages the token count and refill logic, ensuring that tokens are replenished at the specified rate and consumed appropriately when requests are made.
12. The RateLimiterFactory provides a centralized point for creating rate limiter instances, promoting code reuse and separation of concerns by decoupling the instantiation logic from the client code that uses the rate limiter.
13. Overall, this design allows for a flexible and extensible rate limiting system that can accommodate various algorithms and configurations while ensuring thread safety and efficient handling of concurrent requests.

