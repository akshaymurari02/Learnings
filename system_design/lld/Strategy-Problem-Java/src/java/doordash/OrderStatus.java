package doordash;

/**
 * Order status lifecycle.
 */
public enum OrderStatus {
    CREATED,        // Order created, not yet confirmed
    CONFIRMED,      // Restaurant confirmed the order
    ASSIGNED,       // Dasher assigned
    PICKED_UP,      // Dasher picked up from restaurant
    IN_TRANSIT,     // On the way to customer
    DELIVERED,      // Successfully delivered
    CANCELLED       // Order cancelled
}
