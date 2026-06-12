package pubsub;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple subscriber that stores received messages in a list.
 * Useful for testing and simple use cases.
 */
public class SimpleSubscriber extends Subscriber {

    private final List<Message> receivedMessages;

    public SimpleSubscriber(String name) {
        super(name);
        this.receivedMessages = new ArrayList<>();
    }

    @Override
    public void onMessage(Message message) {
        System.out.println(getName() + " received: " + message.getPayload());
        receivedMessages.add(message);
    }

    public List<Message> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public int getMessageCount() {
        return receivedMessages.size();
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}
