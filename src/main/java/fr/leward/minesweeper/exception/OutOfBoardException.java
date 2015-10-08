package fr.leward.minesweeper.exception;

public class OutOfBoardException extends RuntimeException {

    public OutOfBoardException() {
    }

    public OutOfBoardException(String message) {
        super(message);
    }

    public OutOfBoardException(String message, Throwable cause) {
        super(message, cause);
    }
}
