package minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

public class Controller {
    final int totalSpaces = 25;
    final int mines = 3;

    private boolean gameOver = false;
    private boolean gameWon = false;

    int clickedSpaces = 0;
    private ImageView[] minesLeft = new ImageView[3];
    private ImageView[] timeElapsed = new ImageView[3];
    private ImageView smileyImage;

    private SpaceItem[][] boardState;
    private Map<ImageView, BoardPair<Integer, Integer>> boardMap;
    private Set<BoardPair<Integer, Integer>> isMineMap;
    public Controller() {
        this.boardState = new SpaceItem[5][5];
        this.boardMap = new HashMap<>();
        this.addMines();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                this.boardState[row][col] = new SpaceItem(this.isMineMap.contains(new BoardPair<>(row, col)));
            }
        }

        // Update the number of adjacent mines for each space
        for (BoardPair<Integer, Integer> mine : this.isMineMap) {
            this.updateMineNeighbors(mine.getKey(), mine.getValue());
        }
    }

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

    private void addMines() {
        this.isMineMap = new HashSet<>() {
            {
                add(new BoardPair<>(1, 1));
                add(new BoardPair<>(2, 3));
                add(new BoardPair<>(3, 3));
            }
        };
    }

    public void addToBoardMap(ImageView imageView, BoardPair<Integer, Integer> coords) {
        this.boardMap.put(imageView, coords);
    }

    ImageView[] getMinesLeft() {
        return this.minesLeft;
    }

    ImageView[] getTimeElapsed() {
        return this.timeElapsed;
    }

    public void setSmileyImage(ImageView smileyImage) {
        this.smileyImage = smileyImage;
    }

    public void onSpaceClicked(ImageView imageView) {
        if (gameOver || gameWon) {
            return;
        }
        this.spaceClicked(imageView);
    }

    private void setGameOver() {
        // Reveal all mines
        this.gameOver = true;
        for (Map.Entry<ImageView, BoardPair<Integer, Integer>> entry : this.boardMap.entrySet()) {
            ImageView imageView = entry.getKey();
            BoardPair<Integer, Integer> coords = entry.getValue();
            SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
            if (space.isMine() && !space.isRevealed()) {
                imageView.setImage(SpaceItem.getUnrevealedMineImage());
            }
        }
        smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-dead.png"));
    }

    private void setGameWon() {
        gameWon = true;
        smileyImage.setImage(new Image("file:src/main/resources/images/minesweeper-basic/face-win.png"));
    }

    protected void spaceClicked(ImageView imageView) {
        // Flip the image over to show the mine or empty space or number
        BoardPair<Integer, Integer> coords = this.boardMap.get(imageView);
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

        clickedSpaces++;
        if (clickedSpaces == totalSpaces - mines) {
            setGameWon();
        }
    }
}