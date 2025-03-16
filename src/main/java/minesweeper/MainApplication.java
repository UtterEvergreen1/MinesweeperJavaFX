package minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main application class for the Minesweeper game.
 * Sets up the game board and handles the user interface.
 */
public class MainApplication extends Application {
    private final Controller controller = new Controller();

    /**
     * Starts the application and sets up the game board.
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        // Combines the header and game area into a single VBox as the root pane
        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(false);
        root.setStyle("-fx-border-color: #bfbfbf #888888 #888888 #bfbfbf; -fx-border-width: 5; -fx-padding: 10; -fx-background-color: #999999; -fx-alignment: center;");

        // Header of Minesweeper
        HBox header = new HBox();
        header.setSpacing(10);
        this.add3DBorder(header);

        // Add digits for mines left
        this.makeDigits(header, controller.getMinesLeft());

        // Smiley face
        Image smileyImage = new Image("file:src/main/resources/images/minesweeper-basic/face-smile.png");
        ImageView smileyImageView = new ImageView(smileyImage);
        controller.setSmileyImage(smileyImageView);
        smileyImageView.setFitWidth(52);
        smileyImageView.setFitHeight(52);
        smileyImageView.setOnMouseClicked(event -> this.resetGame());
        header.getChildren().add(smileyImageView);

        // Add digits for time elapsed
        this.makeDigits(header, controller.getTimeElapsed());

        Difficulty difficulty = Difficulty.BEGINNER;
        final int rows = difficulty.getRows();
        final int cols = difficulty.getCols();

        GridPane gridPane = this.setupBoard(rows, cols);
        controller.setDifficulty(difficulty);
        controller.setup();

        root.getChildren().add(header);
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 280, 360);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");
        stage.show();
    }

    private GridPane setupBoard(int rows, int cols) {
        // Game area
        GridPane gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        this.add3DBorder(gridPane);

        // Create a XY grid of images for the game area
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageView imageView = getSpaceView(240 / cols);
                gridPane.add(imageView, col, row);
                controller.addToBoardMap(imageView, new Pair<>(row, col));
            }
        }

        return gridPane;
    }

    private void resetGame() {
        controller.setup();
    }

    /**
     * Creates an ImageView for a space on the game board.
     * @return The ImageView for the space.
     */
    private ImageView getSpaceView(int size) {
        Image image = new Image("file:src/main/resources/images/minesweeper-basic/cover.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setOnMouseClicked(event -> {
            boolean leftClick = event.getButton() == MouseButton.PRIMARY;
            if (!leftClick && event.getButton() != MouseButton.SECONDARY) {
                return;
            }
            controller.onSpaceClicked(imageView, leftClick);
        });
        return imageView;
    }

    /**
     * Adds a 3D border style to the given pane.
     * @param pane The pane to style.
     */
    private void add3DBorder(Pane pane) {
        pane.setStyle("-fx-border-color: #888888 #bfbfbf #bfbfbf #888888; -fx-border-width: 5; -fx-alignment: center;");
    }

    /**
     * Creates and adds digit ImageViews to the header.
     * @param header The header to add the digits to.
     * @param digits The array that stores the digit ImageViews.
     */
    private void makeDigits(HBox header, ImageView[] digits) {
        HBox digitsLeft = new HBox();
        for (int imageNum = 0; imageNum < 3; imageNum++) {
            Image image = new Image("file:src/main/resources/images/digits/0.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(28);
            imageView.setFitHeight(50);
            digitsLeft.getChildren().add(imageView);
            digits[imageNum] = imageView;
        }
        digitsLeft.setStyle("-fx-alignment: bottom-center;");
        header.getChildren().add(digitsLeft);
    }

    /**
     * The main method to launch the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}