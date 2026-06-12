Position kingPos = findKing(kingColor);

for(Piece piece : opponentPieces) {
    if(piece.isValidMove(board,
                         piecePos,
                         kingPos)) {
        return true;
    }
}

return false;

1. Strategy Pattern
   -> Piece movement rules

2. Factory Pattern
   -> Board initialization

3. Command Pattern (optional)
   -> Undo/Redo

4. State Pattern (optional)
   -> Game states