package minesweeper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The HighScore class manages the high scores for the Minesweeper game.
 * It supports reading from and writing to a file, and adding new high scores.
 */
public class HighScore implements Comparable<HighScore> {
    public static final String FILENAME = "highscores.txt";
    public static List<HighScore> highScores = new ArrayList<>();

    private final String name;
    private final int score;
    private final Difficulty difficulty;

    /**
     * Constructs a new HighScore object.
     *
     * @param name       the name of the player
     * @param score      the score achieved by the player
     * @param difficulty the difficulty level of the game
     */
    public HighScore(String name, int score, Difficulty difficulty) {
        this.name = name;
        this.score = score;
        this.difficulty = difficulty;
    }

    /**
     * Gets the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the score achieved by the player.
     *
     * @return the score achieved by the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the difficulty level of the game.
     *
     * @return the difficulty level of the game
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Checks if a given score is a high score for a specific difficulty level.
     *
     * @param score      the score to check
     * @param difficulty the difficulty level to check
     * @return true if the score is a high score, false otherwise
     */
    public static boolean isHighScore(int score, Difficulty difficulty) {
        if (HighScore.highScores.isEmpty()) {
            return true;
        }

        for (HighScore highScore : HighScore.highScores) {
            if (highScore.getDifficulty() == difficulty) {
                return highScore.getScore() > score;
            }
        }

        return true;
    }

    /**
     * Gets the high scores as a formatted string.
     *
     * @return the high scores as a formatted string
     */
    public static String getHighScoreText() {
        String highScores = HighScore.highScoreText();
        if (highScores.isEmpty()) {
            highScores = "No high scores yet!";
        }
        return highScores;
    }

    /**
     * Gets the high scores as a formatted string. Used internally for writing to a file.
     *
     * @return the high scores as a formatted string
     */
    private static String highScoreText() {
        if (highScores.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < highScores.size(); i++) {
            sb.append(String.format("%-13s %-3d seconds by %s", highScores.get(i).getDifficulty() + ":", highScores.get(i).getScore(), highScores.get(i).getName()));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Adds a new high score.
     *
     * @param name       the name of the player
     * @param score      the score achieved by the player
     * @param difficulty the difficulty level of the game
     */
    public static void addHighScore(String name, int score, Difficulty difficulty) {

        // check if difficulty already has a high score to only keep one
        // comment out the for loop and the is high score check to keep multiple high scores for each difficulty
        for (int i = 0; i < highScores.size(); i++) {
            if (highScores.get(i).getDifficulty() == difficulty) {
                if (highScores.get(i).getScore() > score) {
                    highScores.remove(i);
                }
                break;
            }
        }

        if (HighScore.isHighScore(score, difficulty)) {
            HighScore.highScores.add(new HighScore(name, score, difficulty));
            Collections.sort(highScores);
        }
    }

    /**
     * Resets all high scores.
     */
    public static void resetHighScores() {
        highScores.clear();
        HighScore.writeHighScores();
    }

    /**
     * Reads the high scores from a file.
     */
    public static void readHighScores() {
        File file = new File(FILENAME);
        HighScore.highScores.clear();
        if (!file.exists()) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                // split by any number of spaces
                String[] parts = line.split("\\s+");
                if (parts.length == 5) {
                    HighScore.addHighScore(parts[4].trim(), Integer.parseInt(parts[1]), Difficulty.fromString(parts[0].trim().substring(0, parts[0].length() - 1)));
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(highScores);
    }

    /**
     * Writes the high scores to a file.
     */
    public static void writeHighScores() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            writer.write(HighScore.highScoreText());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compares this high score with another high score. First compares the difficulty, then the score.
     *
     * @param o the other high score to compare to
     * @return a negative integer, zero, or a positive integer as this high score
     *         is less than, equal to, or greater than the specified high score
     */
    @Override
    public int compareTo(HighScore o) {
        if (this.difficulty != o.difficulty) {
            return Integer.compare(this.difficulty.ordinal(), o.difficulty.ordinal());
        }

        // Not exactly needed since we only keep the top score of each difficulty, but it's good to have :)
        return Integer.compare(this.score, o.score);
    }
}