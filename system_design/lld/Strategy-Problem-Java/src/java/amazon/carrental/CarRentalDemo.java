package amazon.carrental;

import java.time.LocalDate;
import java.util.List;

public class CarRentalDemo {

    public static void main(String[] args) {

        // 1. Choose payment gateway (Strategy)
        IPaymentGateway paymentGateway = new CreditCardPayment();
        CarRentalService service = new CarRentalService(paymentGateway);

        // 2. Setup stores
        RentalStore downtown = new RentalStore("S1", "Downtown Hub", "Manhattan, NY");
        RentalStore airport = new RentalStore("S2", "Airport Branch", "JFK Airport, NY");
        service.addStore(downtown);
        service.addStore(airport);

        // 3. Add vehicles to stores
        service.addVehicleToStore("S1", new Vehicle("NY-1001", "Toyota", "Camry", VehicleType.SEDAN, 45.0));
        service.addVehicleToStore("S1", new Vehicle("NY-1002", "Honda", "CR-V", VehicleType.SUV, 65.0));
        service.addVehicleToStore("S1", new Vehicle("NY-1003", "BMW", "7 Series", VehicleType.LUXURY, 150.0));
        service.addVehicleToStore("S2", new Vehicle("NY-2001", "Ford", "Explorer", VehicleType.SUV, 70.0));
        service.addVehicleToStore("S2", new Vehicle("NY-2002", "Hyundai", "i20", VehicleType.HATCHBACK, 30.0));

        // 4. Search available SUVs at downtown
        System.out.println("\n=== Available SUVs at Downtown ===");
        List<Vehicle> suvs = service.searchAvailableVehicles("S1", VehicleType.SUV);
        suvs.forEach(System.out::println);

        // 5. Customer rents a vehicle
        Customer alice = new Customer("C1", "Alice", "DL-12345");
        Customer bob = new Customer("C2", "Bob", "DL-67890");

        System.out.println("\n=== RENTING ===");
        Reservation r1 = service.rentVehicle(alice, "S1", "NY-1002",
                LocalDate.now(), LocalDate.now().plusDays(3));

        Reservation r2 = service.rentVehicle(bob, "S2", "NY-2001",
                LocalDate.now(), LocalDate.now().plusDays(5));

        // 6. Check available vehicles (SUV should be gone)
        System.out.println("\n=== Available SUVs at Downtown (after rent) ===");
        service.searchAvailableVehicles("S1", VehicleType.SUV).forEach(System.out::println);
        System.out.println("(none)");

        // 7. Return on time
        System.out.println("\n=== RETURNING (on time) ===");
        service.returnVehicle(r1.getId(), LocalDate.now().plusDays(3));

        // 8. Return late (2 days late)
        System.out.println("\n=== RETURNING (late) ===");
        service.returnVehicle(r2.getId(), LocalDate.now().plusDays(7));

        // 9. Rent and cancel
        System.out.println("\n=== RENT & CANCEL ===");
        Reservation r3 = service.rentVehicle(alice, "S1", "NY-1003",
                LocalDate.now(), LocalDate.now().plusDays(2));
        service.cancelReservation(r3.getId());

        // 10. Active reservations
        System.out.println("\n=== Active Reservations ===");
        List<Reservation> active = service.getActiveReservations();
        if (active.isEmpty()) System.out.println("No active reservations");
        else active.forEach(System.out::println);
    }
}

