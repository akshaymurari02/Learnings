package ratelimiter;

public class TokenBucket {

    private int capacity;
    private int tokens;
    private long lastRefillTimestamp;

    public TokenBucket() {
        this.capacity = 10;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastRefillTimestamp;
        int tokensToAdd = (int) (elapsedTime / 1000);
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
}
