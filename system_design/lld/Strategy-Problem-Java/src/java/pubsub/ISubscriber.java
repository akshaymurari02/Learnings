package pubsub;

/**
 * Interface for subscribers to receive messages.
 */
public interface ISubscriber {

    /**
     * Called when a message is received on a subscribed topic.
     */
    void onMessage(Message message);

    /**
     * Returns the unique ID of this subscriber.
     */
    String getSubscriberId();

    /**
     * Returns the name of this subscriber (for logging/debugging).
     */
    String getName();
}
