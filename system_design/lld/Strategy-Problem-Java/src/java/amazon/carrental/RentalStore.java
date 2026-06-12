package amazon.carrental;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RentalStore - a physical location with its own fleet of vehicles.
 */
public class RentalStore {
    private final String id;
    private final String name;
    private final String location;
    private final Map<String, Vehicle> vehicles; // regNumber -> Vehicle

    public RentalStore(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.vehicles = new HashMap<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.put(vehicle.getRegistrationNumber(), vehicle);
    }

    public void removeVehicle(String registrationNumber) {
        vehicles.remove(registrationNumber);
    }

    public Vehicle getVehicle(String registrationNumber) {
        Vehicle v = vehicles.get(registrationNumber);
        if (v == null) throw new RuntimeException("Vehicle not found: " + registrationNumber);
        return v;
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicles.values().stream()
                .filter(Vehicle::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getAvailableByType(VehicleType type) {
        return vehicles.values().stream()
                .filter(v -> v.isAvailable() && v.getType() == type)
                .collect(Collectors.toList());
    }

    public boolean hasAvailableVehicle(VehicleType type) {
        return vehicles.values().stream()
                .anyMatch(v -> v.isAvailable() && v.getType() == type);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }

    @Override
    public String toString() { return name + " (" + location + ")"; }
}

