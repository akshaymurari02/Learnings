package pubsub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Asynchronous message delivery - delivers on a background thread pool.
 */
public class AsyncDeliveryStrategy implements IMessageDeliveryStrategy {

    private final ExecutorService executorService;

    public AsyncDeliveryStrategy() {
        this(10);
    }

    public AsyncDeliveryStrategy(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void deliver(Message message, ISubscriber subscriber) {
        executorService.submit(() -> {
            try {
                subscriber.onMessage(message);
            } catch (Exception e) {
                System.err.println("Error delivering message to " + subscriber.getName() + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
