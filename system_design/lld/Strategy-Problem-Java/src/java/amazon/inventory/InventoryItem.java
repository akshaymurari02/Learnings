package amazon.inventory;

public class InventoryItem {
    private final String id;
    private final Product product;
    private int quantity;
    private final int lowStockThreshold;
    private IReplenishmentStrategy replenishmentStrategy;

    public InventoryItem(String id, Product product, int quantity, int lowStockThreshold) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    public InventoryItem(String id, Product product, int quantity) {
        this(id, product, quantity, 5);
    }

    public String getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public boolean isLowStock() {
        return quantity > 0 && quantity <= lowStockThreshold;
    }

    public boolean isOutOfStock() {
        return quantity == 0;
    }

    public void addStock(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.quantity += amount;
    }

    public void removeStock(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > quantity) throw new RuntimeException("Insufficient stock for " + product.getName());
        this.quantity -= amount;
    }

    public IReplenishmentStrategy getReplenishmentStrategy() {
        return replenishmentStrategy;
    }

    public void setReplenishmentStrategy(IReplenishmentStrategy strategy) {
        this.replenishmentStrategy = strategy;
    }

    @Override
    public String toString() {
        return product.toString() + " | Qty: " + quantity
                + (isLowStock() ? " [LOW]" : "")
                + (isOutOfStock() ? " [OUT]" : "")
                + (replenishmentStrategy != null ? " | Strategy: " + replenishmentStrategy.getName() : "");
    }
}
