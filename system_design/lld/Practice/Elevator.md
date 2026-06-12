Scheduling Strategy
State Management
Concurrency

Not about writing 500 lines.

Requirements
Request Elevator
Select Destination Floor
Move Elevator
Open/Close Door
Multiple Elevators
Components
ElevatorService
    List<Elevator>
    ElevatorSchedulingStrategy

    requestElevator(from,to)
Elevator
    id
    currentFloor
    direction
    state

    upRequests
    downRequests

    move()
    addStop()
enum Direction
{
    UP,
    DOWN,
    IDLE
}
enum ElevatorState
{
    MOVING,
    IDLE,
    DOOR_OPEN
}
class Request
{
    sourceFloor
    destinationFloor
}
Main Logic
External Request

User presses:

Floor 5 ↑

Flow:

requestElevator(5,UP)
        |
Scheduling Strategy
        |
Choose Elevator
        |
addStop(5)
Internal Request

Inside elevator:

Floor 10

Flow:

elevator.addStop(10)
Scheduler Strategy ⭐

Most important pattern.

interface ElevatorSchedulingStrategy
{
    Elevator assignElevator(...);
}
Nearest Elevator

Example:

E1 -> Floor 3
E2 -> Floor 8
Request -> Floor 5

Choose:

E1
Direction Aware

Better.

Request:

Floor 5 UP

Choose elevator:

Moving UP
Closest

instead of:

Moving DOWN
Elevator Data Structure

Usually:

TreeSet<Integer> upStops;
TreeSet<Integer> downStops;

Example

Current:

Floor 5
Direction UP

Requests:

8
12
15

Serve:

8
12
15

in order.

Move Logic
move()

Pseudo:

if(direction == UP)
{
    currentFloor++;

    if(currentFloor == nextStop)
    {
        openDoor();
        removeStop();
    }
}
Relationships
ElevatorService
    HAS-A Elevators

ElevatorService
    HAS-A Scheduler

Elevator
    HAS-A Requests
Patterns
Strategy Pattern ⭐
ElevatorSchedulingStrategy

Different scheduling algorithms.

State Pattern (Reasonable)
IDLE
MOVING
DOOR_OPEN

Can be:

IElevatorState

or enum.

For interview:

enum

usually enough.

Concurrency Followup

Suppose:

100 users

press buttons simultaneously.

Need:

PriorityQueue
TreeSet

protected by:

ReentrantLock

or synchronized.

Common Followups
VIP Elevator

Add:

VIPSchedulingStrategy

Strategy pattern shines.

Elevator Breakdown

State:

OUT_OF_SERVICE

Scheduler skips elevator.

Fire Emergency

State:

EMERGENCY

Move all elevators:

Ground Floor
What NOT To Do

Don't over-design:

ButtonManager
DoorManager
MotorManager
SensorManager

unless interviewer specifically wants hardware modeling.

Amazon LLD usually wants:

Object Design
Scheduling Logic
30 Second Revision
ElevatorService
    requestElevator()

Elevator
    currentFloor
    direction
    state
    upStops
    downStops

Request

Patterns:
    Strategy Pattern
        -> Elevator Scheduling

Followups:
    Nearest Elevator
    Direction Aware Scheduling
    VIP
    Breakdown
    Concurrency

If interviewer asks only one deep question, it'll usually be:

"How do you decide which elevator gets a new request?"

Your answer should immediately go to:

ElevatorSchedulingStrategy

That's the core design discussion in Elevator System.