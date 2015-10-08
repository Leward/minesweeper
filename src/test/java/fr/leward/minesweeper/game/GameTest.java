package fr.leward.minesweeper.game;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import fr.leward.minesweeper.components.Board;
import fr.leward.minesweeper.components.Cell;
import fr.leward.minesweeper.exception.CellAlreadyRevealedException;
import fr.leward.minesweeper.exception.OutOfBoardException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameTest {

    @Test(expected = OutOfBoardException.class)
    public void revealCellThatIsOutsideOfBoard() throws OutOfBoardException{
        Game game = new Game(4, 4, 3);
        game.revealCell(-1, 0);
    }

    @Test
    public void cellShouldGetRevealed() {
        Game game = new Game(4, 4, 3);
        game.revealCell(2, 2);
        assertTrue(game.getBoard().getCell(2, 2).get().isRevealed());
    }

    @Test
    public void revealingACellWithNoSurroundingMineShouldRevealSurroundingCells() {
        Game game = new Game(9, 9, 1);
        Board mockedBoard = mock(Board.class);
        game.setBoard(mockedBoard);

        Cell cell_5_5 = new Cell(5, 5, false);
        Cell dummyCell = new Cell(4, 4, false);

        when(mockedBoard.getCell(5, 5)).thenReturn(Optional.of(cell_5_5));
        when(mockedBoard.getCell(4, 4)).thenReturn(Optional.of(dummyCell));
        when(mockedBoard.countAdjacentMines(5, 5)).thenReturn(0);
        when(mockedBoard.countAdjacentMines(4, 4)).thenReturn(1);
        when(mockedBoard.getAdjacentCells(5, 5)).thenReturn(ImmutableList.of(dummyCell)); // List reduced to one neighbours to avoid over complexity in the test
        when(mockedBoard.getCells()).thenReturn((ImmutableTable) ImmutableTable.builder()
                        .put(5, 5, cell_5_5)
                        .put(4, 4, dummyCell)
                        .build()
        );

        game.revealCell(5, 5);
        assertTrue(dummyCell.isRevealed());
        assertTrue(cell_5_5.isRevealed());
    }

    @Test
    public void testRevealAMine() {
        Game game = new Game(5, 5, 2);
        Board mockedBoard = mock(Board.class);
        game.setBoard(mockedBoard);

        Cell cell = new Cell(3, 3, true);
        when(mockedBoard.getCell(3, 3)).thenReturn(Optional.of(cell));
        when(mockedBoard.getCells()).thenReturn(ImmutableTable.of(3, 3, cell));

        game.revealCell(3, 3);
        assertTrue(game.isGameOver());
    }

    @Test
    public void testGameFinished() {
        Game game = new Game(5, 5, 2);
        Board mockedBoard = mock(Board.class);
        game.setBoard(mockedBoard);

        Cell dummyCell = new Cell(0, 0, false);
        when(mockedBoard.getCells()).thenReturn(ImmutableTable.of(0, 0, dummyCell));

        assertFalse(game.determineIfGameIsFinished());

        dummyCell.reveal();
        assertTrue(game.determineIfGameIsFinished());
    }

    @Test(expected = CellAlreadyRevealedException.class)
    public void revealAlreadyRevealedCell() {
        Game game = new Game(5, 5, 2);
        Board mockedBoard = mock(Board.class);
        game.setBoard(mockedBoard);

        Cell dummyCell = new Cell(0, 0, false);
        dummyCell.reveal();
        when(mockedBoard.getCell(0, 0)).thenReturn(Optional.of(dummyCell));

        game.revealCell(0, 0);
    }

}
