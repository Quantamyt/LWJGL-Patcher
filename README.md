# LWJGL Patcher

LWJGL Patcher is designed to download, process, and merge LWJGL (Lightweight Java Game Library) modules. It provides a flexible and efficient way to create custom LWJGL distributions with specific modifications. Used for using LWJGL3 features in LWJGL2.

## Features

- Concurrent download of LWJGL modules
- Custom processing of JAR files
- Merging of processed JAR files
- Configurable working directory

## Requirements

- Java 11 or higher
- Maven (for building)
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

## Building

To build the project, run:

```
mvn clean package
```

This will create a JAR file in the `target` directory.

## Modules
Check out the [LWJGL3](https://www.lwjgl.org/customize) page to know more about **Modules**.

**Copyright (c) 2023 Quantamyt**