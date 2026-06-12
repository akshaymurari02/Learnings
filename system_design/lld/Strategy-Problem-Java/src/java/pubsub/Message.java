package pubsub;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message in the pub-sub system.
 */
public class Message {

    private final String id;
    private final String topic;
    private final Object payload;
    private final LocalDateTime timestamp;
    private final String publisherId;

    public Message(String topic, Object payload, String publisherId) {
        this.id = UUID.randomUUID().toString();
        this.topic = topic;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
        this.publisherId = publisherId;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public Object getPayload() {
        return payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPublisherId() {
        return publisherId;
    }

    @Override
    public String toString() {
        return String.format("Message[id=%s, topic=%s, publisher=%s, time=%s]",
            id, topic, publisherId, timestamp);
    }
}
