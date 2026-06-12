package amazon.cache.policy;

import amazon.cache.lists.LinkListNode;
import amazon.cache.lists.DLL;

import java.util.HashMap;
import java.util.Map;

public class LRUEvictionPolicy<K> implements IEvictionPolicy<K>
{

    Map<K, LinkListNode<K>> map;
    DLL<K> dll;

    public LRUEvictionPolicy()
    {
        dll = new DLL<>();
        map = new HashMap<>();
    }

    @Override
    public void keyAccessed(K key) {
        LinkListNode<K> node = map.get(key);

        if(node==null)
        {
            node=new LinkListNode<>(key);
            map.put(key, node);
        }

        dll.removeNode(node);
        dll.appendNode(node);
    }

    @Override
    public K getEvictKey() {
        LinkListNode<K> node = dll.headNode();
        dll.removeNode(node);
        map.remove(node.value);
        return node.value;
    }

    @Override
    public void removeKey(K Key)
    {
        LinkListNode<K> node = map.get(Key);
        dll.removeNode(node);
        map.remove(Key);
    }
}
