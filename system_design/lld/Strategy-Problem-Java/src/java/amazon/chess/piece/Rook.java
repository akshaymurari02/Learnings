package amazon.chess.piece;

import amazon.chess.Color;
import amazon.chess.Position;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Piece[][] board) {
        List<Position> moves = new ArrayList<>();
        addLinearMoves(board, moves, -1, 0);
        addLinearMoves(board, moves, 1, 0);
        addLinearMoves(board, moves, 0, -1);
        addLinearMoves(board, moves, 0, 1);
        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "R" : "r";
    }
}

