package doordash;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a food delivery order.
 */
public class Order {

    private final String id;
    private final Customer customer;
    private final Restaurant restaurant;
    private final List<OrderItem> items;
    private final LocalDateTime orderTime;
    private OrderStatus status;
    private Dasher assignedDasher;
    private double subtotal;
    private double deliveryFee;
    private double tax;
    private double tip;
    private LocalDateTime estimatedDeliveryTime;

    public Order(Customer customer, Restaurant restaurant) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        this.deliveryFee = 5.99;
        this.tax = 0.0;
        this.tip = 0.0;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        OrderItem orderItem = new OrderItem(menuItem, quantity);
        items.add(orderItem);
        calculateTotals();
    }

    public void removeItem(String menuItemId) {
        items.removeIf(item -> item.getMenuItem().getId().equals(menuItemId));
        calculateTotals();
    }

    private void calculateTotals() {
        subtotal = items.stream()
            .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
            .sum();
        tax = subtotal * 0.08; // 8% tax
    }

    public double getTotal() {
        return subtotal + deliveryFee + tax + tip;
    }

    public void assignDasher(Dasher dasher) {
        this.assignedDasher = dasher;
        this.status = OrderStatus.ASSIGNED;
        // Estimate 30-45 minutes delivery
        this.estimatedDeliveryTime = LocalDateTime.now().plusMinutes(30);
    }

    public void markPickedUp() {
        this.status = OrderStatus.PICKED_UP;
    }

    public void markDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Dasher getAssignedDasher() {
        return assignedDasher;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTax() {
        return tax;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%s, restaurant=%s, status=%s, total=$%.2f]",
            id, restaurant.getName(), status, getTotal());
    }
}
