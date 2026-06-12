package amazon.carrental;

public class Vehicle {
    private final String registrationNumber;
    private final String make;
    private final String model;
    private final VehicleType type;
    private final double dailyRate;
    private VehicleStatus status;

    public Vehicle(String registrationNumber, String make, String model, VehicleType type, double dailyRate) {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.type = type;
        this.dailyRate = dailyRate;
        this.status = VehicleStatus.AVAILABLE;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public VehicleType getType() { return type; }
    public double getDailyRate() { return dailyRate; }
    public VehicleStatus getStatus() { return status; }

    public void setStatus(VehicleStatus status) { this.status = status; }

    public boolean isAvailable() { return status == VehicleStatus.AVAILABLE; }

    @Override
    public String toString() {
        return make + " " + model + " [" + registrationNumber + "] " + type + " $" + dailyRate + "/day (" + status + ")";
    }
}

