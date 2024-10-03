package dev.quantam.core;

import dev.quantam.util.FileUtils;
import dev.quantam.processors.JarMerger;
import dev.quantam.processors.JarProcessor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The main class for patching LWJGL (Lightweight Java Game Library) JAR files.
 * This class handles downloading, processing, and merging of LWJGL modules.
 *
 * @author Quantamyt
 *
 */

@Getter
@Setter
public class LWJGLPatcher {
    private static final String MAVEN_BASE_URL = "https://repo1.maven.org/maven2/org/lwjgl/";
    private final String version;
    private final List<String> modules;
    private final File workingDirectory;
    private final ExecutorService executorService;
    private JarMerger jarMerger = new JarMerger();
    private JarProcessor jarProcessor = new JarProcessor();

    /**
     * Private constructor for LWJGLPatcher. Use the Builder class to create instances.
     *
     * @param version The version of LWJGL to patch.
     * @param modules The list of LWJGL modules to process.
     * @param workingDirectory The directory for temporary files and the output JAR.
     */
    LWJGLPatcher(String version, List<String> modules, File workingDirectory) {
        this.version = version;
        this.modules = new ArrayList<>(modules);
        this.workingDirectory = workingDirectory;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Executes the patching process.
     *
     * @throws IOException If an I/O error occurs during the patching process.
     * @throws InterruptedException If the execution is interrupted.
     */
    public void execute() throws IOException, InterruptedException {
        try {
            prepareWorkingDirectory();
            List<Future<File>> downloadFutures = downloadModules();
            List<File> downloadedFiles = waitForDownloads(downloadFutures);
            List<File> processedFiles = processFiles(downloadedFiles);
            File outputFile = new File(workingDirectory, "lwjgl-patched.jar");
            jarMerger.mergeJars(processedFiles, outputFile);
            cleanup(downloadedFiles, processedFiles);
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Prepares the working directory by creating it if it doesn't exist,
     * or clearing it if it does.
     *
     * @throws IOException If an I/O error occurs while preparing the directory.
     */
    private void prepareWorkingDirectory() throws IOException {
        if (workingDirectory.exists()) {
            FileUtils.deleteDirectory(workingDirectory);
        }
        Files.createDirectories(workingDirectory.toPath());
    }

    /**
     * Initiates the download of all specified LWJGL modules.
     *
     * @return A list of Future objects representing the pending downloads.
     */
    private List<Future<File>> downloadModules() {
        List<Future<File>> futures = new ArrayList<>();
        for (String module : modules) {
            futures.add(executorService.submit(() -> downloadModule(module)));
        }
        return futures;
    }

    /**
     * Downloads a single LWJGL module.
     *
     * @param module The name of the module to download.
     * @return The File object representing the downloaded module.
     * @throws IOException If an I/O error occurs during the download.
     */
    private File downloadModule(String module) throws IOException {
        String url = MAVEN_BASE_URL + module + "/" + version + "/" + module + "-" + version + ".jar";
        File outputFile = new File(workingDirectory, module + ".jar");
        System.out.println("Downloading " + url);
        try (var in = new URL(url).openStream()) {
            Files.copy(in, outputFile.toPath());
        }
        return outputFile;
    }

    /**
     * Waits for all module downloads to complete.
     *
     * @param futures The list of Future objects representing the pending downloads.
     * @return A list of File objects representing the downloaded modules.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    private List<File> waitForDownloads(List<Future<File>> futures) throws InterruptedException {
        List<File> files = new ArrayList<>();
        for (Future<File> future : futures) {
            try {
                files.add(future.get());
            } catch (Exception e) {
                throw new RuntimeException("Failed to download module", e);
            }
        }
        return files;
    }

    public void addModule(String module) {
        this.modules.add(module);
    }

    /**
     * Processes all downloaded JAR files.
     *
     * @param files The list of downloaded JAR files to process.
     * @return A list of File objects representing the processed JAR files.
     * @throws IOException If an I/O error occurs during processing.
     */
    private List<File> processFiles(List<File> files) throws IOException {
        List<File> processedFiles = new ArrayList<>();
        for (File file : files) {
            File processedFile = new File(workingDirectory, file.getName() + ".processed");
            jarProcessor.process(file, processedFile);
            processedFiles.add(processedFile);
        }
        return processedFiles;
    }

    /**
     * Cleans up temporary files after the patching process is complete.
     *
     * @param downloadedFiles The list of originally downloaded files to delete.
     * @param processedFiles The list of processed files to delete.
     */
    private void cleanup(List<File> downloadedFiles, List<File> processedFiles) {
        for (File file : downloadedFiles) {
            file.delete();
        }
        for (File file : processedFiles) {
            file.delete();
        }
    }


}