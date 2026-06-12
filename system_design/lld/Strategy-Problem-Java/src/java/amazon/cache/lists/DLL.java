package amazon.cache.lists;

public class DLL<V>
{

    public LinkListNode<V> headNode, tailNode;

    public DLL()
    {
        headNode = new LinkListNode<>(null);
        tailNode = new LinkListNode<>(null);
        headNode.next = tailNode;
        tailNode.prev = headNode;
    }

    public void removeNode(LinkListNode<V> node) {
        LinkListNode<V> temp1, temp2;
        temp1 = node.prev;
        temp2 = node.next;
        temp1.next=temp2;
        temp2.prev=temp1;
        appendNode(node);
    }

    public void appendNode(LinkListNode<V> node) {
        LinkListNode<V> temp;
        temp=tailNode.prev;
        tailNode.prev=node;
        node.next=tailNode;
        node.prev=temp;
        temp.next=node;
    }

    public LinkListNode<V> headNode() {
        return headNode.next;
    }
}
