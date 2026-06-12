package amazon.chess;

import amazon.chess.piece.*;

public class Board {

    private final Piece[][] grid;

    public Board() {
        grid = new Piece[8][8];
        initialize();
    }

    private void initialize() {
        // Black pieces (top: row 0)
        grid[0][0] = new Rook(Color.BLACK, new Position(0, 0));
        grid[0][1] = new Knight(Color.BLACK, new Position(0, 1));
        grid[0][2] = new Bishop(Color.BLACK, new Position(0, 2));
        grid[0][3] = new Queen(Color.BLACK, new Position(0, 3));
        grid[0][4] = new King(Color.BLACK, new Position(0, 4));
        grid[0][5] = new Bishop(Color.BLACK, new Position(0, 5));
        grid[0][6] = new Knight(Color.BLACK, new Position(0, 6));
        grid[0][7] = new Rook(Color.BLACK, new Position(0, 7));
        for (int c = 0; c < 8; c++) {
            grid[1][c] = new Pawn(Color.BLACK, new Position(1, c));
        }

        // White pieces (bottom: row 7)
        grid[7][0] = new Rook(Color.WHITE, new Position(7, 0));
        grid[7][1] = new Knight(Color.WHITE, new Position(7, 1));
        grid[7][2] = new Bishop(Color.WHITE, new Position(7, 2));
        grid[7][3] = new Queen(Color.WHITE, new Position(7, 3));
        grid[7][4] = new King(Color.WHITE, new Position(7, 4));
        grid[7][5] = new Bishop(Color.WHITE, new Position(7, 5));
        grid[7][6] = new Knight(Color.WHITE, new Position(7, 6));
        grid[7][7] = new Rook(Color.WHITE, new Position(7, 7));
        for (int c = 0; c < 8; c++) {
            grid[6][c] = new Pawn(Color.WHITE, new Position(6, c));
        }
    }

    public Piece getPiece(Position pos) {
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setPiece(Position pos, Piece piece) {
        grid[pos.getRow()][pos.getCol()] = piece;
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        setPiece(from, null);
        setPiece(to, piece);
        piece.setPosition(to);
    }

    public Piece[][] getGrid() {
        return grid;
    }

    public Position findKing(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King && p.getColor() == color) {
                    return new Position(r, c);
                }
            }
        }
        throw new RuntimeException("King not found for " + color);
    }

    public boolean isUnderAttack(Position pos, Color byColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == byColor) {
                    for (Position move : p.getValidMoves(grid)) {
                        if (move.equals(pos)) return true;
                    }
                }
            }
        }
        return false;
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int r = 0; r < 8; r++) {
            System.out.print((8 - r) + " ");
            for (int c = 0; c < 8; c++) {
                System.out.print(grid[r][c] == null ? ". " : grid[r][c].getSymbol() + " ");
            }
            System.out.println((8 - r));
        }
        System.out.println("  a b c d e f g h");
    }
}

