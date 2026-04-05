# Testing Structure - Estudio Ancora

This document describes the testing strategy and organization in the Estudio Ancora project.

## Testing Strategy

The project uses a combination of unit tests and instrumented (integration) tests to ensure code quality.

### Unit Tests (JVM)

Focused on pure business logic, data mapping (DTOs), and validators. These run quickly on the local machine.

- **Location:** `[module]/src/test/java/...`
- **Primary Areas Covered:**
    - Mapping from `Document` classes to `Domain` entities (DTOs).
    - Domain logic in pure Kotlin `data class` files.
    - Business validators (e.g., `BookingValidator`).

### Instrumented Tests (Android)

Focused on integration with Firebase Firestore and Jetpack DataStore. These run on an emulator or a physical device.

- **Location:** Currently centralized in the `:admin` module at `admin/src/androidTest/java/...`.
- **Centralization Strategy:** All tests involving persistence of `:core` models have been moved to the `:admin` module to simplify maintenance and centralize the validation of the data layer.
- **Primary Areas Covered:**
    - `BookingModel`, `DayModel`, `ServiceModel` (Firestore integration).
    - `BookingLocalPersistence` (DataStore integration).
    - `BookingService`, `DayService` (Business workflows).

## Database Preparation (Seeders)

Specific tests exist in the `:admin` module designed to populate or clear the Firestore database for development and testing purposes:

- **`FirestoreSeederTest`:** Populates the `week-available-times` collection with standard business hours.
- **`ServiceSeederTest`:** Populates the `services` collection with the services offered by the barbershop.

## How to Run Tests

### All Unit Tests
```bash
./gradlew test
```

### Admin Module Instrumented Tests
```bash
./gradlew :admin:connectedDebugAndroidTest
```

### Run a Specific Seeder
```bash
./gradlew :admin:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.eosd.estudio_ancora.FirestoreSeederTest
```

## Testing Conventions

- **Critical Restriction:** No test should EVER modify the `week-available-times` collection. This collection defines the base availability and changes to it can cause extremely difficult-to-track errors across the entire test suite. If any change to this collection is absolutely necessary, it must be strictly discussed and planned.
- **Isolation:** Instrumented tests that modify the database (e.g., in `booking-days` or `bookings` collections) should, preferably, clean their test data before (`@Before`) or after (`@After`) execution to avoid failures in successive runs (e.g., "Time slot already booked" error).
- **Test Environment:** The database configured for tests must be isolated from the production environment.
- **Visual Fidelity:** In the future, UI tests (Compose) will also be added to this same structure.
