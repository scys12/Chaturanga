package chaturanga;

import chaturanga.board.Board1;
import chaturanga.gui.Table;

public class Chaturanga {
    public static void main(String[] args) {
        Board1 board = Board1.createStandardBoard();

        System.out.println(board);
        Table table = new Table();
    }
}