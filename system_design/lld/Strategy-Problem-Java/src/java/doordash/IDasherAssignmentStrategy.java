package doordash;

import java.util.List;

/**
 * Strategy interface for dasher assignment algorithms.
 */
public interface IDasherAssignmentStrategy {

    /**
     * Select the best dasher for an order.
     * @param availableDashers List of available dashers
     * @param order The order to assign
     * @return Selected dasher, or null if none available
     */
    Dasher assignDasher(List<Dasher> availableDashers, Order order);
}
