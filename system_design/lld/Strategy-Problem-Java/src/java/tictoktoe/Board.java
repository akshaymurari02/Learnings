package tictoktoe;

import java.util.Arrays;

class Board {

    private int size;
    private char[][] grid;

    public Board(int size) {
        this.size = size;
        grid = new char[size][size];

        // initialize
        for (int i = 0; i < size; i++) {
            Arrays.fill(grid[i], '-');
        }
    }

    public boolean isCellEmpty(int r, int c) {
        return grid[r][c] == '-';
    }

    public void markCell(int r, int c, char symbol) {
        grid[r][c] = symbol;
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getSize() {
        return size;
    }
}
