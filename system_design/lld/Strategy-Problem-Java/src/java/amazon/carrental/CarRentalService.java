package amazon.carrental;

import java.time.LocalDate;
import java.util.*;

/**
 * CarRentalService - main service managing stores, reservations, payments.
 */
public class CarRentalService {

    private final Map<String, RentalStore> stores;
    private final Map<String, Reservation> reservations;
    private final IPaymentGateway paymentGateway;
    private int reservationCounter = 0;

    public CarRentalService(IPaymentGateway paymentGateway) {
        this.stores = new HashMap<>();
        this.reservations = new HashMap<>();
        this.paymentGateway = paymentGateway;
    }

    // ==================== STORE MANAGEMENT ====================

    public void addStore(RentalStore store) {
        stores.put(store.getId(), store);
    }

    public RentalStore getStore(String storeId) {
        RentalStore store = stores.get(storeId);
        if (store == null) throw new RuntimeException("Store not found: " + storeId);
        return store;
    }

    public List<RentalStore> getAllStores() {
        return new ArrayList<>(stores.values());
    }

    // ==================== VEHICLE MANAGEMENT ====================

    public void addVehicleToStore(String storeId, Vehicle vehicle) {
        RentalStore store = getStore(storeId);
        store.addVehicle(vehicle);
        System.out.println("Added " + vehicle + " to " + store.getName());
    }

    public List<Vehicle> searchAvailableVehicles(String storeId, VehicleType type) {
        return getStore(storeId).getAvailableByType(type);
    }

    // ==================== RENT ====================

    public Reservation rentVehicle(Customer customer, String storeId, String registrationNumber,
                                    LocalDate startDate, LocalDate endDate) {
        RentalStore store = getStore(storeId);
        Vehicle vehicle = store.getVehicle(registrationNumber);

        if (!vehicle.isAvailable()) {
            throw new RuntimeException("Vehicle " + registrationNumber + " is not available");
        }

        // Create reservation
        String reservationId = "RES-" + (++reservationCounter);
        Reservation reservation = new Reservation(reservationId, customer, vehicle, store, startDate, endDate);

        // Process payment
        boolean paid = paymentGateway.processPayment(customer, reservation.getTotalAmount());
        if (!paid) {
            throw new RuntimeException("Payment failed for " + customer.getName());
        }

        // Mark vehicle as rented
        vehicle.setStatus(VehicleStatus.RENTED);
        reservations.put(reservationId, reservation);

        System.out.println("✅ Vehicle rented: " + reservation);
        return reservation;
    }

    // ==================== RETURN ====================

    public Reservation returnVehicle(String reservationId, LocalDate returnDate) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found: " + reservationId);
        }
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new RuntimeException("Reservation is not active: " + reservationId);
        }

        double originalAmount = reservation.getTotalAmount();
        reservation.completeReturn(returnDate);
        double finalAmount = reservation.getTotalAmount();

        // Charge extra if late
        if (finalAmount > originalAmount) {
            double extra = finalAmount - originalAmount;
            System.out.println("⚠ Late return! Extra charge: $" + String.format("%.2f", extra));
            paymentGateway.processPayment(reservation.getCustomer(), extra);
        }

        // Mark vehicle available
        reservation.getVehicle().setStatus(VehicleStatus.AVAILABLE);

        System.out.println("✅ Vehicle returned: " + reservation);
        return reservation;
    }

    // ==================== CANCEL ====================

    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found: " + reservationId);
        }

        reservation.cancel();
        reservation.getVehicle().setStatus(VehicleStatus.AVAILABLE);

        // Refund
        paymentGateway.processRefund(reservation.getCustomer(), reservation.getTotalAmount());
        System.out.println("❌ Reservation cancelled: " + reservationId);
    }

    // ==================== QUERIES ====================

    public List<Reservation> getActiveReservations() {
        List<Reservation> active = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getStatus() == ReservationStatus.ACTIVE) active.add(r);
        }
        return active;
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }
}

