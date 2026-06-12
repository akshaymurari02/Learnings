package pubsub;

/**
 * Subscriber that filters messages based on a condition.
 */
public class FilteredSubscriber extends Subscriber {

    private final ISubscriber delegate;
    private final MessageFilter filter;

    public FilteredSubscriber(String name, ISubscriber delegate, MessageFilter filter) {
        super(name);
        this.delegate = delegate;
        this.filter = filter;
    }

    @Override
    public void onMessage(Message message) {
        if (filter.accept(message)) {
            delegate.onMessage(message);
        }
    }

    @FunctionalInterface
    public interface MessageFilter {
        boolean accept(Message message);
    }
}
