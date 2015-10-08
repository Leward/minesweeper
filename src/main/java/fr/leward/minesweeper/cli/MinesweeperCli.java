package fr.leward.minesweeper.cli;

import fr.leward.minesweeper.cli.renderer.BoardRenderer;
import fr.leward.minesweeper.exception.CellAlreadyRevealedException;
import fr.leward.minesweeper.exception.OutOfBoardException;
import fr.leward.minesweeper.game.Game;

import java.util.Scanner;

public class MinesweeperCli {

    public void run() {
        // Init game
        Scanner sc = new Scanner(System.in);

        int rows = -1;
        do {
            System.out.print("Number of rows? ");
            rows = sc.nextInt();
            if(rows < 1) {
                rows = -1;
                System.out.println("Number of rows is invalid");
            }
        } while(rows == -1);

        int columns = -1;
        do {
            System.out.print("Number of columns? ");
            columns = sc.nextInt();
            if(columns < 1) {
                columns = -1;
                System.out.println("Number of columns is invalid");
            }
        } while(columns == -1);

        int nbMines = -1;
        do {
            System.out.print("Number of mines ?");
            nbMines = sc.nextInt();
            if(nbMines < 1 || nbMines > (rows * columns)) {
                nbMines = -1;
                System.out.println("Number of mines is invalid");
            }
        } while(nbMines == -1);

        Game game = new Game(rows, columns, nbMines);
        BoardRenderer boardRenderer = new BoardRenderer();

        // Main game loop
        do {
            System.out.println(boardRenderer.renderBoardAsString(game.getBoard()));

            System.out.println("Choose cell to reveal. (indexes start at 0)");
            System.out.print("Which row?");
            int rowToReveal = sc.nextInt();
            System.out.print("Which column?");
            int columnToReveal = sc.nextInt();

            try {
                game.revealCell(rowToReveal, columnToReveal);
            }
            catch (OutOfBoardException e) {
                System.out.println("Cell is outside the board");
            }
            catch (CellAlreadyRevealedException e) {
                System.out.println("Cell has already been revealed.");
            }
        }
        while(!game.isGameFinished() && !game.isGameOver());

        // Main loop exited. Display the board one more time to show the result
        System.out.println(boardRenderer.renderBoardAsString(game.getBoard()));

        // Either the player won, or lost :)
        if(game.isGameOver()) {
            System.out.println("The game is over");
        }
        else if(game.isGameFinished()) {
            System.out.println("You won!!!");
        }
    }
}
