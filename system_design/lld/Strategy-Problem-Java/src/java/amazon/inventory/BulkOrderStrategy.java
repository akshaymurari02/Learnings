package amazon.inventory;

/**
 * Bulk Order Strategy - orders large quantities at once (cheaper per unit, slower).
 * Good for predictable, steady-demand products.
 */
public class BulkOrderStrategy implements IReplenishmentStrategy {

    private final int bulkQuantity;

    public BulkOrderStrategy(int bulkQuantity) {
        this.bulkQuantity = bulkQuantity;
    }

    @Override
    public void replenish(Warehouse warehouse, InventoryItem item) {
        System.out.println("[BULK ORDER] Ordering " + bulkQuantity + "x " + item.getProduct().getName()
                + " for warehouse " + warehouse.getName());
        item.addStock(bulkQuantity);
        System.out.println("  → Stock replenished to " + item.getQuantity());
    }

    @Override
    public String getName() {
        return "BulkOrder(" + bulkQuantity + ")";
    }
}

