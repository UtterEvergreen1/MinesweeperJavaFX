package minesweeper;

public class Difficulty {
    public static final Difficulty BEGINNER = new Difficulty(8, 8, 10);
    public static final Difficulty INTERMEDIATE = new Difficulty(16, 16, 40);
    public static final Difficulty EXPERT = new Difficulty(32, 16, 99);

    private final int rows;
    private final int cols;
    private final int mines;
    private final int totalSpaces;

    public Difficulty(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.totalSpaces = rows * cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMines() {
        return mines;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public int getTotalClicks() {
        return totalSpaces - mines;
    }
}
