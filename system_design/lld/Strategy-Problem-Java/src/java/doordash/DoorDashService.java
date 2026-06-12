package doordash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main DoorDash service managing restaurants, customers, dashers, and orders.
 */
public class DoorDashService {

    private final Map<String, Customer> customers;
    private final Map<String, Restaurant> restaurants;
    private final Map<String, Dasher> dashers;
    private final Map<String, Order> orders;
    private final IDasherAssignmentStrategy assignmentStrategy;

    public DoorDashService() {
        this(new NearestDasherStrategy());
    }

    public DoorDashService(IDasherAssignmentStrategy assignmentStrategy) {
        this.customers = new HashMap<>();
        this.restaurants = new HashMap<>();
        this.dashers = new HashMap<>();
        this.orders = new HashMap<>();
        this.assignmentStrategy = assignmentStrategy;
    }

    // ==================== CUSTOMER MANAGEMENT ====================

    public void registerCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        System.out.println("Customer registered: " + customer.getName());
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    // ==================== RESTAURANT MANAGEMENT ====================

    public void registerRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
        System.out.println("Restaurant registered: " + restaurant.getName());
    }

    public Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    public List<Restaurant> searchRestaurants(String query) {
        return restaurants.values().stream()
            .filter(r -> r.getName().toLowerCase().contains(query.toLowerCase()) ||
                        r.getCuisine().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Restaurant> getNearbyRestaurants(Address customerAddress, double radiusKm) {
        return restaurants.values().stream()
            .filter(r -> r.getAddress().distanceTo(customerAddress) <= radiusKm)
            .sorted((r1, r2) -> Double.compare(
                r1.getAddress().distanceTo(customerAddress),
                r2.getAddress().distanceTo(customerAddress)
            ))
            .collect(Collectors.toList());
    }

    // ==================== DASHER MANAGEMENT ====================

    public void registerDasher(Dasher dasher) {
        dashers.put(dasher.getId(), dasher);
        System.out.println("Dasher registered: " + dasher.getName());
    }

    public Dasher getDasher(String dasherId) {
        return dashers.get(dasherId);
    }

    public List<Dasher> getAvailableDashers() {
        return dashers.values().stream()
            .filter(Dasher::isAvailable)
            .collect(Collectors.toList());
    }

    public void setDasherStatus(String dasherId, DasherStatus status) {
        Dasher dasher = dashers.get(dasherId);
        if (dasher != null) {
            dasher.setStatus(status);
            System.out.println("Dasher " + dasher.getName() + " status: " + status);
        }
    }

    // ==================== ORDER MANAGEMENT ====================

    public Order createOrder(String customerId, String restaurantId) {
        Customer customer = customers.get(customerId);
        Restaurant restaurant = restaurants.get(restaurantId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found: " + restaurantId);
        }
        if (!restaurant.isOpen()) {
            throw new IllegalStateException("Restaurant is closed: " + restaurant.getName());
        }

        Order order = new Order(customer, restaurant);
        orders.put(order.getId(), order);
        System.out.println("Order created: " + order.getId());
        return order;
    }

    public void addItemToOrder(String orderId, String menuItemId, int quantity) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Cannot modify order in status: " + order.getStatus());
        }

        MenuItem menuItem = order.getRestaurant().getMenuItem(menuItemId);
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item not found: " + menuItemId);
        }
        if (!menuItem.isAvailable()) {
            throw new IllegalStateException("Menu item not available: " + menuItem.getName());
        }

        order.addItem(menuItem, quantity);
        System.out.println("Added " + quantity + "x " + menuItem.getName() + " to order " + orderId);
    }

    public void placeOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place empty order");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        System.out.println("Order confirmed: " + order.getId());

        // Assign a dasher
        assignDasher(orderId);
    }

    private void assignDasher(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return;
        }

        List<Dasher> availableDashers = getAvailableDashers();
        if (availableDashers.isEmpty()) {
            System.out.println("No dashers available for order: " + orderId);
            return;
        }

        Dasher selectedDasher = assignmentStrategy.assignDasher(availableDashers, order);
        if (selectedDasher != null) {
            selectedDasher.assignOrder(order);
            order.assignDasher(selectedDasher);
            System.out.println("Dasher " + selectedDasher.getName() + " assigned to order " + orderId);
        }
    }

    public void markOrderPickedUp(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }

        order.markPickedUp();
        if (order.getAssignedDasher() != null) {
            order.getAssignedDasher().pickupOrder();
        }
        System.out.println("Order picked up: " + orderId);
    }

    public void markOrderDelivered(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }

        order.markDelivered();
        if (order.getAssignedDasher() != null) {
            order.getAssignedDasher().completeDelivery();
        }
        System.out.println("Order delivered: " + orderId);
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }

        order.cancel();
        if (order.getAssignedDasher() != null) {
            order.getAssignedDasher().completeDelivery();
        }
        System.out.println("Order cancelled: " + orderId);
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getCustomerOrders(String customerId) {
        return orders.values().stream()
            .filter(o -> o.getCustomer().getId().equals(customerId))
            .collect(Collectors.toList());
    }

    public List<Order> getActiveOrders() {
        return orders.values().stream()
            .filter(o -> o.getStatus() != OrderStatus.DELIVERED &&
                        o.getStatus() != OrderStatus.CANCELLED)
            .collect(Collectors.toList());
    }

    // ==================== STATISTICS ====================

    public DoorDashStats getStats() {
        int totalOrders = orders.size();
        int activeOrders = (int) orders.values().stream()
            .filter(o -> o.getStatus() != OrderStatus.DELIVERED &&
                        o.getStatus() != OrderStatus.CANCELLED)
            .count();
        int deliveredOrders = (int) orders.values().stream()
            .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
            .count();
        int availableDashers = (int) dashers.values().stream()
            .filter(Dasher::isAvailable)
            .count();
        int openRestaurants = (int) restaurants.values().stream()
            .filter(Restaurant::isOpen)
            .count();

        return new DoorDashStats(
            customers.size(),
            restaurants.size(),
            openRestaurants,
            dashers.size(),
            availableDashers,
            totalOrders,
            activeOrders,
            deliveredOrders
        );
    }

    public void displayStats() {
        System.out.println("\n=== DoorDash System Statistics ===");
        System.out.println(getStats());
    }

    // ==================== STATS CLASS ====================

    public static class DoorDashStats {
        public final int totalCustomers;
        public final int totalRestaurants;
        public final int openRestaurants;
        public final int totalDashers;
        public final int availableDashers;
        public final int totalOrders;
        public final int activeOrders;
        public final int deliveredOrders;

        public DoorDashStats(int totalCustomers, int totalRestaurants, int openRestaurants,
                           int totalDashers, int availableDashers, int totalOrders,
                           int activeOrders, int deliveredOrders) {
            this.totalCustomers = totalCustomers;
            this.totalRestaurants = totalRestaurants;
            this.openRestaurants = openRestaurants;
            this.totalDashers = totalDashers;
            this.availableDashers = availableDashers;
            this.totalOrders = totalOrders;
            this.activeOrders = activeOrders;
            this.deliveredOrders = deliveredOrders;
        }

        @Override
        public String toString() {
            return String.format(
                "Customers: %d | Restaurants: %d (Open: %d) | Dashers: %d (Available: %d)\n" +
                "Orders: %d (Active: %d, Delivered: %d)",
                totalCustomers, totalRestaurants, openRestaurants, totalDashers, availableDashers,
                totalOrders, activeOrders, deliveredOrders
            );
        }
    }
}
