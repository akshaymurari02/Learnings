package amazon.vendingmachine;

public class Slot {
    private final String code; // e.g., "A1", "B2"
    private Product product;
    private int quantity;

    public Slot(String code) {
        this.code = code;
    }

    public void load(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return product != null && quantity > 0;
    }

    public boolean isEmpty() {
        return quantity == 0;
    }

    public void dispense() {
        if (quantity <= 0) throw new RuntimeException("Slot " + code + " is empty");
        quantity--;
    }

    public String getCode() { return code; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        if (product == null) return "[" + code + "] Empty";
        return "[" + code + "] " + product + " x" + quantity;
    }
}

