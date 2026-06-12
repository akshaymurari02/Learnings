package amazon.inventory;

import java.util.*;

/**
 * InventoryManager - central service that:
 * 1. Manages warehouses
 * 2. Handles sell/restock operations
 * 3. Acts as NOTIFIER (Subject in Observer pattern) for inventory events
 *
 * Observers register to receive low-stock / out-of-stock notifications.
 */
public class InventoryManager {

    private final Map<String, Warehouse> warehouses;
    private final List<IInventoryObserver> observers;

    public InventoryManager() {
        this.warehouses = new HashMap<>();
        this.observers = new ArrayList<>();
    }

    // ==================== OBSERVER PATTERN ====================

    public void registerObserver(IInventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IInventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyLowStock(Warehouse warehouse, InventoryItem item) {
        for (IInventoryObserver observer : observers) {
            observer.onLowStock(warehouse, item);
        }
    }

    private void notifyOutOfStock(Warehouse warehouse, InventoryItem item) {
        for (IInventoryObserver observer : observers) {
            observer.onOutOfStock(warehouse, item);
        }
    }

    // ==================== WAREHOUSE MANAGEMENT ====================

    public void addWarehouse(Warehouse warehouse) {
        warehouses.put(warehouse.getId(), warehouse);
    }

    public Warehouse getWarehouse(String warehouseId) {
        Warehouse wh = warehouses.get(warehouseId);
        if (wh == null) throw new RuntimeException("Warehouse not found: " + warehouseId);
        return wh;
    }

    public List<Warehouse> getAllWarehouses() {
        return new ArrayList<>(warehouses.values());
    }

    // ==================== INVENTORY OPERATIONS ====================

    public void addItem(String warehouseId, InventoryItem item) {
        Warehouse warehouse = getWarehouse(warehouseId);
        warehouse.addItem(item);
        System.out.println("Added " + item.getProduct().getName() + " (qty: " + item.getQuantity() + ") to " + warehouse.getName());
    }

    public void sell(String warehouseId, String itemId, int quantity) {
        Warehouse warehouse = getWarehouse(warehouseId);
        InventoryItem item = warehouse.getItem(itemId);

        item.removeStock(quantity);
        System.out.println("Sold " + quantity + "x " + item.getProduct().getName() + " from " + warehouse.getName());

        // Notify observers based on stock level
        if (item.isOutOfStock()) {
            notifyOutOfStock(warehouse, item);
        } else if (item.isLowStock()) {
            notifyLowStock(warehouse, item);
        }
    }

    public void restock(String warehouseId, String itemId, int quantity) {
        Warehouse warehouse = getWarehouse(warehouseId);
        InventoryItem item = warehouse.getItem(itemId);
        item.addStock(quantity);
        System.out.println("Restocked " + item.getProduct().getName() + " +" + quantity + " at " + warehouse.getName());
    }

    public void transferStock(String fromWarehouseId, String toWarehouseId, String itemId, int quantity) {
        Warehouse from = getWarehouse(fromWarehouseId);
        Warehouse to = getWarehouse(toWarehouseId);

        InventoryItem fromItem = from.getItem(itemId);
        fromItem.removeStock(quantity);

        if (to.hasItem(itemId)) {
            to.getItem(itemId).addStock(quantity);
        } else {
            InventoryItem newItem = new InventoryItem(itemId, fromItem.getProduct(), quantity);
            newItem.setReplenishmentStrategy(fromItem.getReplenishmentStrategy());
            to.addItem(newItem);
        }

        System.out.println("Transferred " + quantity + "x " + fromItem.getProduct().getName()
                + " from " + from.getName() + " → " + to.getName());
    }

    // ==================== QUERIES ====================

    public int getTotalStock(String itemId) {
        return warehouses.values().stream()
                .filter(wh -> wh.hasItem(itemId))
                .mapToInt(wh -> wh.getItem(itemId).getQuantity())
                .sum();
    }

    public List<InventoryItem> getLowStockItems() {
        List<InventoryItem> result = new ArrayList<>();
        for (Warehouse wh : warehouses.values()) {
            result.addAll(wh.getLowStockItems());
        }
        return result;
    }

    public void printInventoryReport() {
        System.out.println("\n========== INVENTORY REPORT ==========");
        for (Warehouse wh : warehouses.values()) {
            System.out.println("\n📦 " + wh);
            for (InventoryItem item : wh.getAllItems()) {
                System.out.println("   " + item);
            }
        }
        System.out.println("=======================================\n");
    }
}

