package pubsub;

import java.util.ArrayList;
import java.util.List;

/**
 * High-level Pub-Sub Service that provides a simplified API.
 * Wraps MessageBroker and provides convenient methods.
 */
public class PubSubService {

    private final MessageBroker broker;
    private final List<Publisher> publishers;
    private final List<ISubscriber> subscribers;

    public PubSubService() {
        this(new SyncDeliveryStrategy(), false);
    }

    public PubSubService(IMessageDeliveryStrategy deliveryStrategy) {
        this(deliveryStrategy, false);
    }

    public PubSubService(IMessageDeliveryStrategy deliveryStrategy, boolean keepHistory) {
        this.broker = MessageBroker.create(deliveryStrategy, keepHistory);
        this.publishers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    // ==================== TOPIC MANAGEMENT ====================

    public void createTopic(String topicName) {
        broker.createTopic(topicName);
    }

    public void createTopic(String topicName, String description) {
        broker.createTopic(new Topic(topicName, description));
    }

    public void deleteTopic(String topicName) {
        broker.deleteTopic(topicName);
    }

    public List<Topic> getAllTopics() {
        return broker.getAllTopics();
    }

    // ==================== PUBLISHER MANAGEMENT ====================

    public Publisher createPublisher(String name) {
        Publisher publisher = new Publisher(name, broker);
        publishers.add(publisher);
        return publisher;
    }

    public void removePublisher(Publisher publisher) {
        publishers.remove(publisher);
    }

    public List<Publisher> getAllPublishers() {
        return new ArrayList<>(publishers);
    }

    // ==================== SUBSCRIBER MANAGEMENT ====================

    public ISubscriber createSubscriber(String name, CallbackSubscriber.MessageHandler handler) {
        ISubscriber subscriber = new CallbackSubscriber(name, handler);
        subscribers.add(subscriber);
        return subscriber;
    }

    public void addSubscriber(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(ISubscriber subscriber) {
        broker.unsubscribeAll(subscriber);
        subscribers.remove(subscriber);
    }

    public List<ISubscriber> getAllSubscribers() {
        return new ArrayList<>(subscribers);
    }

    // ==================== SUBSCRIPTION MANAGEMENT ====================

    public void subscribe(String topicName, ISubscriber subscriber) {
        broker.subscribe(topicName, subscriber);
    }

    public void unsubscribe(String topicName, ISubscriber subscriber) {
        broker.unsubscribe(topicName, subscriber);
    }

    public List<ISubscriber> getSubscribers(String topicName) {
        return broker.getSubscribers(topicName);
    }

    public int getSubscriberCount(String topicName) {
        return broker.getSubscriberCount(topicName);
    }

    // ==================== MESSAGING ====================

    public void publish(String topicName, Object payload, String publisherName) {
        broker.publish(topicName, payload, publisherName);
    }

    public List<Message> getHistory(String topicName) {
        return broker.getHistory(topicName);
    }

    // ==================== UTILITY ====================

    public MessageBroker.BrokerStats getStats() {
        return broker.getStats();
    }

    public void shutdown() {
        broker.shutdown();
        publishers.clear();
        subscribers.clear();
    }

    public MessageBroker getBroker() {
        return broker;
    }
}
