# Testing Structure - Estudio Ancora

This document describes the testing strategy and organization in the Estudio Ancora project.

## Testing Strategy

The project uses a combination of unit tests and instrumented (integration) tests to ensure code quality. **All tests are centralized in the `:admin` module** to simplify development, maintenance, and CI/CD operations.

### Unit Tests (JVM)

Focused on pure business logic, data mapping (DTOs), and validators. These run quickly on the local machine.

- **Location:** `admin/src/test/java/com/eosd/estudio_ancora/`
- **Primary Areas Covered:**
    - `domain/`: Business logic in pure Kotlin entities (e.g., `DayTest`).
    - `models/`: Mapping from `Document` classes to `Domain` entities (e.g., `BookingDtosTest`).
    - `validators/`: Business rule validation (e.g., `BookingValidatorTest`, `ServiceValidatorTest`).

### Instrumented Tests (Android)

Focused on integration with external infrastructure (Firebase Firestore) and local persistence (Jetpack DataStore). These run on an emulator or a physical device.

- **Location:** `admin/src/androidTest/java/com/eosd/estudio_ancora/`
- **Primary Areas Covered:**
    - `models/`: Database integration tests (e.g., `BookingModelTest`, `DayModelTest`, `BookingLocalPersistenceTest`).
    - `services/`: Complex business workflows and orchestration (e.g., `BookingServiceTest`, `DayWorkflowTest`).

## Database Preparation (Seeders)

Specific tests exist in the `:admin` module designed to populate or clear the Firestore database for development and testing purposes:

- **`FirestoreSeederTest`:** Populates the `week-available-times` collection with standard business hours.
- **`ServiceSeederTest`:** Populates the `services` collection with default services.
- **`BookingSeederTest`:** Populates the `bookings` and `booking-days` collections with sample data.

## How to Run Tests

### All Unit Tests
```bash
./gradlew :admin:test
```

### All Instrumented Tests
```bash
./gradlew :admin:connectedDebugAndroidTest
```

### Run a Specific Seeder
```bash
./gradlew :admin:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.eosd.estudio_ancora.FirestoreSeederTest
```

## Testing Conventions

- **Critical Restriction:** No test should EVER modify the `week-available-times` collection. This collection defines the base availability and changes to it can cause difficult-to-track errors across the entire test suite.
- **Isolation:** Instrumented tests that modify the database (e.g., in `booking-days` or `bookings` collections) should clean their test data before (`@Before`) or after (`@After`) execution.
- **Test Environment:** Ensure the database is configured for testing and isolated from production.
- **Visual Fidelity:** Future UI tests (Compose) will also be added to this centralized structure.
