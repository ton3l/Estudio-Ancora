# Project: Estudio Ancora

## Project Overview

This is an Android application named "Estudio Ancora" with the package name `com.eosd.estudio_ancora`. It is built using Kotlin and Jetpack Compose, which indicates a modern Android development approach. The project follows a standard Gradle structure for Android apps.

- **Project Name:** estudio_ancora
- **Package Name:** com.eosd.estudio_ancora
- **Main Activity:** `com.eosd.estudio_ancora.MainActivity`

## Building and Running

This project uses Gradle to manage dependencies and build the application.

- **Build the project:**
  ```bash
  ./gradlew build
  ```

- **Run the application:**
  You can run the application on an emulator or a physical device using Android Studio or the following Gradle command:
  ```bash
  ./gradlew installDebug
  ```

- **Run tests:**
  ```bash
  ./gradlew test
  ```

## Development Conventions

- **Language:** The primary language is Kotlin.
- **UI:** The UI is built with Jetpack Compose.
- **Dependencies:** Dependencies are managed in the `gradle/libs.versions.toml` file.
- **Compose BOM (Bill of Materials):** This project uses the Compose BOM to manage Jetpack Compose library versions. The BOM ensures that all Compose dependencies are compatible with each other. When using the BOM, you should not specify versions for individual `androidx.compose` libraries in the `libs.versions.toml` file; the BOM handles this automatically. This helps prevent version mismatch errors, such as `NoClassDefFoundError`.
- **Structure:** The project follows the standard Android application structure with the main application module located in the `app` directory.
- **Code Cleanup:** Do not perform automatic code cleanup (e.g., removing unused imports, reformatting) unless explicitly requested by the user. If a need for cleanup is identified, alert the user instead of applying changes.

## Ask Context
- When I ask for "explain mode", "ask mode", "chat mode", or similar things, **I only want explanations**, not code changes. Use this section to get some context about me before answering.

I'm new to Android development and to the Android ecosystem (Gradle, Compose, Material, etc.). This is my first real project using these technologies, and I want to learn and understand what I'm doing.
