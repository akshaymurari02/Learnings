package pubsub;

import java.util.UUID;

/**
 * Publisher that can publish messages to topics via the message broker.
 */
public class Publisher {

    private final String publisherId;
    private final String name;
    private final MessageBroker broker;

    public Publisher(String name, MessageBroker broker) {
        this.publisherId = UUID.randomUUID().toString();
        this.name = name;
        this.broker = broker;
    }

    /**
     * Publish a message to a topic.
     */
    public void publish(String topicName, Object payload) {
        broker.publish(topicName, payload, publisherId);
    }

    /**
     * Create and publish to a new topic in one call.
     */
    public void publishToNewTopic(String topicName, Object payload) {
        if (!broker.topicExists(topicName)) {
            broker.createTopic(topicName);
        }
        publish(topicName, payload);
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Publisher[id=%s, name=%s]", publisherId, name);
    }
}
