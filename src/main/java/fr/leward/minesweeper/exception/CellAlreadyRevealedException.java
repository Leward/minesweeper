package fr.leward.minesweeper.exception;

public class CellAlreadyRevealedException extends RuntimeException {

    public CellAlreadyRevealedException() {
        super("The cell has been already revealed");
    }
}
