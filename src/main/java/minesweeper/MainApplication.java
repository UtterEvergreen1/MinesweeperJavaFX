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

import java.io.IOException;

public class MainApplication extends Application {
    private Controller controller = new Controller();

    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(false);
        root.setStyle("-fx-border-color: #bfbfbf #888888 #888888 #bfbfbf; -fx-border-width: 5; -fx-padding: 10; -fx-background-color: #999999; -fx-alignment: center;");
        // Header of Minesweeper
        HBox header = new HBox();
        header.setSpacing(10);
        // set the header "inset" style
        add3DBorder(header);

        makeDigits(header, controller.getMinesLeft());

        // Smiley face
        Image smileyImage = new Image("file:src/main/resources/images/minesweeper-basic/face-smile.png");
        ImageView smileyImageView = new ImageView(smileyImage);
        controller.setSmileyImage(smileyImageView);
        smileyImageView.setFitWidth(52);
        smileyImageView.setFitHeight(52);
        header.getChildren().add(smileyImageView);

        makeDigits(header, controller.getTimeElapsed());

        // Game area
        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        add3DBorder(gridPane);

        // Create a 5x5 grid of images for the game area
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Image image = new Image("file:src/main/resources/images/minesweeper-basic/cover.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(48);
                imageView.setFitHeight(48);
                imageView.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        // Handle left-click
                        controller.onSpaceClicked(imageView);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        // TODO: Place flag
                    }
                });
                gridPane.add(imageView, col, row);
                controller.addToBoardMap(imageView, new BoardPair<>(row, col));
            }
        }

        root.getChildren().add(header);
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 280, 360);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");
        stage.show();
    }

    private void add3DBorder(Pane pane) {
        pane.setStyle("-fx-border-color: #888888 #bfbfbf #bfbfbf #888888; -fx-border-width: 5; -fx-alignment: center;");
    }

    private void spaceClicked(ImageView imageView) {
        controller.onSpaceClicked(imageView);
    }

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

    public static void main(String[] args) {
        launch();
    }
}