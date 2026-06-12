package amazon.chess.piece;

import amazon.chess.Color;
import amazon.chess.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Piece[][] board) {
        List<Position> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? -1 : 1;
        int r = position.getRow();
        int c = position.getCol();

        // Forward one
        int newR = r + direction;
        if (isInsideBoard(newR, c) && board[newR][c] == null) {
            moves.add(new Position(newR, c));

            // Forward two from starting position
            int twoR = r + 2 * direction;
            if (!hasMoved && isInsideBoard(twoR, c) && board[twoR][c] == null) {
                moves.add(new Position(twoR, c));
            }
        }

        // Diagonal captures
        for (int dc : new int[]{-1, 1}) {
            int nc = c + dc;
            if (isInsideBoard(newR, nc) && board[newR][nc] != null
                    && board[newR][nc].getColor() != this.color) {
                moves.add(new Position(newR, nc));
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "P" : "p";
    }
}

