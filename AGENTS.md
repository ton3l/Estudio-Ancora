# Project: Estudio Ancora

## Project Overview

This is an Android application named "Estudio Ancora" with the package name `com.eosd.estudio_ancora`. It is a schedule management app for a small barbershop, built using Kotlin and Jetpack Compose. The project follows a standard Gradle structure for Android apps.

- **Project Name:** estudio_ancora
- **Package Name:** com.eosd.estudio_ancora
- **Main Activity:** `com.eosd.estudio_ancora.MainActivity`
## Architecture (Modified MVC)

The project follows a modified MVC pattern tailored for Jetpack Compose:

- **Domain:** Pure Kotlin classes representing the business entities (e.g., `Booking`, `Service`).
- **Models:** Responsible for data access, Firestore connections, and mapping Firestore documents (`Document` classes) to `Domain` entities.
- **Services:** Implement the business logic and use cases of the application, acting as an orchestration layer between Models and Views.
- **Views & Controllers:** UI components (Composables) represent the **View**. `ViewModels` act as the **Controllers**, managing UI state and interacting with the Services.

## Constraints

- **Firebase Spark Plan:** The application must be designed and implemented to stay strictly within the limits of the Firebase Spark (free) plan. Avoid features or data structures that would necessitate the Blaze (pay-as-you-go) plan.

## Documentation

- **Location:** Project documentation can be found in the `docs/` directory.
- **Files:**
  - `architecture.md`: Details the modified MVC pattern (Domain, Models, Services, Views & Controllers) and the data flow.
  - `auth.md`: Describes the anonymous authentication strategy, service implementation, and security rules.
  - `database.md`: Describes the Firestore structure (collections, schemas), denormalization strategies, and JIT initialization.
  - `modules.md`: Explains the project's multi-module architecture (`:core`, `:shared`, `:app`, `:admin`) and their responsibilities.
  - `project_structure.md`: Provides an overview of the directory structure and module organization.
  - `testing.md`: Explains the testing strategy, including unit and instrumented tests, and where they are located.
- **Maintenance:** Any modification that affects a documented scope requires a mandatory update to the relevant documentation file as the final step of the agent's execution.

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
  - **Unit tests (all modules):** `./gradlew test`
  - **Instrumented tests (Admin):** `./gradlew :admin:connectedDebugAndroidTest`
  - **Detailed testing info:** See `docs/testing.md`.

## Development Conventions

- **Language:** The primary language is Kotlin.
- **Documentation Language:** All project documentation, including markdown files in `docs/` and comments, must be written in English.
- **UI:** The UI is built with Jetpack Compose.
- **Previews:** Always write Compose Previews for UI components to facilitate visual verification and documentation.
- **Dependencies:** Dependencies are managed in the `gradle/libs.versions.toml` file.
- **Compose BOM (Bill of Materials):** This project uses the Compose BOM to manage Jetpack Compose library versions. The BOM ensures that all Compose dependencies are compatible with each other. When using the BOM, you should not specify versions for individual `androidx.compose` libraries in the `libs.versions.toml` file; the BOM handles this automatically. This helps prevent version mismatch errors, such as `NoClassDefFoundError`.
- **Structure:** The project follows the standard Android application structure with the main application module located in the `app` directory.
- **Code Cleanup:** Do not perform automatic code cleanup (e.g., removing unused imports, reformatting) unless explicitly requested by the user. If a need for cleanup is identified, alert the user instead of applying changes.
- **Task Focus:** Focus strictly on the assigned task. For a task to be considered complete, all project tests must pass successfully.
- **Testing Requirements:** Tests must always be generated for new features or modifications within the `:core` module. Existing documentation (especially `docs/testing.md`) must be reviewed before creating new tests to ensure consistency and compliance with established conventions.
- **String Resources:** Ignore string resources (`res/values/strings.xml`) and use hardcoded strings directly in the UI code, unless explicitly instructed otherwise by the user.

I'm new to Android development and to the Android ecosystem (Gradle, Compose, Material, etc.). This is my first real project using these technologies, and I want to learn and understand what I'm doing.
