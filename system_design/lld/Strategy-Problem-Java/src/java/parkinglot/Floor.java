package parkinglot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a floor in the parking lot with multiple parking spots.
 */
public class Floor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;
    private final Map<String, Ticket> activeTickets; // ticketId -> Ticket

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        this.activeTickets = new HashMap<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public void addSpots(int smallSpots, int mediumSpots, int largeSpots) {
        int spotCounter = 1;
        for (int i = 0; i < smallSpots; i++) {
            spots.add(new ParkingSpot(
                String.format("F%d-S%03d", floorNumber, spotCounter++),
                SpotType.SMALL, floorNumber));
        }
        for (int i = 0; i < mediumSpots; i++) {
            spots.add(new ParkingSpot(
                String.format("F%d-M%03d", floorNumber, spotCounter++),
                SpotType.MEDIUM, floorNumber));
        }
        for (int i = 0; i < largeSpots; i++) {
            spots.add(new ParkingSpot(
                String.format("F%d-L%03d", floorNumber, spotCounter++),
                SpotType.LARGE, floorNumber));
        }
    }

    public boolean canParkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                return true;
            }
        }
        return false;
    }

    public Ticket parkVehicle(Vehicle vehicle, ParkingSpot spot) {
        if (!spot.canFitVehicle(vehicle)) {
            return null;
        }

        spot.parkVehicle(vehicle);
        String ticketId = UUID.randomUUID().toString();
        Ticket ticket = new Ticket(ticketId, vehicle, spot);
        activeTickets.put(ticketId, ticket);
        return ticket;
    }

    public boolean unparkVehicle(Ticket ticket) {
        if (!activeTickets.containsKey(ticket.getTicketId())) {
            return false;
        }

        ParkingSpot spot = ticket.getSpot();
        spot.removeVehicle();
        ticket.markExit();
        activeTickets.remove(ticket.getTicketId());
        return true;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public int getAvailableSpotCount() {
        return (int) spots.stream().filter(ParkingSpot::isAvailable).count();
    }

    public int getAvailableSpotCount(SpotType type) {
        return (int) spots.stream()
            .filter(s -> s.isAvailable() && s.getSpotType() == type)
            .count();
    }
}
