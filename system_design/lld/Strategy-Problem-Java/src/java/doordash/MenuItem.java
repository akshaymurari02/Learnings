package doordash;

/**
 * Represents a menu item at a restaurant.
 */
public class MenuItem {

    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final String category;
    private boolean available;

    public MenuItem(String id, String name, String description, double price, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s)", name, price, category);
    }
}
