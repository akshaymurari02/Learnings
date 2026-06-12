package amazon.chess.piece;

import amazon.chess.Color;
import amazon.chess.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    private static final int[][] MOVES = {
        {-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}
    };

    public Knight(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Piece[][] board) {
        List<Position> moves = new ArrayList<>();
        for (int[] m : MOVES) {
            int r = position.getRow() + m[0];
            int c = position.getCol() + m[1];
            if (isInsideBoard(r, c) && isEmptyOrEnemy(board, r, c)) {
                moves.add(new Position(r, c));
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "N" : "n";
    }
}

