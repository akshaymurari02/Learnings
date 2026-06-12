package amazon.cache.policy;

public interface IEvictionPolicy<K>
{

    void keyAccessed(K key);

    K getEvictKey();

    void removeKey(K Key);
}
