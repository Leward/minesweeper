package fr.leward.minesweeper.components;

public class Cell {

    private final int row;
    private final int column;
    private final boolean mine;
    boolean revealed = false;

    public Cell(int row, int column) {
        this(row, column, false);
    }

    public Cell(int row, int column, boolean mine) {
        this.row = row;
        this.column = column;
        this.mine = mine;
    }

    public void reveal() {
        this.revealed = true;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isRevealed() {
        return revealed;
    }
}
