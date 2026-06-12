package parkinglot;

public interface IParkingService {

    Ticket parkVehicle(Vehicle vehicle);
    boolean unparkVehicle(Ticket ticket);
}
