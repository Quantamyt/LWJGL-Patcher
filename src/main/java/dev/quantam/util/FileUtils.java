package dev.quantam.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * Utility class for file operations.
 *
 * @author Quantamyt
 */
public class FileUtils {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FileUtils() {
        // Utility class, do not instantiate
    }

    /**
     * Recursively deletes a directory and all its contents.
     *
     * @param directory The directory to delete.
     * @throws IOException If an I/O error occurs during the deletion process.
     */
    public static void deleteDirectory(File directory) throws IOException {
        Path directoryPath = directory.toPath();
        Files.walk(directoryPath)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }

    /**
     * Ensures that a directory exists, creating it and any necessary parent directories if it doesn't.
     *
     * @param directory The directory to ensure exists.
     * @throws IOException If the directory cannot be created.
     */
    public static void ensureDirectoryExists(File directory) throws IOException {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + directory);
        }
    }
}