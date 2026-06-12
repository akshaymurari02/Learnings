Stock Tracking
Warehouses
Low Stock Alerts
Replenishment

Not about e-commerce checkout flows.

Requirements
Add Inventory
Remove Inventory
Track Stock
Multiple Warehouses
Low Stock Alerts
Replenishment
Components
InventoryService
    List<Warehouse>
    List<InventoryObserver>
    ReplenishmentStrategy

    sell()
    addInventory()
Warehouse
    warehouseId
    Map<ProductId, InventoryItem>

    addItem()
    removeItem()
    getItem()
Product
    sku
    name
    category
InventoryItem
    Product
    quantity
    lowThreshold
Main Logic
Sell Product
sell(warehouseId,
     productId,
     quantity)

Flow:

InventoryService
      |
Warehouse
      |
Reduce Quantity
      |
Check Threshold
      |
Notify Observers
Add Inventory
addInventory(...)

Flow:

Warehouse
      |
Increase Quantity
Observer Pattern ⭐

Most important pattern.

interface InventoryObserver
{
    notifyLowStock(...)
}

Implementation:

LowStockObserver
EmailObserver
SupplierObserver

When:

quantity < threshold

InventoryService:

observer.notify(...)
Replenishment Strategy ⭐

Second most important pattern.

interface IReplenishmentStrategy
{
    replenish(...)
}

Implementations:

BulkOrderStrategy
JustInTimeStrategy

Example:

Bulk
Remaining = 10

Order:

1000 units
JIT
Remaining = 10

Order:

20 units
Relationships
InventoryService
    HAS-A Warehouse

Warehouse
    HAS-A InventoryItem

InventoryItem
    HAS-A Product

InventoryService
    HAS-A ReplenishmentStrategy

InventoryService
    HAS-A InventoryObserver
Concurrency Followup

Suppose:

Stock = 1

Two orders:

Order A
Order B

Both:

sell(...)

Need:

lock(item)

before decrement.

Otherwise:

Overselling

Solutions:

ReentrantLock

or

AtomicInteger

for quantity.

Common Followups
Multiple Warehouses

Choose warehouse:

IWarehouseSelectionStrategy

Examples:

Nearest Warehouse
Lowest Cost Warehouse
Highest Stock Warehouse
Out Of Stock

Observer:

notifyOutOfStock()
Reservation

E-commerce:

AVAILABLE = 10

User reserves 2

AVAILABLE = 8
RESERVED = 2

until payment completes.

Patterns
Observer ⭐
Low Stock Alerts
Strategy ⭐
Replenishment
Warehouse Selection
Composition
Warehouse
InventoryItem
Product
30 Second Revision
InventoryService
    sell()
    addInventory()

Warehouse
    Map<ProductId, InventoryItem>

InventoryItem
    quantity
    threshold

Product

Patterns:
    Observer
        -> Low Stock

    Strategy
        -> Replenishment

Followups:
    Multi Warehouse
    Reservation
    Concurrency