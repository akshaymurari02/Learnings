package amazon.ratelimiter;

import amazon.ratelimiter.user.User;

public interface IRateLimiter
{

    public boolean allowAccess(User user);

}
