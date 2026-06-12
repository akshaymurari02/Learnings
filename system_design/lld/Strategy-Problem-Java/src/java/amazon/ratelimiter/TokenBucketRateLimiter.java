package amazon.ratelimiter;

import amazon.ratelimiter.config.FreeTierTokenBucketRateLimiterConfig;
import amazon.ratelimiter.config.ProTierTokenBucketRateLimiterConfig;
import amazon.ratelimiter.config.Tier;
import amazon.ratelimiter.config.TokenBucketRateLimiterConfig;
import amazon.ratelimiter.user.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter implements IRateLimiter
{

    private final Map<User, TokenBucket> userBuckets;
    private final Map<Tier, TokenBucketRateLimiterConfig> tierConfigs;

    public TokenBucketRateLimiter()
    {
        userBuckets=new ConcurrentHashMap<>();
        tierConfigs= Collections.synchronizedMap(new HashMap<>() {{
            put(Tier.FREE, new FreeTierTokenBucketRateLimiterConfig());
            put(Tier.PRO, new ProTierTokenBucketRateLimiterConfig());
        }});
    }

    @Override
    public boolean allowAccess(User user) {
        TokenBucket bucket = userBuckets.get(user);

        if (bucket == null) {
            synchronized (this) {
                bucket = userBuckets.get(user);
                if (bucket == null) {
                    TokenBucketRateLimiterConfig config = tierConfigs.get(Tier.valueOf(user.getTier()));
                    bucket = new TokenBucket(config);
                    userBuckets.put(user, bucket);
                }
            }
        }

        return bucket.tryConsume();
    }

}
