Tic toc toe

Requirements:

start a game with 2 players 
in each move players will switch there turn 
if symbols in row or col or diagnoally match player won and game ends


Components and relationships: 

TicTocToeGameService
    Board b
    Player p1, p2
    CurrentPlayerHandler curr
    GameStatus status

    makeMove(Position)

Board 
    isOutOfBounds(Postion pos)
    markPostion(Postion pos, Symbol s)
    checkWinner(Symbol s)

Position 
    row, col 

enum Symbol 
    X
    O

Player
    name 
    matches_won 
    games_played

GameStatus 
    DRAW
    IN_PROGRESS
    COMPLETED

Design discussion

will use status pattern for game status 
for current player state also use state pattern 
and used compostion for TicTocToeGameService

out of scope 
can also have Player strategy for bots like

IPlayerStrategy 
setNextMove()
canPredictNextMove()
getNextMove() -> throws Exception if next move cannot predicted 

HumanPlayer
BotPlayer


Error Handling 
out of bound and player startegy or if cannot mark already marked cell we throw exceptions 

Logic 

private final int[] rows;
private final int[] cols;
private int diagonal;
private int antiDiagonal;

For X:

+1

For O:

-1

After move:

rows[row] += value;
cols[col] += value;

Winner:

Math.abs(rows[row]) == n
||
Math.abs(cols[col]) == n
||
Math.abs(diagonal) == n
||
Math.abs(antiDiagonal) == n