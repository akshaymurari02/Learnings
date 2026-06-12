package cache;

import cache.policy.EvictionPolicy;

public abstract class Cache<K extends Number, String> {

    public EvictionPolicy evictionPolicy;
    public Integer capacity;

    public Cache(EvictionPolicy evictionPolicy, Integer capacity) {
        this.evictionPolicy = evictionPolicy;
        this.capacity = capacity;

    }

    public abstract void put(K key, String value);

    public abstract String get(K key);

    public abstract void remove(K key);
}
