package pubsub;

/**
 * Interface for message delivery strategies.
 * Allows different delivery mechanisms (sync, async, batched, etc).
 */
public interface IMessageDeliveryStrategy {

    /**
     * Deliver a message to a subscriber.
     */
    void deliver(Message message, ISubscriber subscriber);

    /**
     * Shutdown the delivery mechanism.
     */
    default void shutdown() {}
}
