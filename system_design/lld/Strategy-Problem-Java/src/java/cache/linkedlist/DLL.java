package cache.linkedlist;

public class DLL<V> {

    private LinkedListNode<V> head;
    private LinkedListNode<V> tail;

    public DLL() {
        // Sentinel nodes to simplify edge cases
        head = new LinkedListNode<>(null);
        tail = new LinkedListNode<>(null);
        head.next = tail;
        tail.prev = head;
    }

    public void append(LinkedListNode<V> node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    public void remove(LinkedListNode<V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
    }

    public LinkedListNode<V> headNode() {
        if (isEmpty()) return null;
        return head.next;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }
}
