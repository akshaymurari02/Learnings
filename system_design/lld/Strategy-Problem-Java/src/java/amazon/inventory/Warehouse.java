package amazon.inventory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Warehouse contains a map of inventoryItemId -> InventoryItem.
 */
public class Warehouse {
    private final String id;
    private final String name;
    private final String location;
    private final Map<String, InventoryItem> items; // itemId -> InventoryItem

    public Warehouse(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.items = new HashMap<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }

    public void addItem(InventoryItem item) {
        if (items.containsKey(item.getId())) {
            // If item exists, just add stock
            items.get(item.getId()).addStock(item.getQuantity());
        } else {
            items.put(item.getId(), item);
        }
    }

    public void removeItem(String itemId) {
        if (!items.containsKey(itemId)) {
            throw new RuntimeException("Item not found in warehouse: " + itemId);
        }
        items.remove(itemId);
    }

    public InventoryItem getItem(String itemId) {
        InventoryItem item = items.get(itemId);
        if (item == null) {
            throw new RuntimeException("Item " + itemId + " not found in warehouse " + name);
        }
        return item;
    }

    public boolean hasItem(String itemId) {
        return items.containsKey(itemId);
    }

    public List<InventoryItem> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public List<InventoryItem> getLowStockItems() {
        return items.values().stream()
                .filter(InventoryItem::isLowStock)
                .collect(Collectors.toList());
    }

    public List<InventoryItem> getOutOfStockItems() {
        return items.values().stream()
                .filter(InventoryItem::isOutOfStock)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}
