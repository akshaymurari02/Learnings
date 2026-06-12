package amazon.ratelimiter;

import amazon.ratelimiter.config.TokenBucketRateLimiterConfig;

import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {

    private final int capacity;
    private final int refillTokens;
    private final long refillIntervalNanos;
    private int currentTokens;
    private long lastRefillTimestamp;
    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucket(TokenBucketRateLimiterConfig config) {
        this.capacity = config.getCapacity();
        this.refillTokens = config.getRefillTokens();
        this.refillIntervalNanos = 1_000_000_000L; // 1 second refill interval
        this.currentTokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    public boolean tryConsume() {
        lock.lock();
        try {
            refill();
            if (currentTokens > 0) {
                currentTokens--;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long now = System.nanoTime();
        long elapsed = now - lastRefillTimestamp;
        long intervals = elapsed / refillIntervalNanos;

        if (intervals > 0) {
            currentTokens = Math.min(capacity, currentTokens + (int) (intervals * refillTokens));
            lastRefillTimestamp += intervals * refillIntervalNanos;
        }
    }
}
