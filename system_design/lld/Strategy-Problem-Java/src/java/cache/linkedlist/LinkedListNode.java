package cache.linkedlist;

public class LinkedListNode<V>
{

    public LinkedListNode<V> prev, next;
    public V value;

    public LinkedListNode(V value)
    {
        this.prev=null;
        this.next=null;
        this.value=value;
    }
}
