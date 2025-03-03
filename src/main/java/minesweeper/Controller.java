package minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.*;

public class Controller {
    final int totalSpaces = 25;
    final int mines = 3;
    int clickedSpaces = 0;

    private boolean gameOver = false;
    private boolean gameWon = false;

    private final ImageView[] minesLeft = new ImageView[3];
    private final ImageView[] timeElapsed = new ImageView[3];
    private ImageView smileyImage;

    private SpaceItem[][] boardState;
    private Map<ImageView, Pair<Integer, Integer>> boardMap;
    private Set<Pair<Integer, Integer>> isMineMap;

    /**
     * Constructor for the Controller class.
     * Initializes the board with mines and updates the board state.
     */
    public Controller() {
        setup();
    }

    public void setup() {
        // Initialize the board with mines
        this.boardState = new SpaceItem[5][5];
        this.boardMap = new HashMap<>();
        this.addMines();

        // Initialize the board state
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                this.boardState[row][col] = new SpaceItem(this.isMineMap.contains(new Pair<>(row, col)));
            }
        }

        // Update the number of adjacent mines for each space
        for (Pair<Integer, Integer> mine : this.isMineMap) {
            this.updateMineNeighbors(mine.getKey(), mine.getValue());
        }
    }

    /**
     * Updates the number of adjacent mines for the given cell with a mine.
     * @param row The row of the cell with the mine.
     * @param col The column of the cell with the mine.
     */
    private void updateMineNeighbors(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < 5 && j >= 0 && j < 5 && !(i == row && j == col)) {
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
        this.isMineMap = new HashSet<>() {
            {
                add(new Pair<>(1, 1));
                add(new Pair<>(2, 3));
                add(new Pair<>(3, 3));
            }
        };
    }

    /**
     * Adds an ImageView and its coordinates to the board map.
     * @param imageView The ImageView to add.
     * @param coords The coordinates of the ImageView.
     */
    public void addToBoardMap(ImageView imageView, Pair<Integer, Integer> coords) {
        this.boardMap.put(imageView, coords);
    }

    /**
     * Gets the ImageViews representing the number of mines left.
     * @return An array of ImageViews.
     */
    ImageView[] getMinesLeft() {
        return this.minesLeft;
    }

    /**
     * Gets the ImageViews representing the elapsed time.
     * @return An array of ImageViews.
     */
    ImageView[] getTimeElapsed() {
        return this.timeElapsed;
    }

    /**
     * Sets the smiley image.
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
        smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-dead.png"));
    }

    /**
     * Sets the game state to game won.
     */
    private void setGameWon() {
        gameWon = true;
        smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-win.png"));
    }

    /**
     * Handles the event when a space is clicked.
     * @param imageView The ImageView representing the clicked space.
     */
    public void onSpaceClicked(ImageView imageView) {
        if (gameOver || gameWon) {
            return;
        }
        this.spaceClicked(imageView);
    }

    /**
     * Handles the logic for when a space is clicked.
     * @param imageView The ImageView representing the clicked space.
     */
    protected void spaceClicked(ImageView imageView) {
        // Get the space at the clicked coordinates
        Pair<Integer, Integer> coords = this.boardMap.get(imageView);
        SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
        if (space.isRevealed()) {
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
        clickedSpaces++;
        if (clickedSpaces == totalSpaces - mines) {
            setGameWon();
        }
    }
}