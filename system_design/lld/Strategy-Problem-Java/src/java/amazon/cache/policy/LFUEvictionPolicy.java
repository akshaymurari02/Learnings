package amazon.cache.policy;

import amazon.cache.lists.DLL;
import amazon.cache.lists.LinkListNode;

import java.util.HashMap;
import java.util.Map;

public class LFUEvictionPolicy<V> implements IEvictionPolicy<V>
{

    public int minFrequency;
    Map<Integer, DLL<V>> frequencyToDLLMap;
    Map<V, LinkListNode<V>> keyTONodeMap;
    Map<V, Integer> keyTOFreqMap;

    public LFUEvictionPolicy()
    {
        minFrequency = 1;
        frequencyToDLLMap = new HashMap<>();
        keyTONodeMap = new HashMap<>();
    }

    @Override
    public void keyAccessed(V key) {

        if(!keyTOFreqMap.containsKey(key))
        {
            keyTOFreqMap.put(key, 1);

            DLL<V> dll = frequencyToDLLMap.computeIfAbsent(
                    1,
                    k -> new DLL<>()
            );

            LinkListNode<V> node = new LinkListNode<>(key);

            dll.appendNode(node);

            keyTONodeMap.put(key, node);

            minFrequency = 1;

            return;
        }

        int oldFreq = keyTOFreqMap.get(key);

        DLL<V> oldDLL = frequencyToDLLMap.get(oldFreq);

        LinkListNode<V> node = keyTONodeMap.get(key);

        oldDLL.removeNode(node);

        if(oldFreq == minFrequency &&
                oldDLL.headNode.next == oldDLL.tailNode)
        {
            minFrequency++;
        }

        int newFreq = oldFreq + 1;

        DLL<V> newDLL = frequencyToDLLMap.computeIfAbsent(
                newFreq,
                k -> new DLL<>()
        );

        newDLL.appendNode(node);

        keyTOFreqMap.put(key, newFreq);
    }

    @Override
    public V getEvictKey() {

        DLL<V> dll = frequencyToDLLMap.get(minFrequency);

        LinkListNode<V> node = dll.headNode.next;

        dll.removeNode(node);

        V key = node.value;

        keyTONodeMap.remove(key);
        keyTOFreqMap.remove(key);

        return key;
    }

    @Override
    public void removeKey(V key) {

        if(!keyTOFreqMap.containsKey(key))
            return;

        int freq = keyTOFreqMap.get(key);

        DLL<V> dll = frequencyToDLLMap.get(freq);

        LinkListNode<V> node = keyTONodeMap.get(key);

        dll.removeNode(node);

        keyTONodeMap.remove(key);
        keyTOFreqMap.remove(key);

        if(freq == minFrequency &&
                dll.headNode.next == dll.tailNode)
        {
            minFrequency++;
        }
    }
}
