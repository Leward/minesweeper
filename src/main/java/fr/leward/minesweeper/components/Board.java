package fr.leward.minesweeper.components;

import com.google.common.collect.*;
import fr.leward.minesweeper.exception.CellNotFoundException;
import fr.leward.minesweeper.exception.TooManyMinesException;

import java.util.*;

public class Board {

    private final int width;
    private final int height;
    private final int nbMines;

    private final ImmutableTable<Integer, Integer, Cell> cells;

    /**
     * Create a new board
     * @param width Width of the board
     * @param height Height of the board
     * @param nbMines Number of mines to be placed on the board
     */
    public Board(int width, int height, int nbMines) throws TooManyMinesException {
        this.width = width;
        this.height = height;
        this.nbMines = nbMines;
        cells = generateBoard(width, height, nbMines);
    }

    /**
     * Generate a new board which consists of a table of cells
     * @param width width of the board to generate
     * @param height height of the board to generate
     * @param nbMines number of mines that will be placed on the board
     * @return a generate table of cells to be used as a board
     * @throws TooManyMinesException throwns when the number of mines exceeds the size of the board
     */
    private ImmutableTable<Integer, Integer, Cell> generateBoard(int width, int height, int nbMines) throws TooManyMinesException{
        // There must be less mines than the total number of cells
        if(nbMines >= (width * height)) {
            throw new TooManyMinesException();
        }

        // Fill a table of cells
        Table<Integer, Integer, Cell> table = HashBasedTable.create(width, height);
        for(int row = 0; row < width; row++) {
            for(int column = 0; column < height; column++) {
                table.put(row, column, new Cell(row, column));
            }
        }

        // Put mines on the board
        for(int i = 0; i < nbMines; i++) {
            putRandomMineOnBoard(table);
        }

        return ImmutableTable.copyOf(table);
    }

    /**
     * Puts a mine at a random location of the board
     * A randomly selected cell is set with a mine if the cell has no mine on it
     * or the cell has not been defined yet.
     * @param cells the table of cells in which a mine will be randomly placed
     */
    private void putRandomMineOnBoard(Table<Integer, Integer, Cell> cells) {
        Random random = new Random();
        // Try to put a mine randomly until a valid cell is found
        while(true) {
            int row = random.nextInt(width);
            int column = random.nextInt(height);
            Cell randomCell = cells.get(row, column);
            if(randomCell == null || !randomCell.isMine()) {
                cells.put(row, column, new Cell(row, column, true));
                break;
            }
        }
    }

    /**
     * Get a cell using its coordinates.
     * Off board coordinates return an empty value and do not throw exception
     * @param row the X coordinate
     * @param column the Y coordinate
     * @return the cell at (x,y) coordinates if applicable, otherwise an empty value
     */
    public Optional<Cell> getCell(int row, int column) {
        if(row < 0 || row >= width || column < 0 || column > height) {
            return Optional.empty();
        }
        else {
            return Optional.ofNullable(cells.get(row, column));
        }
    }

    /**
     * Get a list of cells that are adjacent to the cell located at a given row and column
     * @param row row of the cell to target
     * @param column row of the cell to target
     * @return list of adjacent cells
     */
    public ImmutableList getAdjacentCells(int row, int column) {
        List<Cell> adjacentCells = new LinkedList<>();

        // Go through the surrounding cells
        getCell(row - 1, column - 1)    .ifPresent(cell -> adjacentCells.add(cell)); // Top-left
        getCell(row - 1, column)        .ifPresent(cell -> adjacentCells.add(cell)); // Top
        getCell(row - 1, column + 1)    .ifPresent(cell -> adjacentCells.add(cell)); // Top-right
        getCell(row, column + 1)        .ifPresent(cell -> adjacentCells.add(cell)); // Right
        getCell(row + 1, column + 1)    .ifPresent(cell -> adjacentCells.add(cell)); // Bottom-right
        getCell(row + 1, column)        .ifPresent(cell -> adjacentCells.add(cell)); // Bottom
        getCell(row + 1, column - 1)    .ifPresent(cell -> adjacentCells.add(cell)); // Bottom-left
        getCell(row, column - 1)        .ifPresent(cell -> adjacentCells.add(cell)); // Left

        return ImmutableList.copyOf(adjacentCells);
    }

    /**
     * Count the number of mines among a collection of cells
     * @param cells collection of cells
     * @return the number of mines in the collection
     */
    public static int countMines(final Collection<Cell> cells) {
        // Here stream API is used as a showcase. However I personally
        // find the classic foreach iteration with a counter more readable.
        return cells.stream()
                .map(cell -> (cell.isMine()) ? 1 : 0)
                .reduce(0, (a, b) -> a + b)
                .intValue();
    }

    /**
     * Count the adjacent mines of a cell that is in a given row and column
     * @param row row of the cell to target
     * @param column column of the cell to target
     * @return the number of adjacent mines
     */
    public int countAdjacentMines(int row, int column) {
        return countMines(getAdjacentCells(row, column));
    }

    public ImmutableTable<Integer, Integer, Cell> getCells() {
        return cells;
    }
}
