package parkinglot;

/**
 * Strategy that finds the smallest spot that can fit the vehicle.
 * Optimizes space utilization by not wasting large spots on small vehicles.
 */
public class BestFitStrategy implements IParkingStrategy {

    @Override
    public ParkingSpot findSpot(Vehicle vehicle, Floor floor) {
        ParkingSpot bestSpot = null;
        int minCapacity = Integer.MAX_VALUE;

        for (ParkingSpot spot : floor.getSpots()) {
            if (spot.canFitVehicle(vehicle)) {
                int capacity = spot.getSpotType().getCapacity();
                if (capacity < minCapacity) {
                    minCapacity = capacity;
                    bestSpot = spot;
                }
            }
        }
        return bestSpot;
    }
}
