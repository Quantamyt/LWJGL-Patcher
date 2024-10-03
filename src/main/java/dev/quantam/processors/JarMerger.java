package dev.quantam.processors;

import dev.quantam.processors.interfaces.IMerger;

import java.io.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipException;

/**
 * Utility class for merging multiple JAR files into a single JAR.
 *
 * @author Quantamyt
 */
public class JarMerger implements IMerger {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    public JarMerger() {
        // Utility class, do not instantiate
    }

    /**
     * Merges multiple input JAR files into a single output JAR file.
     *
     * @param inputFiles List of input JAR files to merge.
     * @param outputFile The output JAR file to create.
     * @throws IOException If an I/O error occurs during the merging process.
     */
    @Override
    public void mergeJars(List<File> inputFiles, File outputFile) throws IOException {
        try (JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outputFile))) {
            byte[] buffer = new byte[8192];

            for (File inputFile : inputFiles) {
                try (JarInputStream jarIn = new JarInputStream(new FileInputStream(inputFile))) {
                    JarEntry entry;
                    while ((entry = jarIn.getNextJarEntry()) != null) {
                        if (entry.isDirectory()) {
                            continue;
                        }

                        try {
                            jarOut.putNextEntry(new JarEntry(entry.getName()));
                        } catch (ZipException e) {
                            // Entry already exists, skip it
                            continue;
                        }

                        int bytesRead;
                        while ((bytesRead = jarIn.read(buffer)) != -1) {
                            jarOut.write(buffer, 0, bytesRead);
                        }

                        jarOut.closeEntry();
                    }
                }
            }
        }
    }
}
