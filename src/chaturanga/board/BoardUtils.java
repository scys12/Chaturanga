package chaturanga.board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] FOURTH_COLUMN = initColumn(3);

    public static final boolean[] EIGTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(4);
    public static final boolean[] SIXTH_RANK = initRow(8);
    public static final boolean[] FIFTH_RANK = initRow(12);
    public static final boolean[] FOURTH_RANK = initRow(16);
    public static final boolean[] THIRD_RANK = initRow(20);
    public static final boolean[] SECOND_RANK = initRow(24);
    public static final boolean[] FIRST_RANK = initRow(28);

    public static final int NUM_TILES = 32;
    public static final int NUM_TILES_PER_ROW = 4;

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me");
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    public static boolean isEndGame(final Board board) {
        return board.currentPlayer().isInCheckMate();
    }
}
