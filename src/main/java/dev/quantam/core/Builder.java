package dev.quantam.core;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating LWJGLPatcher instances.
 *
 * @author Quantamyt
 */
@Getter
@Setter
public class Builder {
    private String version = "3.3.3";
    private final List<String> modules = new ArrayList<>();
    private File workingDirectory = new File("lwjgl_patcher_temp");

    /**
     * Sets the LWJGL version to patch.
     *
     * @param version The LWJGL version.
     * @return This Builder instance.
     */
    public Builder setVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * Adds an LWJGL module to be patched.
     *
     * @param module The name of the LWJGL module.
     * @return This Builder instance.
     */
    public Builder addModule(String module) {
        this.modules.add(module);
        return this;
    }

    /**
     * Sets the working directory for the patching process.
     *
     * @param workingDirectory The File object representing the working directory.
     * @return This Builder instance.
     */
    public Builder setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    /**
     * Builds and returns a new LWJGLPatcher instance.
     *
     * @return A new LWJGLPatcher instance.
     */
    public LWJGLPatcher build() {
        return new LWJGLPatcher(version, modules, workingDirectory);
    }
}
