package pubsub;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central message broker that manages topics, subscriptions, and message delivery.
 * Thread-safe implementation using concurrent collections.
 */
public class MessageBroker {

    private static MessageBroker instance;

    // Topic -> List of Subscribers
    private final Map<String, List<ISubscriber>> subscriptions;

    // All registered topics
    private final Map<String, Topic> topics;

    // Message delivery strategy
    private IMessageDeliveryStrategy deliveryStrategy;

    // Message history (optional, for replay)
    private final Map<String, List<Message>> messageHistory;
    private final boolean keepHistory;

    private MessageBroker() {
        this(new SyncDeliveryStrategy(), false);
    }

    private MessageBroker(IMessageDeliveryStrategy deliveryStrategy, boolean keepHistory) {
        this.subscriptions = new ConcurrentHashMap<>();
        this.topics = new ConcurrentHashMap<>();
        this.messageHistory = new ConcurrentHashMap<>();
        this.deliveryStrategy = deliveryStrategy;
        this.keepHistory = keepHistory;
    }

    /**
     * Singleton instance (default configuration).
     */
    public static synchronized MessageBroker getInstance() {
        if (instance == null) {
            instance = new MessageBroker();
        }
        return instance;
    }

    /**
     * Create a new broker instance with custom configuration.
     */
    public static MessageBroker create(IMessageDeliveryStrategy deliveryStrategy, boolean keepHistory) {
        return new MessageBroker(deliveryStrategy, keepHistory);
    }

    /**
     * Reset the singleton instance (useful for testing).
     */
    public static synchronized void reset() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }

    // ==================== TOPIC MANAGEMENT ====================

    /**
     * Create a new topic.
     */
    public synchronized void createTopic(String topicName) {
        createTopic(new Topic(topicName));
    }

    /**
     * Create a new topic.
     */
    public synchronized void createTopic(Topic topic) {
        if (topics.containsKey(topic.getName())) {
            throw new IllegalArgumentException("Topic already exists: " + topic.getName());
        }
        topics.put(topic.getName(), topic);
        subscriptions.put(topic.getName(), new CopyOnWriteArrayList<>());
        if (keepHistory) {
            messageHistory.put(topic.getName(), new CopyOnWriteArrayList<>());
        }
    }

    /**
     * Delete a topic and all its subscriptions.
     */
    public synchronized void deleteTopic(String topicName) {
        topics.remove(topicName);
        subscriptions.remove(topicName);
        messageHistory.remove(topicName);
    }

    /**
     * Check if a topic exists.
     */
    public boolean topicExists(String topicName) {
        return topics.containsKey(topicName);
    }

    /**
     * Get all topics.
     */
    public List<Topic> getAllTopics() {
        return new ArrayList<>(topics.values());
    }

    // ==================== SUBSCRIPTION MANAGEMENT ====================

    /**
     * Subscribe to a topic.
     */
    public void subscribe(String topicName, ISubscriber subscriber) {
        if (!topicExists(topicName)) {
            createTopic(topicName);
        }

        List<ISubscriber> subscribers = subscriptions.get(topicName);
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    /**
     * Unsubscribe from a topic.
     */
    public void unsubscribe(String topicName, ISubscriber subscriber) {
        if (topicExists(topicName)) {
            subscriptions.get(topicName).remove(subscriber);
        }
    }

    /**
     * Unsubscribe from all topics.
     */
    public void unsubscribeAll(ISubscriber subscriber) {
        for (List<ISubscriber> subscribers : subscriptions.values()) {
            subscribers.remove(subscriber);
        }
    }

    /**
     * Get all subscribers for a topic.
     */
    public List<ISubscriber> getSubscribers(String topicName) {
        if (!topicExists(topicName)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(subscriptions.get(topicName));
    }

    /**
     * Get subscriber count for a topic.
     */
    public int getSubscriberCount(String topicName) {
        if (!topicExists(topicName)) {
            return 0;
        }
        return subscriptions.get(topicName).size();
    }

    // ==================== MESSAGE PUBLISHING ====================

    /**
     * Publish a message to a topic.
     */
    public void publish(String topicName, Object payload, String publisherId) {
        if (!topicExists(topicName)) {
            throw new IllegalArgumentException("Topic does not exist: " + topicName);
        }

        Message message = new Message(topicName, payload, publisherId);

        // Store in history if enabled
        if (keepHistory) {
            messageHistory.get(topicName).add(message);
        }

        // Deliver to all subscribers
        List<ISubscriber> subscribers = subscriptions.get(topicName);
        for (ISubscriber subscriber : subscribers) {
            deliveryStrategy.deliver(message, subscriber);
        }
    }

    // ==================== MESSAGE HISTORY ====================

    /**
     * Get message history for a topic.
     */
    public List<Message> getHistory(String topicName) {
        if (!keepHistory) {
            throw new UnsupportedOperationException("History is not enabled");
        }
        if (!topicExists(topicName)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(messageHistory.get(topicName));
    }

    /**
     * Clear message history for a topic.
     */
    public void clearHistory(String topicName) {
        if (keepHistory && messageHistory.containsKey(topicName)) {
            messageHistory.get(topicName).clear();
        }
    }

    // ==================== UTILITY ====================

    /**
     * Set a new delivery strategy.
     */
    public void setDeliveryStrategy(IMessageDeliveryStrategy strategy) {
        if (this.deliveryStrategy != null) {
            this.deliveryStrategy.shutdown();
        }
        this.deliveryStrategy = strategy;
    }

    /**
     * Shutdown the broker and release resources.
     */
    public void shutdown() {
        if (deliveryStrategy != null) {
            deliveryStrategy.shutdown();
        }
        subscriptions.clear();
        topics.clear();
        messageHistory.clear();
    }

    /**
     * Get statistics about the broker.
     */
    public BrokerStats getStats() {
        int totalTopics = topics.size();
        int totalSubscriptions = subscriptions.values().stream()
            .mapToInt(List::size)
            .sum();
        int totalMessages = keepHistory ? messageHistory.values().stream()
            .mapToInt(List::size)
            .sum() : 0;

        return new BrokerStats(totalTopics, totalSubscriptions, totalMessages);
    }

    public static class BrokerStats {
        public final int topicCount;
        public final int subscriptionCount;
        public final int messageCount;

        public BrokerStats(int topicCount, int subscriptionCount, int messageCount) {
            this.topicCount = topicCount;
            this.subscriptionCount = subscriptionCount;
            this.messageCount = messageCount;
        }

        @Override
        public String toString() {
            return String.format("Topics: %d, Subscriptions: %d, Messages: %d",
                topicCount, subscriptionCount, messageCount);
        }
    }
}
