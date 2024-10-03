package dev.quantam.processors.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface for merging JAR files.
 *
 * @author Quantamyt
 */
public interface IMerger {
    void mergeJars(List<File> inputFiles, File outputFile) throws IOException;
}
