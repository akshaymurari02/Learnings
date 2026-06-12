package parkinglot;

import java.time.LocalDateTime;

/**
 * Represents a parking ticket issued when a vehicle is parked.
 */
public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void markExit() {
        this.exitTime = LocalDateTime.now();
    }

    public int getFloorNumber() {
        return spot.getFloorNumber();
    }

    public String getSpotId() {
        return spot.getSpotId();
    }
}
