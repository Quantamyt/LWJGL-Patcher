package dev.quantam.processors.interfaces;

import java.io.File;
import java.io.IOException;

/**
 * Interface for processing JAR files.
 *
 * @author Quantamyt
 */
public interface IProcessor {
    void process(File inputFile, File outputFile) throws IOException;
}
