package pubsub;

/**
 * Demo showing how to use the Pub-Sub system.
 *
 * Example usage patterns:
 * 1. Simple synchronous pub-sub
 * 2. Asynchronous pub-sub
 * 3. Multiple subscribers on same topic
 * 4. Multiple topics
 * 5. Filtered subscribers
 * 6. Message history
 */
public class PubSubDemo {

    public static void main(String[] args) {
        // Example 1: Simple Pub-Sub with PubSubService
        simpleExample();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Example 2: Using MessageBroker directly
        brokerExample();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Example 3: Async delivery
        asyncExample();
    }

    /**
     * Simple example using PubSubService (recommended for most use cases).
     */
    public static void simpleExample() {
        System.out.println("Example 1: Simple Pub-Sub with PubSubService");

        // Create service
        PubSubService service = new PubSubService();

        // Create topic
        service.createTopic("news", "News updates");

        // Create subscribers
        ISubscriber subscriber1 = new SimpleSubscriber("Alice");
        ISubscriber subscriber2 = new SimpleSubscriber("Bob");

        // Subscribe to topic
        service.subscribe("news", subscriber1);
        service.subscribe("news", subscriber2);

        // Create publisher and publish
        Publisher publisher = service.createPublisher("NewsAgency");
        publisher.publish("news", "Breaking news: Java 21 released!");
        publisher.publish("news", "Weather update: Sunny day ahead");

        // Check stats
        System.out.println("\nStats: " + service.getStats());

        service.shutdown();
    }

    /**
     * Example using MessageBroker directly for more control.
     */
    public static void brokerExample() {
        System.out.println("Example 2: Using MessageBroker Directly");

        // Create broker with history enabled
        MessageBroker broker = MessageBroker.create(new SyncDeliveryStrategy(), true);

        // Create topic
        broker.createTopic("orders");

        // Create subscribers with callbacks
        ISubscriber orderProcessor = new CallbackSubscriber("OrderProcessor", message -> {
            System.out.println("Processing order: " + message.getPayload());
        });

        ISubscriber orderLogger = new CallbackSubscriber("OrderLogger", message -> {
            System.out.println("Logging order: " + message.getId());
        });

        // Subscribe
        broker.subscribe("orders", orderProcessor);
        broker.subscribe("orders", orderLogger);

        // Publish
        Publisher publisher = new Publisher("ShoppingCart", broker);
        publisher.publish("orders", "Order #1001: Laptop");
        publisher.publish("orders", "Order #1002: Mouse");

        // View history
        System.out.println("\nMessage History:");
        broker.getHistory("orders").forEach(msg ->
            System.out.println("  - " + msg.getPayload() + " at " + msg.getTimestamp())
        );

        broker.shutdown();
    }

    /**
     * Example with async delivery for high-throughput scenarios.
     */
    public static void asyncExample() {
        System.out.println("Example 3: Async Message Delivery");

        // Create service with async delivery
        PubSubService service = new PubSubService(new AsyncDeliveryStrategy(5));

        // Create topic
        service.createTopic("logs");

        // Create subscriber
        ISubscriber logSubscriber = new CallbackSubscriber("LogAggregator", message -> {
            System.out.println("[ASYNC] Received: " + message.getPayload());
        });

        service.subscribe("logs", logSubscriber);

        // Publish multiple messages rapidly
        Publisher publisher = service.createPublisher("Application");
        for (int i = 1; i <= 5; i++) {
            publisher.publish("logs", "Log entry #" + i);
        }

        // Wait a bit for async processing
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();
    }

    /**
     * Example with filtered subscriber.
     */
    public static void filteredExample() {
        System.out.println("Example 4: Filtered Subscriber");

        PubSubService service = new PubSubService();
        service.createTopic("events");

        // Base subscriber
        ISubscriber baseSubscriber = new SimpleSubscriber("EventHandler");

        // Filtered subscriber - only accepts messages containing "error"
        ISubscriber errorFilter = new FilteredSubscriber(
            "ErrorHandler",
            baseSubscriber,
            message -> message.getPayload().toString().toLowerCase().contains("error")
        );

        service.subscribe("events", errorFilter);

        Publisher publisher = service.createPublisher("App");
        publisher.publish("events", "Info: Application started");
        publisher.publish("events", "Error: Connection failed");  // This will be received
        publisher.publish("events", "Debug: Processing request");

        service.shutdown();
    }
}
