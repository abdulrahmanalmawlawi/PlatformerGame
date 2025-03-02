package assets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * GameFileManager provides utility methods for file I/O operations in the game.
 * It handles reading and writing game data, level files, and player progress.
 * This class includes methods for:
 * - Reading level data from files
 * - Parsing file content into usable game data
 * - Appending new data to existing files
 * - Creating files if they don't exist
 */
public class GameFileManager {
    /**
     * Reads a text file and returns its contents as a list of strings.
     * If the file doesn't exist, it creates an empty file first.
     * 
     * @param filePath The path of the file to read
     * @return A list containing each line of the file as a separate string
     */
    public static List<String> ReadTextLevel(String filePath) {
        List<String> lines = new LinkedList<>();
        try {
            File file = new File(filePath);

            // Create the file if it doesn't exist
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("Created missing file: " + filePath);
                } catch (IOException e) {
                    System.err.println("Failed to create file: " + filePath);
                    e.printStackTrace();
                }
            }

            // Read the file line by line
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Parses a line of text by splitting it at semicolons.
     * Used for parsing level data and game settings.
     * 
     * @param line The line to parse
     * @return A list of strings containing the separated values
     */
    public static List<String> AnalyseLine(String line) {
        List<String> parsedValues = new LinkedList<>();
        String currentValue = "";

        for (char c : line.toCharArray()) {
            if (c == ';') {
                parsedValues.add(currentValue);
                currentValue = "";
            } else {
                currentValue += c;
            }
        }

        return parsedValues;
    }

    /**
     * Appends a line of text to a file.
     * Creates the file if it doesn't exist.
     * 
     * @param filename The name of the file to append to
     * @param text     The text to append to the file
     */
    public static void appendLine(String filename, String text) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try {
            // Create the file if it doesn't exist
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("Created missing file: " + filename);
                } catch (IOException e) {
                    System.err.println("Failed to create file: " + filename);
                    e.printStackTrace();
                }
            }

            // Append the text to the file
            fileWriter = new FileWriter(filename, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}