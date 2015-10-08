package fr.leward.minesweeper.components;

public class CellCoordinates {

    private final int row;
    private final int column;

    public CellCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
