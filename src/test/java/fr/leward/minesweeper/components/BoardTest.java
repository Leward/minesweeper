package fr.leward.minesweeper.components;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import fr.leward.minesweeper.components.Board;
import fr.leward.minesweeper.exception.TooManyMinesException;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class BoardTest {

    @Test
    public void boardWith4WidthAnd4HeightShouldHave16Cells() {
        Board board = new Board(4, 4, 0);
        assertEquals(16, board.getCells().size());
    }

    @Test
    public void boardShouldHaveTheRightAmountOfMines() {
        Board board = new Board(4, 4, 0);
        assertEquals(0, countMinesOnBoard(board));
        board = new Board(4, 4, 5);
        assertEquals(5, countMinesOnBoard(board));
        board = new Board(4, 4, 15);
        assertEquals(15, countMinesOnBoard(board));
    }

    @Test(expected = TooManyMinesException.class)
    public void boardWithTooManyMainesShouldThrowException() {
        new Board(4, 4, 50);
    }

    @Test
    public void shouldGetCellsInBoard() {
        Board board = new Board(4, 4, 4);
        assertEquals(true, board.getCell(0, 0).isPresent());
        assertEquals(true, board.getCell(3, 3).isPresent());
        assertEquals(true, board.getCell(2, 1).isPresent());
        assertEquals(true, board.getCell(0, 3).isPresent());
    }

    @Test
    public void shouldNotGetCellsOutsideOfBoard() {
        Board board = new Board(4, 4, 4);
        assertEquals(false, board.getCell(-1, 0).isPresent());
        assertEquals(false, board.getCell(-1, -1).isPresent());
        assertEquals(false, board.getCell(0, -6).isPresent());
        assertEquals(false, board.getCell(4, 4).isPresent());
        assertEquals(false, board.getCell(6, 1).isPresent());
        assertEquals(false, board.getCell(23, 9).isPresent());
    }

    @Test
    public void testGetAdjacentCellsInMiddleOfBoard() {
        Board mockedBoard = mock(Board.class);
        when(mockedBoard.getCell(1, 1)).thenReturn(Optional.of(new Cell(1,1)));
        when(mockedBoard.getCell(1, 2)).thenReturn(Optional.of(new Cell(1,2)));
        when(mockedBoard.getCell(1, 3)).thenReturn(Optional.of(new Cell(1,3)));
        when(mockedBoard.getCell(2, 1)).thenReturn(Optional.of(new Cell(2,1)));
        when(mockedBoard.getCell(2, 3)).thenReturn(Optional.of(new Cell(2,3)));
        when(mockedBoard.getCell(3, 1)).thenReturn(Optional.of(new Cell(3,1)));
        when(mockedBoard.getCell(3, 2)).thenReturn(Optional.of(new Cell(3,2)));
        when(mockedBoard.getCell(3, 3)).thenReturn(Optional.of(new Cell(3,3)));
        when(mockedBoard.getAdjacentCells(2, 2)).thenCallRealMethod();
        assertEquals(8, mockedBoard.getAdjacentCells(2, 2).size());
    }

    @Test
    public void testGetAdjacentCellsInEdgeOfBoard() {
        Board mockedBoard = mock(Board.class);
        when(mockedBoard.getCell(-1, 0)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(-1, 1)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(-1, 2)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(0, 0)).thenReturn(Optional.of(new Cell(0,0)));
        when(mockedBoard.getCell(0, 2)).thenReturn(Optional.of(new Cell(0,2)));
        when(mockedBoard.getCell(1, 0)).thenReturn(Optional.of(new Cell(1,0)));
        when(mockedBoard.getCell(1, 1)).thenReturn(Optional.of(new Cell(1,1)));
        when(mockedBoard.getCell(1, 2)).thenReturn(Optional.of(new Cell(1,2)));
        when(mockedBoard.getAdjacentCells(0, 1)).thenCallRealMethod();
        assertEquals(5, mockedBoard.getAdjacentCells(0, 1).size());
    }

    @Test
    public void testGetAdjacentCellsInCornerOfBoard() {
        Board mockedBoard = mock(Board.class);
        when(mockedBoard.getCell(-1, -1)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(-1, 0)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(-1, 1)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(0, -1)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(0, 1)).thenReturn(Optional.of(new Cell(0,1)));
        when(mockedBoard.getCell(1, -1)).thenReturn(Optional.empty());
        when(mockedBoard.getCell(1, 0)).thenReturn(Optional.of(new Cell(1,0)));
        when(mockedBoard.getCell(1, 1)).thenReturn(Optional.of(new Cell(1,1)));
        when(mockedBoard.getAdjacentCells(0, 0)).thenCallRealMethod();
        assertEquals(3, mockedBoard.getAdjacentCells(0, 0).size());
    }

    @Test
    public void testCountMinesInCollection() {
        List<Cell> cells =Lists.newArrayList(new Cell(0,0), new Cell(0,0), new Cell(0,0,true));
        assertEquals(1, Board.countMines(cells));
        cells = Lists.newArrayList(new Cell(0,0), new Cell(0,0), new Cell(0,0));
        assertEquals(0, Board.countMines(cells));
        cells =Lists.newArrayList(new Cell(0,0,true), new Cell(0,0,true), new Cell(0,0,true));
        assertEquals(3, Board.countMines(cells));
    }

    @Test
    public void testCountAdjacentMines() {
        Board boardMock = mock(Board.class);
        when(boardMock.getAdjacentCells(0, 0)).thenReturn(ImmutableList.copyOf(Lists.newArrayList(new Cell(0,0), new Cell(0,0,true), new Cell(0,0))));
        when(boardMock.countAdjacentMines(0, 0)).thenCallRealMethod();
        // Static method Board.countMines is not mocked and mockito does not support testing of static methods
        // Moreover, countAdjacentMines is a composition of two functions that are already unit tested
        assertEquals(1, boardMock.countAdjacentMines(0, 0));
    }

    private int countMinesOnBoard(Board board) {
        return board.getCells().cellSet()
                .stream()
                .map(tableCell -> (tableCell.getValue().isMine()) ? 1 : 0)
                .reduce(0, (a, b) -> a + b)
                .intValue();
    }

}
