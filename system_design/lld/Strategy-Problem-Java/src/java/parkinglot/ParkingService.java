package parkinglot;

import java.util.List;

/**
 * Parking service that manages vehicle parking across multiple floors.
 * Uses a pluggable parking strategy to determine spot selection.
 */
public class ParkingService implements IParkingService {

    private final List<Floor> floors;
    private final IParkingStrategy parkingStrategy;

    public ParkingService(List<Floor> floors) {
        this(floors, new FirstAvailableStrategy());
    }

    public ParkingService(List<Floor> floors, IParkingStrategy parkingStrategy) {
        this.floors = floors;
        this.parkingStrategy = parkingStrategy;
    }

    @Override
    public Ticket parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors) {
            ParkingSpot spot = parkingStrategy.findSpot(vehicle, floor);
            if (spot != null) {
                return floor.parkVehicle(vehicle, spot);
            }
        }
        return null;
    }

    @Override
    public boolean unparkVehicle(Ticket ticket) {
        for (Floor floor : floors) {
            if (floor.unparkVehicle(ticket)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get total available spots across all floors.
     */
    public int getTotalAvailableSpots() {
        return floors.stream()
            .mapToInt(Floor::getAvailableSpotCount)
            .sum();
    }

    /**
     * Get available spots of a specific type across all floors.
     */
    public int getAvailableSpots(SpotType type) {
        return floors.stream()
            .mapToInt(f -> f.getAvailableSpotCount(type))
            .sum();
    }

    /**
     * Get the number of floors in the parking lot.
     */
    public int getFloorCount() {
        return floors.size();
    }
}
