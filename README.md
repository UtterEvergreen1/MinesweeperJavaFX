# Minesweeper Game

This is a Java implementation of the classic Minesweeper game. The game includes various features and enhancements to provide an engaging user experience.

## Notable Features

- **First Click Guarantee**: The first click is always on a tile with no mines around it, and surrounding tiles with no mines are opened automatically using recursion.
- **Revealed Number Click Functionality**: Left-clicking on a number tile with the correct number of flags around it will uncover all remaining covered tiles in its group of 8 (including unflagged mines).
- **Difficulty Levels**: A difficulty menu with three levels: Beginner, Intermediate, and Expert.
- **Elapsed Time and Mines Display**: Shows the elapsed time and mines left in the original red digit style.
- **High Scores**: Tracks high scores based on the least amount of time elapsed per difficulty level. Prompts for a name when a new high score is achieved.
- **Visual Enhancements**: Includes a nice border around the mines and uses specific graphics for different game states (e.g., Face-O graphic while the mouse is pressed, misflagged graphic when a mine is clicked and there was a flag on a non-mine tile).

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/UtterEvergreen1/minesweeper.git
    cd minesweeper
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

### Running the Game

1. Run the application:
    ```sh
    mvn javafx:run
    ```

## Usage

- **Starting a Game**: Select a difficulty level from the menu to start a new game.
- **Playing the Game**: Use left-click to uncover tiles and right-click to place flags.
- **High Scores**: If you achieve a high score, you will be prompted to enter your name.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Acknowledgments

- Inspired by the classic Minesweeper game.
- Uses JavaFX for the user interface.
