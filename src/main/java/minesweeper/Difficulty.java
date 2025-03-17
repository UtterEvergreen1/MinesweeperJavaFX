package minesweeper;

/**
 * Represents the difficulty level of the Minesweeper game.
 * Each difficulty level has a specific number of rows, columns, and mines.
 */
public class Difficulty {
    public static final Difficulty BEGINNER = new Difficulty(8, 8, 10);
    public static final Difficulty INTERMEDIATE = new Difficulty(16, 16, 40);
    public static final Difficulty EXPERT = new Difficulty(32, 16, 99);

    private final int rows;
    private final int cols;
    private final int mines;
    private final int totalSpaces;

    /**
     * Constructor for the Difficulty class.
     * @param rows The number of rows in the game board.
     * @param cols The number of columns in the game board.
     * @param mines The number of mines in the game board.
     */
    public Difficulty(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.totalSpaces = rows * cols;
    }

    /**
     * Gets the number of rows in the game board.
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the game board.
     * @return The number of columns.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Gets the number of mines in the game board.
     * @return The number of mines.
     */
    public int getMines() {
        return mines;
    }

    /**
     * Gets the total number of spaces in the game board.
     * @return The total number of spaces.
     */
    public int getTotalSpaces() {
        return totalSpaces;
    }

    /**
     * Gets the total number of clicks required to win the game.
     * @return The total number of clicks required to win.
     */
    public int getTotalClicks() {
        return totalSpaces - mines;
    }
}