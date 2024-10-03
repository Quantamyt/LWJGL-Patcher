import dev.quantam.core.Builder;
import dev.quantam.core.LWJGLPatcher;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        LWJGLPatcher patcher = new Builder()
                .setVersion("3.3.3")
                .addModule("lwjgl")
                .addModule("lwjgl-stb")
                .addModule("lwjgl-nanovg")
                .addModule("lwjgl-nfd")
                .addModule("lwjgl-tinyfd")
                .setWorkingDirectory(new File("lwjgl_patcher_output"))
                .build();

        try {
            patcher.execute();
            System.out.println("LWJGL patching completed successfully.");
            File workingDir = patcher.getWorkingDirectory();
            String patchedJarPath = new File(workingDir, "lwjgl-patched.jar").getAbsolutePath();
            System.out.println("Patched JAR can be found at: " + patchedJarPath);
        } catch (Exception e) {
            System.err.println("An error occurred during LWJGL patching:");
            e.printStackTrace();
        }
    }
}