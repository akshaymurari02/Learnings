package amazon.inventory;

/**
 * Strategy interface for replenishment.
 */
public interface IReplenishmentStrategy {
    void replenish(Warehouse warehouse, InventoryItem item);
    String getName();
}

