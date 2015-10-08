package fr.leward.minesweeper.exception;

public class GameOverException extends RuntimeException {

    public GameOverException() {
        this("The game is over. ");
    }

    public GameOverException(String message) {
        super(message);
    }
}
