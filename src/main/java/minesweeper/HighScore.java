package minesweeper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScore implements Comparable<HighScore> {
    public static final String FILENAME = "highscores.txt";
    public static List<HighScore> highScores = new ArrayList<>();

    private final String name;
    private final int score;
    private final Difficulty difficulty;

    public HighScore(String name, int score, Difficulty difficulty) {
        this.name = name;
        this.score = score;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

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

    public static String getHighScoreText() {
        String highScores = HighScore.highScoreText();
        if (highScores.isEmpty()) {
            highScores = "No high scores yet!";
        }
        return highScores;
    }

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

    public static void resetHighScores() {
        highScores.clear();
        HighScore.writeHighScores();
    }

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

    @Override
    public int compareTo(HighScore o) {
        if (this.difficulty != o.difficulty) {
            return Integer.compare(this.difficulty.ordinal(), o.difficulty.ordinal());
        }

        // Not exactly needed since we only keep the top score of each difficulty, but it's good to have :)
        return Integer.compare(this.score, o.score);
    }
}