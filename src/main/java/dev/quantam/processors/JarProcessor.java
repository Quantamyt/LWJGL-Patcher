package dev.quantam.processors;

import dev.quantam.processors.interfaces.IProcessor;
import dev.quantam.processors.remapper.LWJGLRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * Utility class for processing JAR files.
 *
 * @author Quantamyt
 */
public class JarProcessor implements IProcessor {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    public JarProcessor() {
        // Utility class, do not instantiate
    }

    /**
     * Processes a JAR file, applying transformations to its contents.
     *
     * @param inputFile The input JAR file to process.
     * @param outputFile The output JAR file to write the processed contents to.
     * @throws IOException If an I/O error occurs during processing.
     */
    @Override
    public void process(File inputFile, File outputFile) throws IOException {
        try (JarInputStream jarIn = new JarInputStream(new FileInputStream(inputFile));
             JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(outputFile))) {

            JarEntry entry;
            while ((entry = jarIn.getNextJarEntry()) != null) {
                if (entry.isDirectory() || entry.getName().startsWith("META-INF/")) {
                    continue;
                }

                jarOut.putNextEntry(new JarEntry(entry.getName()));

                if (entry.getName().endsWith(".class")) {
                    processClass(jarIn, jarOut);
                } else {
                    transferBytes(jarIn, jarOut);
                }

                jarOut.closeEntry();
            }
        }
    }

    /**
     * Processes a single class file within the JAR.
     *
     * @param in The input stream for the class file.
     * @param out The output stream to write the processed class file to.
     * @throws IOException If an I/O error occurs during processing.
     */
    private void processClass(InputStream in, OutputStream out) throws IOException {
        ClassReader reader = new ClassReader(in);
        ClassWriter writer = new ClassWriter(0);

        ClassRemapper remapper = new ClassRemapper(writer, new LWJGLRemapper());
        reader.accept(remapper, 0);

        out.write(writer.toByteArray());
    }

    /**
     * Transfers bytes from an input stream to an output stream.
     *
     * @param in The input stream to read from.
     * @param out The output stream to write to.
     * @throws IOException If an I/O error occurs during the transfer.
     */
    private void transferBytes(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
}
