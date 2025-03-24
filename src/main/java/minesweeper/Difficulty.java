package minesweeper;

/**
 * Represents the difficulty level of the Minesweeper game.
 * Each difficulty level has a specific number of rows, columns, and mines.
 */
public enum Difficulty {
    BEGINNER("Beginner", 8, 8, 10, 30, 310, 425),
    INTERMEDIATE("Intermediate", 16, 16, 40, 30, 575, 670),
    EXPERT("Expert", 16, 32, 99, 25, 945, 600);
    public static Difficulty fromString(String s) {
        return switch (s) {
            case "Beginner" -> BEGINNER;
            case "Intermediate" -> INTERMEDIATE;
            case "Expert" -> EXPERT;
            default -> null;
        };
    }

    private final String name;
    private final int rows;
    private final int cols;
    private final int mines;
    private final int tileSize;
    private final int screenWidth;
    private final int screenHeight;
    private final int totalSpaces;

    /**
     * Constructor for the Difficulty class.
     * @param rows The number of rows in the game board.
     * @param cols The number of columns in the game board.
     * @param mines The number of mines in the game board.
     */
    Difficulty(String name, int rows, int cols, int mines, int tileSize, int screenWidth, int screenHeight) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.tileSize = tileSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.totalSpaces = rows * cols;
    }

    public String getName() {
        return name;
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

    public int getTileSize() {
        return tileSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
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

    @Override
    public String toString() {
        return name;
    }
}