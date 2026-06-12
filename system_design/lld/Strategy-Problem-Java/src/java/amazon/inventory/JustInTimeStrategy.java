package amazon.inventory;

/**
 * Just-In-Time Strategy - orders small quantities immediately when stock is low.
 * Good for expensive/perishable products or unpredictable demand.
 */
public class JustInTimeStrategy implements IReplenishmentStrategy {

    private final int targetStock;

    public JustInTimeStrategy(int targetStock) {
        this.targetStock = targetStock;
    }

    @Override
    public void replenish(Warehouse warehouse, InventoryItem item) {
        int needed = targetStock - item.getQuantity();
        if (needed <= 0) return;

        System.out.println("[JIT] Ordering " + needed + "x " + item.getProduct().getName()
                + " for warehouse " + warehouse.getName() + " (target: " + targetStock + ")");
        item.addStock(needed);
        System.out.println("  → Stock replenished to " + item.getQuantity());
    }

    @Override
    public String getName() {
        return "JustInTime(target=" + targetStock + ")";
    }
}

