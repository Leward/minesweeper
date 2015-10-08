package fr.leward.minesweeper.components;

import org.junit.Test;
import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void testCellWithMineHasMine() {
        Cell cell = new Cell(4, 2, true);
        assertEquals(true, cell.isMine());
    }

    @Test
    public void testCellWithNoMineHasNoMine() {
        Cell cell = new Cell(4, 2, false);
        assertEquals(false, cell.isMine());
    }

    public void testCellCoordinates() {
        Cell cell = new Cell(4, 2);
        assertEquals(4, cell.getRow());
        assertEquals(2, cell.getColumn());
    }

    @Test
    public void cellShouldNotBeRevealedByDefault() {
        Cell cell = new Cell(0, 0);
        assertEquals(false, cell.isRevealed());
    }

    @Test
    public void cellShouldGetRevealed() {
        Cell cell = new Cell(4, 2);
        assertEquals(false, cell.isRevealed());
        cell.reveal();
        assertEquals(true, cell.isRevealed());
    }

}
