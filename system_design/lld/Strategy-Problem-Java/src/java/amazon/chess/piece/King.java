package amazon.chess.piece;

import amazon.chess.Color;
import amazon.chess.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    private static final int[][] DIRS = {
        {-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}
    };

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Piece[][] board) {
        List<Position> moves = new ArrayList<>();
        for (int[] d : DIRS) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            if (isInsideBoard(r, c) && isEmptyOrEnemy(board, r, c)) {
                moves.add(new Position(r, c));
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "K" : "k";
    }
}

