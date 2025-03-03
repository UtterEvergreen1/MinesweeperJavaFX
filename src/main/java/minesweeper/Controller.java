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
     */
    public Controller() {
        this.boardState = new SpaceItem[5][5];
        this.boardMap = new HashMap<>();
    }

    /**
     * Sets up the Minesweeper board.
     */
    public void setup() {
        // Reset the game state
        this.clickedSpaces = 0;
        this.gameOver = false;
        this.gameWon = false;

        // Initialize the board with mines
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
        this.setNumMinesLeft(this.mines);
    }

    /**
     * Sets the number of mines left in the header display.
     * @param numMinesLeft The number of mines left.
     */
    private void setNumMinesLeft(int numMinesLeft) {
        int hundreds = numMinesLeft / 100;
        int tens = (numMinesLeft % 100) / 10;
        int ones = numMinesLeft % 10;
        this.minesLeft[0].setImage(new Image("file:src/main/resources/images/digits/" + hundreds + ".png"));
        this.minesLeft[1].setImage(new Image("file:src/main/resources/images/digits/" + tens + ".png"));
        this.minesLeft[2].setImage(new Image("file:src/main/resources/images/digits/" + ones + ".png"));
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
     * @param imageView The ImageView representing the clicked space.
     */
    public void onSpaceClicked(ImageView imageView) {
        if (this.gameOver || this.gameWon) {
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
        this.clickedSpaces++;
        if (this.clickedSpaces == this.totalSpaces - this.mines) {
            setGameWon();
        }
    }
}