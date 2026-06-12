package amazon.chess.piece;

import amazon.chess.Color;
import amazon.chess.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getValidMoves(Piece[][] board) {
        List<Position> moves = new ArrayList<>();
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] d : dirs) {
            addLinearMoves(board, moves, d[0], d[1]);
        }
        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "Q" : "q";
    }
}

