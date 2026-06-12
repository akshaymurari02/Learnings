package doordash;

/**
 * Represents a delivery driver (Dasher).
 */
public class Dasher {

    private final String id;
    private final String name;
    private final String phone;
    private final String vehicleType;
    private Address currentLocation;
    private DasherStatus status;
    private Order currentOrder;
    private double rating;

    public Dasher(String id, String name, String phone, String vehicleType, Address location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.currentLocation = location;
        this.status = DasherStatus.AVAILABLE;
        this.rating = 5.0;
    }

    public boolean isAvailable() {
        return status == DasherStatus.AVAILABLE;
    }

    public void assignOrder(Order order) {
        this.currentOrder = order;
        this.status = DasherStatus.ASSIGNED;
    }

    public void pickupOrder() {
        this.status = DasherStatus.PICKED_UP;
    }

    public void completeDelivery() {
        this.currentOrder = null;
        this.status = DasherStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Address getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Address currentLocation) {
        this.currentLocation = currentLocation;
    }

    public DasherStatus getStatus() {
        return status;
    }

    public void setStatus(DasherStatus status) {
        this.status = status;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("Dasher[id=%s, name=%s, status=%s, rating=%.1f]",
            id, name, status, rating);
    }
}
