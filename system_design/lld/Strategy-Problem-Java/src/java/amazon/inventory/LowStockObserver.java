package amazon.inventory;

/**
 * Observer that listens for low stock events and triggers replenishment.
 */
public class LowStockObserver implements IInventoryObserver {

    private final IReplenishmentStrategy defaultStrategy;

    public LowStockObserver(IReplenishmentStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    @Override
    public void onLowStock(Warehouse warehouse, InventoryItem item) {
        System.out.println("⚠ LOW STOCK DETECTED: " + item.getProduct().getName()
                + " at " + warehouse.getName() + " (qty: " + item.getQuantity() + ")");

        // Use product-specific strategy if set, else default
        IReplenishmentStrategy strategy = item.getReplenishmentStrategy();
        if (strategy == null) {
            strategy = defaultStrategy;
        }
        strategy.replenish(warehouse, item);
    }

    @Override
    public void onOutOfStock(Warehouse warehouse, InventoryItem item) {
        System.out.println("🚨 OUT OF STOCK: " + item.getProduct().getName()
                + " at " + warehouse.getName() + " — URGENT replenishment needed!");

        IReplenishmentStrategy strategy = item.getReplenishmentStrategy();
        if (strategy == null) {
            strategy = defaultStrategy;
        }
        strategy.replenish(warehouse, item);
    }
}

