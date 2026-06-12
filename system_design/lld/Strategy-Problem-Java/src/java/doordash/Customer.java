package doordash;

import java.time.LocalDateTime;

/**
 * Represents a customer in the DoorDash system.
 */
public class Customer {

    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private Address address;

    public Customer(String id, String name, String email, String phone, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%s, name=%s]", id, name);
    }
}
