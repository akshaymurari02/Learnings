package cache;

import cache.policy.EvictionPolicy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCache<K extends Number, String> extends Cache<K, String>
{

    public Map<K, String> cache;

    public InMemoryCache(EvictionPolicy evictionPolicy, Integer capacity) {
        super(evictionPolicy, capacity);
        cache = new HashMap<>(capacity);
    }

    @Override
    public synchronized void put(K key, String value)
    {

        evictionPolicy.keyAccessed(key);
        cache.put(key, value);

        if(cache.containsKey(key))
        {
            return;
        }

        if(cache.size()>capacity)
        {
            K evictionKey = (K) evictionPolicy.evictKey();
            cache.remove(evictionKey);
        }

    }

    @Override
    public synchronized String get(K key) {
        if(cache.containsKey(key)) {
            evictionPolicy.keyAccessed(key);
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void remove(K key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
    }

}
