package amazon.carrental;

public class Customer {
    private final String id;
    private final String name;
    private final String licenseNumber;

    public Customer(String id, String name, String licenseNumber) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLicenseNumber() { return licenseNumber; }

    @Override
    public String toString() { return name + " (License: " + licenseNumber + ")"; }
}

