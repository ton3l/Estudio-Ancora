# Project Structure

This document provides an overview of the directory structure for the Estudio Ancora project. Build artifacts, IDE configurations, and dependency caches are omitted for clarity.

```text
estudio_ancora/
├── admin/                  # Admin module (for barbershop management)
│   ├── build.gradle.kts
│   └── src/
│       ├── androidTest/    # Instrumented tests (Centralized)
│       │   └── java/com/eosd/estudio_ancora/
│       │       ├── models/ # Firestore & DataStore integration tests
│       │       ├── services/ # Business workflow integration tests
│       │       ├── BookingSeederTest.kt
│       │       ├── FirestoreSeederTest.kt
│       │       └── ServiceSeederTest.kt
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/eosd/estudio_ancora/admin/
│       │   │   ├── MainActivity.kt
│       │   │   └── views/
│       │   │       ├── components/ # Admin-specific components
│       │   │       ├── screens/    # Admin-specific screens
│       │   │       └── viewModels/ # Admin-specific ViewModels
│       │   └── res/        # Admin specific resources
│       └── test/           # Unit tests (Centralized)
│           └── java/com/eosd/estudio_ancora/
│               ├── domain/     # Entity logic tests
│               ├── models/     # DTO mapping tests
│               └── validators/ # Business rule tests
├── app/                    # Main client application module
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/eosd/estudio_ancora/
│       │   │   ├── MainActivity.kt
│       │   │   └── views/
│       │   │       ├── components/ # Client-specific components
│       │   │       ├── screens/    # Client-specific screens
│       │   │       └── viewModels/ # Client-specific ViewModels
│       │   └── res/
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
│               └── views/  # Shared UI themes, components, and logic
│                   ├── components/ # Reusable UI components
│                   ├── screens/    # Shared multi-module screens
│                   ├── utils/      # Visual transformations
│                   └── viewModels/ # ViewModels for shared screens
├── docs/                   # Project documentation
│   ├── architecture.md
│   ├── database.md
│   ├── modules.md
│   ├── project_structure.md
│   └── testing.md
├── gradle/                 # Gradle wrapper and version catalogs
│   └── libs.versions.toml  # Centralized dependency management
├── build.gradle.kts        # Root build script
├── GEMINI.md               # Project rules and context for AI agents
├── settings.gradle.kts     # Project module definitions
└── gradle.properties       # Project-wide Gradle settings
```
