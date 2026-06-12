package doordash;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant in the DoorDash system.
 */
public class Restaurant {

    private final String id;
    private final String name;
    private final Address address;
    private final String cuisine;
    private final List<MenuItem> menu;
    private boolean isOpen;
    private double rating;

    public Restaurant(String id, String name, Address address, String cuisine) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisine = cuisine;
        this.menu = new ArrayList<>();
        this.isOpen = true;
        this.rating = 0.0;
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    public void removeMenuItem(String itemId) {
        menu.removeIf(item -> item.getId().equals(itemId));
    }

    public MenuItem getMenuItem(String itemId) {
        return menu.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .orElse(null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getCuisine() {
        return cuisine;
    }

    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("Restaurant[id=%s, name=%s, cuisine=%s, rating=%.1f]",
            id, name, cuisine, rating);
    }
}
