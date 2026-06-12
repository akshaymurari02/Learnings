package pubsub;

import java.util.UUID;

/**
 * Base implementation of a subscriber.
 * Extend this class and override onMessage() to handle incoming messages.
 */
public abstract class Subscriber implements ISubscriber {

    private final String subscriberId;
    private final String name;

    public Subscriber(String name) {
        this.subscriberId = UUID.randomUUID().toString();
        this.name = name;
    }

    @Override
    public String getSubscriberId() {
        return subscriberId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public abstract void onMessage(Message message);

    @Override
    public String toString() {
        return String.format("Subscriber[id=%s, name=%s]", subscriberId, name);
    }
}
