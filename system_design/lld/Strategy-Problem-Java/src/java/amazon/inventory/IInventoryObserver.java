package amazon.inventory;

/**
 * Observer interface for inventory events.
 */
public interface IInventoryObserver {
    void onLowStock(Warehouse warehouse, InventoryItem item);
    void onOutOfStock(Warehouse warehouse, InventoryItem item);
}

