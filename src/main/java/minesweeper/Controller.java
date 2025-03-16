package minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.*;

public class Controller {
    Difficulty difficulty;
    int clickedSpaces = 0;
    int flaggedMines = 0;

    private boolean gameOver = false;
    private boolean gameWon = false;

    private final ImageView[] minesLeft = new ImageView[3];
    private final ImageView[] timeElapsed = new ImageView[3];
    private ImageView smileyImage;

    private final Map<ImageView, Pair<Integer, Integer>> boardMap;

    private SpaceItem[][] boardState;
    private Set<Pair<Integer, Integer>> isMineMap;

    /**
     * Constructor for the Controller class.
     */
    public Controller() {
        this.boardMap = new HashMap<>(); // Image view stays the same, so no need to reinitialize every game
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Sets up the Minesweeper board.
     */
    public void setup() {
        // Reset the game state
        this.clickedSpaces = 0;
        this.flaggedMines = 0;
        this.gameOver = false;
        this.gameWon = false;

        final int rows = this.difficulty.getRows();
        final int cols = this.difficulty.getCols();

        // Initialize the board with random mines
        this.addMines();

        // Initialize the board state
        this.boardState = new SpaceItem[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.boardState[row][col] = new SpaceItem(this.isMineMap.contains(new Pair<>(row, col)));
            }
        }

        // Update the number of adjacent mines for each space
        for (Pair<Integer, Integer> mine : this.isMineMap) {
            this.updateMineNeighbors(mine.getKey(), mine.getValue());
        }
        this.setNumMinesLeft(this.difficulty.getMines());
        this.smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-smile.png"));
        for (ImageView imageView : this.boardMap.keySet()) {
            imageView.setImage(SpaceItem.getCoverImage());
        }

        //Print out the board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print((this.boardState[i][j].isMine() ? "X" : this.boardState[i][j].getNumAdjacentMines()) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Sets the number of mines left in the header display.
     *
     * @param numMinesLeft The number of mines left.
     */
    private void setNumMinesLeft(int numMinesLeft) {
        int hundreds = numMinesLeft / 100;
        int tens = Math.abs((numMinesLeft % 100) / 10);
        int ones = Math.abs(numMinesLeft % 10);
        if (numMinesLeft < 0) {
            this.minesLeft[0].setImage(new Image("file:src/main/resources/images/digits/neg.png"));
        } else {
            this.minesLeft[0].setImage(new Image("file:src/main/resources/images/digits/" + hundreds + ".png"));
        }
        this.minesLeft[1].setImage(new Image("file:src/main/resources/images/digits/" + tens + ".png"));
        this.minesLeft[2].setImage(new Image("file:src/main/resources/images/digits/" + ones + ".png"));
    }

    /**
     * Updates the number of adjacent mines for the given cell with a mine.
     *
     * @param row The row of the cell with the mine.
     * @param col The column of the cell with the mine.
     */
    private void updateMineNeighbors(int row, int col) {
        int maxRow = this.difficulty.getRows();
        int maxCol = this.difficulty.getCols();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < maxRow && j >= 0 && j < maxCol && !(i == row && j == col)) {
                    if (!this.boardState[i][j].isMine()) {
                        this.boardState[i][j].addAdjacentMine();
                    }
                }
            }
        }
    }

    /**
     * Adds mines to the board.
     */
    private void addMines() {
        if (this.isMineMap == null) {
            this.isMineMap = new HashSet<>();
        }
        this.isMineMap.clear();
        for (int i = 0; i < this.difficulty.getMines(); i++) {
            Random rand = new Random();
            int row = rand.nextInt(this.difficulty.getRows());
            int col = rand.nextInt(this.difficulty.getCols());

            Pair<Integer, Integer> mine = new Pair<>(row, col);
            if (this.isMineMap.contains(mine)) {
                i--;
            } else {
                this.isMineMap.add(mine);
            }
        }
    }

    /**
     * Adds an ImageView and its coordinates to the board map.
     *
     * @param imageView The ImageView to add.
     * @param coords    The coordinates of the ImageView.
     */
    public void addToBoardMap(ImageView imageView, Pair<Integer, Integer> coords) {
        this.boardMap.put(imageView, coords);
    }

    /**
     * Gets the ImageViews representing the number of mines left.
     *
     * @return An array of ImageViews.
     */
    ImageView[] getMinesLeft() {
        return this.minesLeft;
    }

    /**
     * Gets the ImageViews representing the elapsed time.
     *
     * @return An array of ImageViews.
     */
    ImageView[] getTimeElapsed() {
        return this.timeElapsed;
    }

    /**
     * Sets the smiley image.
     *
     * @param smileyImage The ImageView representing the smiley image.
     */
    public void setSmileyImage(ImageView smileyImage) {
        this.smileyImage = smileyImage;
    }

    /**
     * Sets the game state to game over and reveals all mines.
     */
    private void setGameOver() {
        // Reveal all mines
        this.gameOver = true;
        for (Map.Entry<ImageView, Pair<Integer, Integer>> entry : this.boardMap.entrySet()) {
            ImageView imageView = entry.getKey();
            Pair<Integer, Integer> coords = entry.getValue();
            SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
            if (space.isMine() && !space.isRevealed()) {
                imageView.setImage(SpaceItem.getUnrevealedMineImage());
            }
        }
        this.smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-dead.png"));
    }

    /**
     * Sets the game state to game won.
     */
    private void setGameWon() {
        this.gameWon = true;
        this.smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-win.png"));
    }

    /**
     * Handles the event when a space is clicked.
     *
     * @param imageView The ImageView representing the clicked space.
     */
    public void onSpaceClicked(ImageView imageView, boolean leftClick) {
        if (this.gameOver || this.gameWon) {
            return;
        }
        this.spaceClicked(imageView, leftClick);
    }

    /**
     * Handles the logic for when a space is clicked.
     *
     * @param imageView The ImageView representing the clicked space.
     * @param leftClick True if the left mouse button was clicked, false otherwise.
     */
    protected void spaceClicked(ImageView imageView, boolean leftClick) {
        // Get the space at the clicked coordinates
        Pair<Integer, Integer> coords = this.boardMap.get(imageView);
        SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
        if (space.isRevealed()) {
            return;
        }

        if (!leftClick) {
            // Don't allow more than 99 flags + mines
            if (flaggedMines >= this.difficulty.getMines() + 99) {
                return;
            }

            // Place flag and return
            space.setFlagged(!space.isFlagged());

            if (space.isFlagged()) {
                imageView.setImage(SpaceItem.getFlagImage());
                flaggedMines++;
            } else {
                imageView.setImage(SpaceItem.getCoverImage());
                flaggedMines--;
            }
            this.setNumMinesLeft(this.difficulty.getMines() - flaggedMines);
            return;
        }

        if (space.isFlagged()) {
            return;
        }

        space.setRevealed(true);
        imageView.setImage(space.getRevealedImage());

        // Check if the game is over
        if (space.isMine()) {
            setGameOver();
            return;
        }

        // Check if the game is won
        this.clickedSpaces++;
        if (this.clickedSpaces == this.difficulty.getTotalClicks()) {
            setGameWon();
        }
    }
}