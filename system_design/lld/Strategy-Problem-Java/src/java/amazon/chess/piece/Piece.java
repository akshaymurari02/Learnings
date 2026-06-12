package amazon.chess.piece;

import amazon.chess.Color;
import amazon.chess.Position;

import java.util.List;

public abstract class Piece {
    protected Color color;
    protected Position position;
    protected boolean hasMoved;

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }

    public abstract List<Position> getValidMoves(Piece[][] board);
    public abstract String getSymbol();

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public boolean hasMoved() { return hasMoved; }

    public void setPosition(Position position) {
        this.position = position;
        this.hasMoved = true;
    }

    protected boolean isInsideBoard(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    protected boolean isEmptyOrEnemy(Piece[][] board, int r, int c) {
        return board[r][c] == null || board[r][c].getColor() != this.color;
    }

    protected void addLinearMoves(Piece[][] board, List<Position> moves, int dr, int dc) {
        int r = position.getRow() + dr;
        int c = position.getCol() + dc;
        while (isInsideBoard(r, c)) {
            if (board[r][c] == null) {
                moves.add(new Position(r, c));
            } else {
                if (board[r][c].getColor() != this.color) {
                    moves.add(new Position(r, c));
                }
                break;
            }
            r += dr;
            c += dc;
        }
    }
}

