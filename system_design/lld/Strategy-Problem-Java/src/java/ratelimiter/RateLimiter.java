package ratelimiter;

public class RateLimiter implements IRateLimiter
{

    RedisService redisService;

    RateLimiter(RedisService redisService)
    {
        this.redisService = redisService;
    }

    @Override
    public boolean allowRequest(User user) {
        TokenBucket tokenBucket = redisService.getTokenBucket(user.getUserId());
        return tokenBucket.allowRequest();
    }
}
