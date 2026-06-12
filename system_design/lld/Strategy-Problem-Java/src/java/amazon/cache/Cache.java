package amazon.cache;

import amazon.cache.policy.IEvictionPolicy;

public abstract class Cache<K, V>
{

    public IEvictionPolicy<K> evictionPolicy;
    public int capacity;

    public Cache(IEvictionPolicy<K> evictionPolicy, int capacity)
    {
        this.evictionPolicy=evictionPolicy;
        this.capacity = capacity;
    }

    /**
     *
     * for expiring keys we can also use CacheExpiryEntry
     * and a schedular which runs periodically and maintain this CacheExpiryEntries in
     * PriorityQueue or can evict only when the event on this key is called
     *
     */
    abstract void put(K key, V value);

    abstract V get(K key);

    abstract void remove(K key);

}
