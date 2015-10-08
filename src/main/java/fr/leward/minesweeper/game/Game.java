package fr.leward.minesweeper.game;

import com.google.common.collect.ImmutableList;
import fr.leward.minesweeper.components.Board;
import fr.leward.minesweeper.components.Cell;
import fr.leward.minesweeper.exception.CellAlreadyRevealedException;
import fr.leward.minesweeper.exception.GameOverException;
import fr.leward.minesweeper.exception.OutOfBoardException;

/**
 * A minesweeper game
 */
public class Game {

    private int rows;
    private int columns;
    private int nbMines;
    private boolean gameOver = false;
    private boolean gameFinished = false;

    private /*final*/ Board board; // Final has been removed to allow testing and mocking
    private int hitCounter = 0;

    public Game(int rows, int columns, int nbMines) {
        this.rows = rows;
        this.columns = columns;
        this.nbMines = nbMines;
        board = new Board(rows, columns, nbMines);
    }

    /**
     * Reveal a cell and increase the hit counter
     * @param row
     * @param column
     * @throws OutOfBoardException
     * @throws CellAlreadyRevealedException
     */
    public void revealCell(int row, int column) throws OutOfBoardException, CellAlreadyRevealedException {
        revealCellInternal(row, column);
        hitCounter++;
    }

    /**
     * Reveal a cell
     * A call to this method alone does not increase the hit counter
     * Default visibility is used to enable tests
     * @param row
     * @param column
     * @throws OutOfBoardException
     * @throws CellAlreadyRevealedException
     */
    void revealCellInternal(int row, int column) throws OutOfBoardException, CellAlreadyRevealedException {
        // Cannot reveal a cell if game is over
        if(gameOver) {
            throw new GameOverException();
        }

        Cell cell = board.getCell(row, column).orElseThrow(OutOfBoardException::new);
        // Cannot reveal a cell that is already revealed
        if(cell.isRevealed()) {
            throw new CellAlreadyRevealedException();
        }
        cell.reveal();

        // Revealing a mine means the game is over
        if(cell.isMine()) {
            gameOver = true;
        }
        // If no adjacent mine, also reveal surrounding cells
        else if(board.countAdjacentMines(row, column) == 0) {
            ImmutableList<Cell> adjacentCells = board.getAdjacentCells(row, column);
            for(Cell adjacentCell : adjacentCells) {
                try {
                    revealCell(adjacentCell.getRow(), adjacentCell.getColumn());
                }
                catch (OutOfBoardException | CellAlreadyRevealedException e) {
                    // It's no big deal here, it will just prevent further recursion to happen
                }
            }
        }
        gameFinished = determineIfGameIsFinished();
    }

    /**
     * Default visibility for tests
     * @return
     */
     boolean determineIfGameIsFinished() {
        for(Cell cell : board.getCells().values()) {
            if(!cell.isMine() && !cell.isRevealed()) {
                return false;
            }
        }
        return true;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNbMines() {
        return nbMines;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Set the board of the game
     * This is intended to be used only by tests, hence its default visibility
     * @param board
     */
    void setBoard(Board board) {
        this.board = board;
    }

    public int getHitCounter() {
        return hitCounter;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }
}
