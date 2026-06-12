Inventory
Money Handling
State Management
Dispense Product
Refund
Requirements
Load Products
Insert Coins
Select Product
Dispense Product
Refund Money
Components
VendingMachine
    Inventory
    currentAmount
    State

    insertCoin()
    selectProduct()
    dispense()
    refund()
Inventory
    Map<ProductId, Slot>

    addProduct()
    getProduct()
Slot
    Product
    quantity

    isAvailable()
Product
    id
    name
    price
    type
enum Coin
{
    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10)
}
Main Logic
Insert Coin
insertCoin(Coin coin)
currentAmount += coin.value
Select Product
selectProduct(productId)

Validation:

Product Exists?
Quantity Available?
Money Sufficient?
Dispense
dispense()

Flow:

Reduce Inventory
Return Product
Return Change
Reset Amount
Refund
refund()

Flow:

Return currentAmount
Reset Amount
Most Important Pattern
State Pattern ⭐

This is one of the few questions where State Pattern actually fits.

interface IVendingMachineState
{
    insertCoin()
    selectProduct()
    dispense()
    refund()
}

States:

IdleState
HasMoneyState
DispensingState
OutOfStockState

Example:

IdleState

Allowed:

Insert Coin

Not Allowed:

Dispense
HasMoneyState

Allowed:

Insert Coin
Select Product
Refund
DispensingState

Allowed:

Dispense Product
Relationships
VendingMachine
    HAS-A Inventory

Inventory
    HAS-A Slot

Slot
    HAS-A Product

VendingMachine
    HAS-A State
Concurrency Followup

Suppose:

Only 1 Coke Left

Two users:

Select Coke

simultaneously.

Need:

lock(slot)

before:

quantity--

Otherwise:

Oversell
Common Followups
Multiple Payment Modes

Add:

IPaymentStrategy

Implementations:

CoinPayment
CardPayment
UPIPayment
Dynamic Pricing
IPricingStrategy
Restocking
restockProduct()

Admin operation.

Product Expiry

Add:

expiryDate

inside Product.

Patterns
State Pattern ⭐
Idle
HasMoney
Dispensing
OutOfStock
Strategy Pattern
Payment
Pricing

(optional)

30 Second Revision
VendingMachine
    insertCoin()
    selectProduct()
    dispense()
    refund()

Inventory
    Slots

Slot
    Product
    quantity

Patterns:
    State Pattern
        -> Machine States

Followups:
    Payment Strategy
    Restocking
    Concurrency