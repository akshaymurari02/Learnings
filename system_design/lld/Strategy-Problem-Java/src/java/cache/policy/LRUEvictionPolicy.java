package cache.policy;

import cache.linkedlist.DLL;
import cache.linkedlist.LinkedListNode;

import java.util.HashMap;
import java.util.Map;

public class LRUEvictionPolicy implements EvictionPolicy
{
    Map<Object, LinkedListNode<Object>> map;
    DLL<Object> dll;

    public LRUEvictionPolicy() {
        this.map = new HashMap<>();
        this.dll = new DLL<>();
    }

    @Override
    public void keyAccessed(Object key) {

        if(!map.containsKey(key))
        {
            LinkedListNode<Object> node = new LinkedListNode<>(key);
            dll.append(node);
            map.put(key, node);
            return ;
        }

        LinkedListNode<Object> node = map.get(key);
        dll.remove(node);
        dll.append(node);
    }

    @Override
    public Object evictKey() {
        LinkedListNode<Object> node = dll.headNode();
        if (node == null) return null;
        dll.remove(node);
        map.remove(node.value);
        return node.value;
    }
}
