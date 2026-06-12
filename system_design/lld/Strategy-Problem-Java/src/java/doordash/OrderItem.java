package doordash;

/**
 * Represents an item in an order with quantity.
 */
public class OrderItem {

    private final MenuItem menuItem;
    private int quantity;
    private String specialInstructions;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%dx %s - $%.2f", quantity, menuItem.getName(), getTotalPrice());
    }
}
