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

Focused on integration with external infrastructure (Firebase Firestore) and local persistence (Jetpack DataStore). These run on an emulator or a physical device. **Tests are exclusively available and should only be run on the `dev` flavor.**

- **Location:** `admin/src/androidTest/java/com/eosd/estudio_ancora/`
- **Primary Areas Covered:**
    - `models/`: Database integration tests (e.g., `BookingModelTest`, `DayModelTest`, `BookingLocalPersistenceTest`).
    - `services/`: Complex business workflows and orchestration (e.g., `BookingServiceTest`, `DayWorkflowTest`).

## Database Preparation (Seeders)

Specific tests exist in the `:admin` module designed to populate or clear the Firestore database for development and testing purposes (**Only on `dev` flavor**):

- **`FirestoreSeederTest`:** Populates the `week-available-times` collection with standard business hours.
- **`ServiceSeederTest`:** Populates the `services` collection with default services.
- **`BookingSeederTest`:** Populates the `bookings` and `booking-days` collections with sample data.

## How to Run Tests

### All Unit Tests
```bash
./gradlew :admin:test
```

### All Instrumented Tests (Dev environment)
```bash
./gradlew :admin:connectedDevDebugAndroidTest
```

### Run a Specific Seeder (Dev environment)
To run a seeder, you must explicitly include the `@Seeder` annotation filter, as they are excluded from the default test run for security.

#### Via Command Line:
```bash
./gradlew :admin:connectedDevDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.annotation=com.eosd.estudio_ancora.utils.Seeder \
  -Pandroid.testInstrumentationRunnerArguments.class=com.eosd.estudio_ancora.FirestoreSeederTest
```

**Note:** The use of `\` is for Linux/Git Bash. For PowerShell use `` ` `` and for CMD use `^`.

#### Via Android Studio (IDE):
The easiest way to run seeders using the Android Studio interface (play button) is:
1. Go to `admin/build.gradle.kts`.
2. Temporarily comment out the line: `testInstrumentationRunnerArguments["notAnnotation"] = "com.eosd.estudio_ancora.utils.Seeder"`.
3. Sync Gradle.
4. Run the seeder class or method directly using the IDE's run icons.
5. **Remember to uncomment the line after you are done** to restore the security measures.


## Automated Testing (Agent Guidelines)

- **Mandatory Attempt:** Instrumented tests (`./gradlew :admin:connectedDevDebugAndroidTest`) MUST always be attempted by the agent during the verification phase, even if it is uncertain whether a device or emulator is connected.
- **Graceful Fallback:** If the instrumented tests fail (e.g., due to no device being found), the agent must proceed with the task by executing all available unit tests (`./gradlew test`).
- **Reporting:** In case of failure to run instrumented tests due to device absence, the agent must explicitly inform the user at the end of the task that these tests were skipped for that reason.
- **Seeder Restriction:** The agent MUST NOT execute any seeder (`FirestoreSeederTest`, `ServiceSeederTest`, `BookingSeederTest`). If the agent identifies that the database needs to be populated or reset to proceed with a task or test, it must stop the current task immediately and ask the user to perform the seeding manually.

## Testing Conventions

- **Environment Restriction:** Instrumented tests and Seeders must NEVER be run against the production database. The project is configured to disable `androidTest` for the `prod` flavor to enforce this.
- **Critical Restriction:** No test should EVER modify the `week-available-times` collection. This collection defines the base availability and changes to it can cause difficult-to-track errors across the entire test suite.
- **Isolation:** Instrumented tests that modify the database (e.g., in `booking-days` or `bookings` collections) should clean their test data before (`@Before`) or after (`@After`) execution.
- **Test Environment:** Ensure the database is configured for testing and isolated from production.
- **Visual Fidelity:** Future UI tests (Compose) will also be added to this centralized structure.
