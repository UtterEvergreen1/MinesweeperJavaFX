package minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main application class for the Minesweeper game.
 * Sets up the game board and handles the user interface.
 */
public class MainApplication extends Application {
    private final Controller controller = new Controller();
    private Stage mainStage;
    private Scene mainScene;
    private StackPane root;
    private VBox gameRoot;
    private VBox menu;
    private TextField nameField;
    private VBox highScoreInput;
    private Text highScoreText;
    private VBox highScoreDisplay;
    private Text highScoreDisplayText;
    private HBox header;

    /**
     * Creates the header for the Minesweeper game.
     */
    private void makeHeader() {
        // Header of Minesweeper
        this.header = new HBox();
        this.header.setSpacing(10);
        this.add3DBorder(header);

        // Add digits for mines left
        this.makeDigits(header, this.controller.getMinesLeft(), true);

        // Smiley face
        Image smileyImage = new Image("file:src/main/resources/images/minesweeper-basic/face-smile.png");
        ImageView smileyImageView = new ImageView(smileyImage);
        this.controller.setSmileyImage(smileyImageView);
        smileyImageView.setFitWidth(52);
        smileyImageView.setFitHeight(52);
        smileyImageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.resetGame();
            }
        });
        this.header.getChildren().add(smileyImageView);

        // Add digits for time elapsed
        this.makeDigits(this.header, this.controller.getTimeElapsed(), false);
    }

    /**
     * Creates the menu for selecting the difficulty level.
     */
    private void makeMenu() {
        // Create the difficulty selection menu
        this.menu = new VBox(10);
        this.menu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20; -fx-alignment: center;");
        // Text for the menu "Choose a difficulty"
        Text text = new Text("Choose a difficulty");
        text.setStyle("-fx-fill: white; -fx-font-size: 20;");
        Button beginnerButton = new Button("Beginner");
        Button intermediateButton = new Button("Intermediate");
        Button expertButton = new Button("Expert");

        beginnerButton.setStyle("-fx-font-size: 16; -fx-background-color: #00ff00; -fx-text-fill: #1e1f22;");
        intermediateButton.setStyle("-fx-font-size: 16; -fx-background-color: #ffcc00; -fx-text-fill: #1e1f22;");
        expertButton.setStyle("-fx-font-size: 16; -fx-background-color: #ff0000; -fx-text-fill: #1e1f22;");
        beginnerButton.setOnAction(event -> setDifficulty(Difficulty.BEGINNER));
        intermediateButton.setOnAction(event -> setDifficulty(Difficulty.INTERMEDIATE));
        expertButton.setOnAction(event -> setDifficulty(Difficulty.EXPERT));

        this.menu.getChildren().addAll(text, beginnerButton, intermediateButton, expertButton);
    }

    /**
     * Creates the input for submitting a high score.
     */
    private void makeHighScoreInput() {
        // Create the high score input VBox
        this.highScoreInput = new VBox(10);
        this.highScoreInput.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20; -fx-alignment: center;");

        this.highScoreText = new Text();
        this.highScoreText.setStyle("-fx-fill: white; -fx-font-size: 20; -fx-alignment: center;");
        this.highScoreText.setWrappingWidth(250);
        this.nameField = new TextField();
        this.nameField.setPromptText("Your name");
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            this.updateHighScores();
        });

        this.controller.setHighScoreInput(this.highScoreInput, this.highScoreText);
        this.highScoreInput.getChildren().addAll(this.highScoreText, this.nameField, submitButton);
        this.highScoreInput.setVisible(false);
    }

    /**
     * Creates the display for showing the high scores.
     */
    private void makeHighScoreDisplay() {
        // Create the high score display VBox
        this.highScoreDisplay = new VBox(10);
        this.highScoreDisplay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20; -fx-alignment: center;");
        Text text = new Text("High Scores");
        text.setStyle("-fx-fill: white; -fx-font-size: 20; -fx-font-family: monospace; -fx-alignment: center;");
        this.highScoreDisplay.getChildren().add(text);

        this.highScoreDisplayText = new Text(HighScore.getHighScoreText());
        this.highScoreDisplayText.setStyle("-fx-fill: white; -fx-font-size: 12; -fx-font-family: monospace; -fx-alignment: center;");
        this.highScoreDisplay.getChildren().add(this.highScoreDisplayText);

        HBox highScoreButtons = new HBox(10);
        highScoreButtons.setStyle("-fx-alignment: center;");
        this.highScoreDisplay.getChildren().add(highScoreButtons);

        // Create a button to reset high scores
        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-font-size: 16; -fx-background-color: #ff0000; -fx-text-fill: #1e1f22; -fx-alignment: center;");
        resetButton.setOnAction(event -> {
            HighScore.resetHighScores();
            this.highScoreDisplayText.setText("No high scores yet!");
        });
        highScoreButtons.getChildren().add(resetButton);

        // Create a button to close the high score display
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 16; -fx-background-color: #ff0000; -fx-text-fill: #1e1f22; -fx-alignment: center;");
        closeButton.setOnAction(event -> this.toggleHighScores());
        highScoreButtons.getChildren().add(closeButton);

        this.highScoreDisplay.setVisible(false);
    }

    /**
     * Creates the root pane for the game.
     */
    private void makeGameRoot() {
        // Combines the header and game area into a single VBox as the root pane
        this.gameRoot = new VBox();
        this.gameRoot.setSpacing(10);
        this.gameRoot.setVisible(false);
        this.gameRoot.setStyle("-fx-border-color: #dfdfdf #888888 #888888 #dfdfdf; -fx-border-width: 4; -fx-padding: 10; -fx-background-color: #999999; -fx-alignment: center;");

        this.makeHeader();
        this.gameRoot.getChildren().add(this.header);

        // Create the default game board
        GridPane gridPane = this.setupBoard(0, 0, 1); // Initially empty
        this.gameRoot.getChildren().add(gridPane);

        this.makeMenu();
        this.makeHighScoreInput();
        this.makeHighScoreDisplay();
    }

    /**
     * Creates the main scene for the application.
     */
    private void makeScene() {
        this.mainScene = new Scene(this.root, 280, 360);
        this.mainScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.H) {
                toggleHighScores();
            }
        });
        this.mainStage.setScene(this.mainScene);
        this.mainStage.setTitle("Minesweeper");
        this.mainStage.show();
    }

    /**
     * Combines the game board, menu, and high score input into a single stacked root pane.
     */
    private void makeRoot() {
        // Overlay the menu and high score input on top of the game board with a StackPane
        this.root = new StackPane();
        this.root.getChildren().addAll(this.gameRoot, this.menu, this.highScoreInput, this.highScoreDisplay);
    }

    /**
     * Starts the application and sets up the game board.
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        HighScore.readHighScores();

        this.makeGameRoot();
        this.makeRoot();
        this.makeScene();
    }

    /**
     * Updates the high scores with the player's name and time.
     */
    private void updateHighScores() {
        String playerName = this.nameField.getText();
        // Handle high score submission
        this.highScoreInput.setVisible(false);
        this.menu.setVisible(true);
        this.saveHighScore(playerName, this.controller.getTimeElapsedSeconds(), this.controller.getDifficulty());
        this.highScoreDisplayText.setText(HighScore.getHighScoreText());
    }

    /**
     * Toggles the visibility of the high score display.
     */
    private void toggleHighScores() {
        if (this.highScoreDisplay.isVisible()) {
            this.highScoreDisplay.setVisible(false);
            this.controller.resumeGame();
            return;
        }

        this.controller.pauseGame();
        this.highScoreDisplay.setVisible(true);
    }

    /**
     * Saves the high score to the high score list.
     * @param playerName The name of the player.
     * @param score The score of the player.
     * @param difficulty The difficulty level of the game.
     */
    private void saveHighScore(String playerName, int score, Difficulty difficulty) {
        HighScore.addHighScore(playerName, score, difficulty);
        HighScore.writeHighScores();
    }

    /**
     * Sets up the game board with the given number of rows and columns.
     * @param rows The number of rows in the game board.
     * @param cols The number of columns in the game board.
     * @param tileSize The size of each tile in the game board.
     * @return The game board as a GridPane.
     */
    private GridPane setupBoard(int rows, int cols, int tileSize) {
        // Game area
        GridPane gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        this.add3DBorder(gridPane);

        // Create a XY grid of images for the game area
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageView imageView = getSpaceView(tileSize);
                gridPane.add(imageView, col, row);
                this.controller.addToBoardMap(imageView, new Pair<>(row, col));
            }
        }

        return gridPane;
    }

    /**
     * Resets the game to the initial state.
     */
    private void resetGame() {
        this.controller.setup();
    }

    /**
     * Sets the difficulty level of the game.
     * @param difficulty The difficulty level to set.
     */
    private void setDifficulty(Difficulty difficulty) {
        this.controller.setDifficulty(difficulty);
        this.controller.clearBoardMap();
        this.mainStage.setWidth(difficulty.getScreenWidth());
        this.mainStage.setHeight(difficulty.getScreenHeight());
        this.mainStage.setResizable(false);
        GridPane newBoard = setupBoard(difficulty.getRows(), difficulty.getCols(), difficulty.getTileSize());
        ((VBox) this.root.getChildren().getFirst()).getChildren().set(1, newBoard);
        this.menu.setVisible(false);
        this.gameRoot.setVisible(true);
        resetGame();
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
            this.controller.onSpaceClicked(imageView, leftClick);
        });
        imageView.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boolean leftClick = event.getButton() == MouseButton.PRIMARY;
                this.controller.spaceClickDown(imageView, leftClick);
            }
        });
        imageView.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.controller.spaceClickUp(imageView);
            }
        });
        return imageView;
    }

    /**
     * Adds a 3D border style to the given pane.
     * @param pane The pane to style.
     */
    private void add3DBorder(Pane pane) {
        pane.setStyle("-fx-border-color: #888888 #dfdfdf #dfdfdf #888888; -fx-border-width: 4; -fx-alignment: center;");
    }

    /**
     * Creates and adds digit ImageViews to the header.
     * @param header The header to add the digits to.
     * @param digits The array that stores the digit ImageViews.
     */
    private void makeDigits(HBox header, ImageView[] digits, boolean isLeft) {
        HBox digitsLeft = new HBox();
        // fill the width
        for (int imageNum = 0; imageNum < 3; imageNum++) {
            Image image = new Image("file:src/main/resources/images/digits/0.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(28);
            imageView.setFitHeight(50);
            digitsLeft.getChildren().add(imageView);
            digits[imageNum] = imageView;
        }
        digitsLeft.setStyle("-fx-alignment: bottom-" + (isLeft ? "left" : "right") + ";");
        HBox.setHgrow(digitsLeft, javafx.scene.layout.Priority.ALWAYS);
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