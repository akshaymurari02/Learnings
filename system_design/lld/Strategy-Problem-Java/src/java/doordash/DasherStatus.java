package doordash;

/**
 * Status of a dasher.
 */
public enum DasherStatus {
    AVAILABLE,      // Ready to accept orders
    ASSIGNED,       // Order assigned, heading to restaurant
    PICKED_UP,      // Order picked up, heading to customer
    OFFLINE         // Not accepting orders
}
