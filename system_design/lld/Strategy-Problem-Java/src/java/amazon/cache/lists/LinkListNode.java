package amazon.cache.lists;

public class LinkListNode<V>
{
    public V value;
    public LinkListNode<V> next, prev;

    public LinkListNode(V value)
    {
        this.value = value;
        this.next = null;
        this.prev = null;
    }
}
