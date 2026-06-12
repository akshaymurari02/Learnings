package parkinglot;

/**
 * Strategy interface for finding parking spots.
 * Different strategies can implement different parking algorithms.
 */
public interface IParkingStrategy {

    /**
     * Find a suitable parking spot for the given vehicle.
     * @param vehicle The vehicle to park
     * @param floor The floor to search
     * @return A suitable parking spot, or null if none available
     */
    ParkingSpot findSpot(Vehicle vehicle, Floor floor);
}
