package pubsub;

/**
 * Synchronous message delivery - delivers immediately on the same thread.
 */
public class SyncDeliveryStrategy implements IMessageDeliveryStrategy {

    @Override
    public void deliver(Message message, ISubscriber subscriber) {
        try {
            subscriber.onMessage(message);
        } catch (Exception e) {
            System.err.println("Error delivering message to " + subscriber.getName() + ": " + e.getMessage());
        }
    }
}
