package doordash;

import java.util.List;

/**
 * Assigns the nearest available dasher to the restaurant.
 */
public class NearestDasherStrategy implements IDasherAssignmentStrategy {

    @Override
    public Dasher assignDasher(List<Dasher> availableDashers, Order order) {
        if (availableDashers.isEmpty()) {
            return null;
        }

        Address restaurantAddress = order.getRestaurant().getAddress();
        Dasher nearestDasher = null;
        double minDistance = Double.MAX_VALUE;

        for (Dasher dasher : availableDashers) {
            if (dasher.isAvailable()) {
                double distance = dasher.getCurrentLocation().distanceTo(restaurantAddress);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestDasher = dasher;
                }
            }
        }

        return nearestDasher;
    }
}
