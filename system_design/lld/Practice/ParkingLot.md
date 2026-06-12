Requirements
Park Vehicle
Unpark Vehicle
Generate Ticket
Multiple Floors
Different Spot Types
Components
ParkingLotService
    Floors
    ParkingStrategy

    park(vehicle)
    unpark(ticket)
Floor
    List<ParkingSpot>

    parkVehicle()
    unparkVehicle()
ParkingSpot
    spotId
    VehicleType
    Vehicle vehicle

    isFree()
    assignVehicle()
    removeVehicle()
Vehicle
    registrationNumber
    VehicleType
enum VehicleType
{
    BIKE,
    CAR,
    TRUCK
}
Ticket
    ticketId
    Vehicle
    ParkingSpot
    entryTime
Main Logic
Park
park(vehicle)
ParkingLotService
        |
ParkingStrategy
        |
Find Spot
        |
Assign Vehicle
        |
Generate Ticket
Unpark
unpark(ticket)
Find Spot
Remove Vehicle
Calculate Fee
Free Spot
Most Important Pattern
Strategy Pattern ⭐
interface IParkingStrategy
{
    ParkingSpot findSpot(...)
}

Implementations:

NearestParkingStrategy
RandomParkingStrategy
FirstAvailableStrategy

This is the pattern interviewers usually expect.

Relationships
ParkingLotService
    HAS-A Floor

Floor
    HAS-A ParkingSpot

ParkingSpot
    HAS-A Vehicle

Ticket
    HAS-A ParkingSpot
Common Followups
Payment

Add:

PaymentService
processPayment()
Multiple Entry Gates

Add:

EntryGate
ExitGate
Display Board
DisplayBoard

showAvailableSpots()
Spot Reservation
AVAILABLE
OCCUPIED
RESERVED
Concurrency Followup

Most common SDE2 question.

Suppose:

2 cars
1 spot

Both attempt:

park()

Solution:

synchronized
ReentrantLock
CAS

around spot assignment.

Example:

spot.lock()

if(spot.isFree())
{
    assign()
}

spot.unlock()
What NOT To Do

Don't create:

SpotManager
FloorManager
VehicleManager
TicketManager
PaymentManager

for every entity.

Interviewers prefer:

Simple Design
30 Second Revision
ParkingLotService
    park()
    unpark()

Floor
    spots

ParkingSpot
    vehicleType
    vehicle

Vehicle

Ticket

Pattern:
    Strategy Pattern
        -> find parking spot

Followups:
    Payment
    Entry/Exit Gates
    Display Board
    Concurrency