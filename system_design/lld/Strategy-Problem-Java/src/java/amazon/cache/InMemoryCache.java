package amazon.cache;

import amazon.cache.policy.LRUEvictionPolicy;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCache<K, V> extends Cache<K, V> {

    Map<K, V> map;

    public InMemoryCache(int capacity) {
        super(new LRUEvictionPolicy<K>(), capacity);
        map = new HashMap<>();
    }

    @Override
    void put(K key, V value) {
        evictionPolicy.keyAccessed(key);
        map.put(key, value);

        if (map.size() > capacity) {
            K evictKey = evictionPolicy.getEvictKey();
            map.remove(evictKey);
        }
    }

    @Override
    V get(K key) {
        if (map.containsKey(key)) {
            evictionPolicy.keyAccessed(key);
            return map.get(key);
        }
        return null;
    }

    @Override
    void remove(K key) {
        if(map.containsKey(key)) {
            evictionPolicy.removeKey(key);
            map.remove(key);
        }
    }
}
