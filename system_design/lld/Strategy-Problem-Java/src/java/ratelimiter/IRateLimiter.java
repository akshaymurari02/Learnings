package ratelimiter;

public interface IRateLimiter {

    public boolean allowRequest(User user);

}
