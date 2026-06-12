package pubsub;

/**
 * Subscriber with a custom message handler (functional interface style).
 */
public class CallbackSubscriber extends Subscriber {

    private final MessageHandler handler;

    public CallbackSubscriber(String name, MessageHandler handler) {
        super(name);
        this.handler = handler;
    }

    @Override
    public void onMessage(Message message) {
        handler.handle(message);
    }

    @FunctionalInterface
    public interface MessageHandler {
        void handle(Message message);
    }
}
