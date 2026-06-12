package amazon.ratelimiter.config;

public class TokenBucketRateLimiterConfig {

     private int capacity;
     private int refillTokens;

     public TokenBucketRateLimiterConfig(int capacity, int refillTokens) {
          this.capacity = capacity;
          this.refillTokens = refillTokens;
     }

     public int getCapacity() {
          return capacity;
     }

     public int getRefillTokens() {
          return refillTokens;
     }
}
