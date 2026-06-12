package amazon.ratelimiter.factory;

import amazon.ratelimiter.IRateLimiter;
import amazon.ratelimiter.RateLimiterType;
import amazon.ratelimiter.TokenBucketRateLimiter;

import java.util.Map;

public class RateLimiterFactory {

    public static Map<RateLimiterType, IRateLimiter> rateLimiterMap = Map.of(RateLimiterType.TOKEN_BUCKET, new TokenBucketRateLimiter());

    public static IRateLimiter createRateLimiter(RateLimiterType type) {
        return rateLimiterMap.get(type);
    }
}
