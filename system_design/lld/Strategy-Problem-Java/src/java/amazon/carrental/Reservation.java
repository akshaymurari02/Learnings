package amazon.carrental;

import java.time.LocalDate;

public class Reservation {
    private final String id;
    private final Customer customer;
    private final Vehicle vehicle;
    private final RentalStore store;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private LocalDate actualReturnDate;
    private ReservationStatus status;
    private double totalAmount;

    public Reservation(String id, Customer customer, Vehicle vehicle, RentalStore store,
                       LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.customer = customer;
        this.vehicle = vehicle;
        this.store = store;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ReservationStatus.ACTIVE;
        this.totalAmount = calculateAmount(startDate, endDate);
    }

    private double calculateAmount(LocalDate from, LocalDate to) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(from, to);
        if (days <= 0) days = 1;
        return days * vehicle.getDailyRate();
    }

    public void completeReturn(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
        this.status = ReservationStatus.COMPLETED;
        // Recalculate if returned late
        if (returnDate.isAfter(endDate)) {
            long extraDays = java.time.temporal.ChronoUnit.DAYS.between(endDate, returnDate);
            double penalty = extraDays * vehicle.getDailyRate() * 1.5; // 1.5x late fee
            this.totalAmount += penalty;
        }
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public RentalStore getStore() { return store; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public ReservationStatus getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Reservation[" + id + "] " + customer.getName() + " → " + vehicle.getMake() + " " + vehicle.getModel()
                + " | " + startDate + " to " + endDate + " | $" + String.format("%.2f", totalAmount) + " | " + status;
    }
}

