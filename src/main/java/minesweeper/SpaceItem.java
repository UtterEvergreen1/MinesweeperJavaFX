package minesweeper;

import javafx.scene.image.Image;

/**
 * Represents a space on the Minesweeper board.
 * Each space can either be a mine or an empty space with a number of adjacent mines.
 */
public class SpaceItem {
    private final boolean mine;
    private boolean isRevealed;
    private int numAdjacentMines;

    /**
     * Constructor for the SpaceItem class.
     * @param isMine Indicates whether the space is a mine.
     */
    public SpaceItem(boolean isMine) {
        this.mine = isMine;
        this.isRevealed = false;
        this.numAdjacentMines = 0;
    }

    /**
     * Gets the image for an unrevealed mine (different from the revealed red mine image).
     * @return The image for an unrevealed mine.
     */
    static public Image getUnrevealedMineImage() {
        return new Image("file:src/main/resources/images/minesweeper-basic/mine-grey.png");
    }

    /**
     * Gets the image for the revealed state of the space.
     * @return The image for the revealed state of the space.
     */
    public Image getRevealedImage() {
        if (this.mine) {
            return new Image("file:src/main/resources/images/minesweeper-basic/mine-red.png");
        } else {
            return new Image("file:src/main/resources/images/minesweeper-basic/" + this.numAdjacentMines + ".png");
        }
    }

    /**
     * Checks if the space is a mine.
     * @return True if the space is a mine, false otherwise.
     */
    public boolean isMine() {
        return this.mine;
    }

    /**
     * Checks if the space is revealed.
     * @return True if the space is revealed, false otherwise.
     */
    public boolean isRevealed() {
        return this.isRevealed;
    }

    /**
     * Sets the revealed state of the space.
     * @param isRevealed The new revealed state of the space.
     */
    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    /**
     * Gets the number of adjacent mines.
     * @return The number of adjacent mines.
     */
    public int getNumAdjacentMines() {
        return this.numAdjacentMines;
    }

    /**
     * Increments the number of adjacent mines by one.
     */
    public void addAdjacentMine() {
        this.numAdjacentMines++;
    }
}