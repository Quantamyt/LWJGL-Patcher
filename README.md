<div align="center">
    <h2>LWJGL Patcher</h2>
    LWJGL Patcher is designed to download, process, and merge LWJGL (Lightweight Java Game Library) modules. It provides a flexible and efficient way to create custom LWJGL distributions with specific modifications. Used for using LWJGL3 features in LWJGL2.
</div>

## Features

- Concurrent download of LWJGL modules
- Custom processing of JAR files
- Merging of processed JAR files
- Configurable working directory

## Requirements

- Java 11 or higher
- Gradle (for building)
- Dependencies:
    - ASM (for bytecode manipulation)

## Usage

To use LWJGL Patcher, create an instance using the builder pattern and then call the `execute()` method:

```java
LWJGLPatcher patcher = new Builder()
    .setVersion("3.3.3")
    .addModule("lwjgl")
    .addModule("lwjgl-opengl")
    .addModule("lwjgl-glfw") // add more modules if needed (As to what modules are available, check the LWJGL3 page.)
    .setWorkingDirectory(new File("custom_patcher_dir"))
    .build();

try {
    patcher.execute();
    System.out.println("Patching completed successfully.");
} catch (IOException | InterruptedException e) {
    System.err.println("An error occurred during patching: " + e.getMessage());
    e.printStackTrace();
}
```

## Building with Gradle

To build the project using Gradle, run:

```bash
./gradlew clean build
```

This will compile the project and create a JAR file in the `build/libs` directory.

## Dependencies

In your `build.gradle` file, make sure to include the following dependencies:

```gradle
dependencies {
    implementation 'org.lwjgl:lwjgl:3.3.3'
    implementation 'org.lwjgl:lwjgl-opengl:3.3.3'
    implementation 'org.lwjgl:lwjgl-glfw:3.3.3'
    implementation 'org.ow2.asm:asm:9.2'  // For bytecode manipulation
}
```

Ensure that your Gradle version is compatible with Java 11 or higher.

## Modules

Check out the [LWJGL3](https://www.lwjgl.org/customize) page to know more about **Modules**.

**Copyright (c) 2023 Quantamyt**
