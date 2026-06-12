Vehicle Inventory
Reservation
Payment
Store Management

Not fleet tracking, GPS, etc.

Requirements
Search Vehicles
Reserve Vehicle
Rent Vehicle
Return Vehicle
Payment
Multiple Rental Stores
Components
CarRentalService
    List<RentalStore>
    PaymentStrategy

    rentVehicle()
    returnVehicle()
RentalStore
    storeId
    location
    List<Vehicle>

    addVehicle()
    searchVehicle()
Vehicle
    vehicleId
    registrationNumber
    VehicleStatus
    dailyRate

    calculateRent()
enum VehicleStatus
{
    AVAILABLE,
    RESERVED,
    RENTED,
    MAINTENANCE
}
Customer
    customerId
    name
    licenseNumber
Reservation
    reservationId
    Customer
    Vehicle
    startDate
    endDate
    ReservationStatus

    calculateAmount()
enum ReservationStatus
{
    CREATED,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}
Main Logic
Search Vehicle
searchVehicle(location,type)

Flow:

CarRentalService
      |
RentalStore
      |
Filter AVAILABLE Vehicles
Reserve Vehicle
reserveVehicle(...)

Flow:

Vehicle AVAILABLE
      |
Reservation Created
      |
Vehicle RESERVED
Rent Vehicle
rentVehicle(...)

Flow:

Reservation
      |
Payment
      |
Vehicle RENTED
Return Vehicle
returnVehicle(...)

Flow:

Vehicle Returned
      |
Calculate Charges
      |
Refund / Additional Charge
      |
AVAILABLE
Most Important Pattern
Strategy Pattern ⭐

Payment.

interface PaymentStrategy
{
    processPayment(...)
}

Implementations:

CreditCardPayment
UPIPayment
NetBankingPayment
Relationships
CarRentalService
    HAS-A RentalStore

RentalStore
    HAS-A Vehicle

Reservation
    HAS-A Customer

Reservation
    HAS-A Vehicle

CarRentalService
    HAS-A PaymentStrategy
Concurrency Followup

Most common.

Suppose:

Only 1 BMW Available

Two users:

Reserve BMW

simultaneously.

Need:

lock(vehicle)

before:

AVAILABLE -> RESERVED

Otherwise double booking.

Common Followups
Dynamic Pricing

Add:

IPricingStrategy

Implementations:

WeekendPricing
HolidayPricing
SurgePricing
Multiple Vehicle Types
Car
SUV
Truck
Bike

inherit:

Vehicle
Maintenance

Vehicle state:

MAINTENANCE

cannot be rented.

Cancellation
cancelReservation()

Vehicle:

RESERVED -> AVAILABLE
Patterns
Strategy ⭐
Payment
Pricing
State (Optional)
AVAILABLE
RESERVED
RENTED
MAINTENANCE

Usually enum is enough.

Factory (Optional)
VehicleFactory

for creating vehicle types.

30 Second Revision
CarRentalService
    search
    reserve
    rent
    return

RentalStore
    vehicles

Vehicle
    status
    dailyRate

Customer

Reservation

Patterns:
    Strategy
        -> Payment
        -> Pricing

Followups:
    Dynamic Pricing
    Maintenance
    Concurrency
    Cancellation