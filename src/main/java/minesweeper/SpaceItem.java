package minesweeper;

import javafx.scene.image.Image;

public class SpaceItem {
    private final boolean isMine;
    private boolean isRevealed;
    private int numAdjacentMines;

    public SpaceItem(boolean isMine) {
        this.isMine = isMine;
        this.isRevealed = false;
        this.numAdjacentMines = 0;
    }

    static public Image getUnrevealedMineImage() {
        return new Image("file:src/main/resources/images/minesweeper-basic/mine-grey.png");
    }

    public Image getRevealedImage() {
        if (this.isMine) {
            return new Image("file:src/main/resources/images/minesweeper-basic/mine-red.png");
        } else {
            return new Image("file:src/main/resources/images/minesweeper-basic/" + this.numAdjacentMines + ".png");
        }
    }

    public boolean isMine() {
        return this.isMine;
    }

    public boolean isRevealed() {
        return this.isRevealed;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public int getNumAdjacentMines() {
        return this.numAdjacentMines;
    }

    public void addAdjacentMine() {
        this.numAdjacentMines++;
    }
}
