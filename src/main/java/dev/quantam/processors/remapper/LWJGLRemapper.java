package dev.quantam.processors.remapper;

import org.objectweb.asm.commons.Remapper;

/**
 * Custom Remapper for LWJGL classes.
 *
 * @author Quantamyt
 *
 */
public class LWJGLRemapper extends Remapper {

    /**
     * Maps internal names of classes, potentially modifying LWJGL-specific class names.
     *
     * @param internalName The internal name of the class.
     * @return The potentially modified internal name.
     */
    @Override
    public String map(String internalName) {
        if (internalName.equals("org/lwjgl/BufferUtils") || internalName.equals("org/lwjgl/PointerBuffer")) {
            return internalName.replace("lwjgl", "lwjgl3");
        }
        return internalName;
    }
}
