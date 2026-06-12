package amazon.ratelimiter.config;

public class ProTierTokenBucketRateLimiterConfig extends TokenBucketRateLimiterConfig {
    public ProTierTokenBucketRateLimiterConfig() {
        super(300, 50);
    }
}
