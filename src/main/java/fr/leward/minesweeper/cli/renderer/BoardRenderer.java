package fr.leward.minesweeper.cli.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import fr.leward.minesweeper.components.Board;
import fr.leward.minesweeper.components.Cell;

public class BoardRenderer {

    public String renderBoardAsString(Board board) {
        StringBuilder sb = new StringBuilder();

        for(int row : board.getCells().rowKeySet()) {
            ImmutableMap<Integer, Cell> columns = board.getCells().row(row);
            for(int column : columns.keySet()) {
                Cell cell = board.getCell(row, column).get();
                if(!cell.isRevealed()) {
                    sb.append("?");
                }
                else if(cell.isMine()) {
                    sb.append("X");
                }
                else {
                    sb.append(board.countAdjacentMines(row, column));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
