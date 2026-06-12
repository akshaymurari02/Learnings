package cache;

import cache.policy.LRUEvictionPolicy;

public class Main {

    public static void main(String[] args)
    {

        InMemoryCache<Integer, String> memoryCache = new InMemoryCache<>(new LRUEvictionPolicy(), 2);

        memoryCache.put(1,"test1");
        memoryCache.put(2,"test2");
        memoryCache.put(3,"test3");

        System.out.println(memoryCache.get(2));
        System.out.println(memoryCache.get(3));


    }
}
