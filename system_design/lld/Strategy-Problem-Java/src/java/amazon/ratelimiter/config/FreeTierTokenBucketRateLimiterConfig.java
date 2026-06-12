package amazon.ratelimiter.config;

public class FreeTierTokenBucketRateLimiterConfig extends TokenBucketRateLimiterConfig {

    public FreeTierTokenBucketRateLimiterConfig() {
        super(100, 10);
    }
}
