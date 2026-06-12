package amazon.chess;

import amazon.chess.piece.Piece;

import java.util.List;

public class GameService {

    private final Board board;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player currentPlayer;
    private GameStatus status;

    public GameService(Player whitePlayer, Player blackPlayer) {
        this.board = new Board();
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.currentPlayer = whitePlayer; // white moves first
        this.status = GameStatus.IN_PROGRESS;
    }

    public GameStatus makeMove(Position from, Position to) {
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE || status == GameStatus.RESIGNED) {
            throw new RuntimeException("Game is already over");
        }

        Piece piece = board.getPiece(from);

        // Validate piece exists and belongs to current player
        if (piece == null) {
            throw new RuntimeException("No piece at " + from);
        }
        if (piece.getColor() != currentPlayer.getColor()) {
            throw new RuntimeException("Not your piece");
        }

        // Validate move is legal
        List<Position> validMoves = piece.getValidMoves(board.getGrid());
        if (!validMoves.contains(to)) {
            throw new RuntimeException("Invalid move for " + piece.getSymbol() + " from " + from + " to " + to);
        }

        // Simulate move and check if own king is left in check
        Piece captured = board.getPiece(to);
        board.movePiece(from, to);

        Color ownColor = currentPlayer.getColor();
        Color opponentColor = (ownColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if (isInCheck(ownColor)) {
            // Undo move — can't leave own king in check
            board.movePiece(to, from);
            board.setPiece(to, captured);
            piece.setPosition(from);
            throw new RuntimeException("Move leaves your king in check");
        }

        // Check game state after move
        if (isInCheck(opponentColor)) {
            if (hasNoLegalMoves(opponentColor)) {
                status = GameStatus.CHECKMATE;
            } else {
                status = GameStatus.CHECK;
            }
        } else if (hasNoLegalMoves(opponentColor)) {
            status = GameStatus.STALEMATE;
        } else {
            status = GameStatus.IN_PROGRESS;
        }

        switchPlayer();
        return status;
    }

    public void resign() {
        status = GameStatus.RESIGNED;
    }

    private boolean isInCheck(Color color) {
        Position kingPos = board.findKing(color);
        Color opponent = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        return board.isUnderAttack(kingPos, opponent);
    }

    private boolean hasNoLegalMoves(Color color) {
        Piece[][] grid = board.getGrid();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == color) {
                    for (Position move : p.getValidMoves(grid)) {
                        // Simulate
                        Position origPos = p.getPosition();
                        Piece captured = board.getPiece(move);
                        board.movePiece(origPos, move);

                        boolean stillInCheck = isInCheck(color);

                        // Undo
                        board.movePiece(move, origPos);
                        board.setPiece(move, captured);
                        p.setPosition(origPos);

                        if (!stillInCheck) return false;
                    }
                }
            }
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public Player getCurrentPlayer() { return currentPlayer; }
    public GameStatus getStatus() { return status; }
    public Board getBoard() { return board; }
}

