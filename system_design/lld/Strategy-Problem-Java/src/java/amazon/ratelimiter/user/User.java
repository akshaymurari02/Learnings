package amazon.ratelimiter.user;

public class User
{
    private final String userId;
    private final String tier;

    public User(String userId, String tier) {
        this.userId = userId;
        this.tier = tier;
    }

    public String getUserId() {
        return userId;
    }

    public String getTier() {
        return tier;
    }
}
