package tictoktoe;

class GameService {

    private Board board;
    private Player p1;
    private Player p2;
    private Player currentPlayer;
    private int moves;

    public GameService(Board board, Player p1, Player p2) {
        this.board = board;
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = p1;
        this.moves = 0;
    }

    // mark a move
    public GameStatus makeMove(int r, int c) {

        if (!board.isCellEmpty(r, c)) {
            throw new RuntimeException("Cell already occupied");
        }

        board.markCell(r, c, currentPlayer.getSymbol());
        moves++;

        // check win
        if (checkWin(r, c, currentPlayer.getSymbol())) {
            return GameStatus.WIN;
        }

        // check draw
        if (moves == board.getSize() * board.getSize()) {
            return GameStatus.DRAW;
        }

        // switch player
        switchPlayer();
        return GameStatus.IN_PROGRESS;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == p1) ? p2 : p1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private boolean checkWin(int r, int c, char symbol) {

        int n = board.getSize();
        char[][] g = board.getGrid();

        // check row
        boolean win = true;
        for (int i = 0; i < n; i++) {
            if (g[r][i] != symbol) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // check column
        win = true;
        for (int i = 0; i < n; i++) {
            if (g[i][c] != symbol) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // check diagonal
        if (r == c) {
            win = true;
            for (int i = 0; i < n; i++) {
                if (g[i][i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // anti diagonal
        if (r + c == n - 1) {
            win = true;
            for (int i = 0; i < n; i++) {
                if (g[i][n - 1 - i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        return false;
    }

}