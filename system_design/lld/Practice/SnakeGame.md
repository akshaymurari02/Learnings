SnakeGameService
    Board
    Snake
    Direction
    Score
    GameStatus

Board
    rows
    cols
    foodPosition

    spawnFood()
Snake
    Deque<Position>

    move()
    grow()
    collidesWithSelf()

Use:

Deque<Position>

because:

Add Head -> O(1)
Remove Tail -> O(1)
Main Logic
move()
Compute Next Head Position
Out Of Bounds?

→ Game Over

Self Collision?

→ Game Over

Food Found?

→ Grow Snake

→ Increase Score

→ Spawn New Food

Else

→ Add Head

→ Remove Tail

Design Patterns

Honestly:

None Required

If interviewer pushes:

Strategy
IFoodSpawnStrategy

Random Food

Special Food

etc.

But not necessary.

Followups
Multiplayer Snake
List<Snake>
Different Food Types
Food
NormalFood
BonusFood
PoisonFood

Strategy / Inheritance.

Thread Safety

Not usually asked.

Single game loop.

30 Second Revision
Snake -> Deque<Position>

Board -> Food Position

GameService
    move()
    changeDirection()

Move:
    calculate next head
    boundary check
    self collision
    food eaten -> grow
    else remove tail

GameStatus:
    IN_PROGRESS
    GAME_OVER