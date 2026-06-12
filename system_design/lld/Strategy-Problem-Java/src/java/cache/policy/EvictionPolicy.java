package cache.policy;

public interface EvictionPolicy
{

    public void keyAccessed(Object key);

    Object evictKey();
}
