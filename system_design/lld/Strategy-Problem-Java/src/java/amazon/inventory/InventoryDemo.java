package amazon.inventory;

public class InventoryDemo {

    public static void main(String[] args) {

        // 1. Create InventoryManager (Subject/Notifier)
        InventoryManager manager = new InventoryManager();

        // 2. Create replenishment strategies
        IReplenishmentStrategy bulkStrategy = new BulkOrderStrategy(100);
        IReplenishmentStrategy jitStrategy = new JustInTimeStrategy(20);

        // 3. Register LowStockObserver with default JIT strategy
        LowStockObserver lowStockObserver = new LowStockObserver(jitStrategy);
        manager.registerObserver(lowStockObserver);

        // 4. Setup warehouses
        Warehouse nyc = new Warehouse("W1", "NYC Warehouse", "New York");
        Warehouse la = new Warehouse("W2", "LA Warehouse", "Los Angeles");
        manager.addWarehouse(nyc);
        manager.addWarehouse(la);

        // 5. Create products & inventory items with different strategies
        Product iphone = new Product("P1", "iPhone 15", 999.99, Category.ELECTRONICS);
        Product tshirt = new Product("P2", "T-Shirt", 29.99, Category.CLOTHING);
        Product book = new Product("P3", "Java Book", 49.99, Category.BOOKS);

        // iPhone uses Bulk Order (high demand, predictable)
        InventoryItem iphoneItem = new InventoryItem("ITEM-1", iphone, 50, 10);
        iphoneItem.setReplenishmentStrategy(bulkStrategy);

        // T-Shirt uses JIT (seasonal, unpredictable)
        InventoryItem tshirtItem = new InventoryItem("ITEM-2", tshirt, 30, 5);
        tshirtItem.setReplenishmentStrategy(jitStrategy);

        // Book uses default strategy (no override)
        InventoryItem bookItem = new InventoryItem("ITEM-3", book, 8, 3);

        // 6. Add items to warehouses
        manager.addItem("W1", iphoneItem);
        manager.addItem("W1", tshirtItem);
        manager.addItem("W2", bookItem);

        // 7. Print initial state
        manager.printInventoryReport();

        // 8. Sell items — triggers Observer when low stock
        System.out.println("=== SELLING ===\n");

        manager.sell("W1", "ITEM-1", 42); // iPhone: 50 -> 8 (LOW! triggers BulkOrder)

        System.out.println();
        manager.sell("W1", "ITEM-2", 27); // T-Shirt: 30 -> 3 (LOW! triggers JIT)

        System.out.println();
        manager.sell("W2", "ITEM-3", 8);  // Book: 8 -> 0 (OUT! triggers default JIT)

        // 9. Print final state
        manager.printInventoryReport();

        // 10. Transfer between warehouses
        System.out.println("=== TRANSFER ===\n");
        manager.transferStock("W1", "W2", "ITEM-1", 30);

        manager.printInventoryReport();
    }
}
