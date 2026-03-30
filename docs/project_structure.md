# Project Structure

This document provides an overview of the directory structure for the Estudio Ancora project. Build artifacts, IDE configurations, and dependency caches are omitted for clarity.

```text
estudio_ancora/
├── admin/                  # Admin module (for barbershop management)
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/eosd/estudio_ancora/admin/
│           │   └── MainActivity.kt
│           └── res/        # Admin specific resources
├── app/                    # Main client application module
│   ├── build.gradle.kts
│   ├── google-services.json
│   └── src/
│       ├── androidTest/    # Instrumented tests
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/com/eosd/estudio_ancora/
│       │       ├── MainActivity.kt
│       │       └── views/   # View layer (Composables, ViewModels)
│       └── test/           # Local unit tests
├── core/                   # Core business logic module
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           └── java/com/eosd/estudio_ancora/
│               ├── domain/     # Pure business entities
│               ├── libs/       # Infrastructure wrappers (Firestore, DataStore)
│               ├── models/     # Data access and DTOs
│               ├── services/   # Business logic orchestration
│               ├── states/     # UI State definitions
│               └── validators/ # Business rule validators
├── shared/                 # Shared UI and utility module
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           └── java/com/eosd/estudio_ancora/
│               ├── utils/  # Common formatters and utilities
│               └── views/  # Shared UI themes and components
├── docs/                   # Project documentation
│   ├── architecture.md
│   ├── database.md
│   ├── modules.md
│   └── project_structure.md
├── gradle/                 # Gradle wrapper and version catalogs
│   └── libs.versions.toml  # Centralized dependency management
├── build.gradle.kts        # Root build script
├── GEMINI.md               # Project rules and context for AI agents
├── settings.gradle.kts     # Project module definitions
└── gradle.properties       # Project-wide Gradle settings
```
