# Application Architecture (Modified MVC)

This project utilizes an adapted version of the **MVC (Model-View-Controller)** pattern, designed for the modern Android ecosystem with **Jetpack Compose** and **Firebase Firestore**.

The architecture is divided into four primary layers: **Domain**, **Models**, **Services**, and **Views**.

## 1. Domain
The Domain layer contains the system's **Entities**. These are pure Kotlin `data classes` that represent the core business concepts of the application.

- **What they are:** Objects such as `Booking`, `Service`, and `Day`.
- **Golden Rule:** These classes must not have dependencies on external frameworks (like Firebase or Android). They are the "heart" of the application and should remain stable.

## 2. Models
The Models layer is responsible for data persistence and access. In this project, it handles direct communication with **Google Cloud Firestore** and **Jetpack DataStore**.

### Model Files (e.g., `BookingModel.kt`)
These are singleton objects (`object`) that act as the entry point for data operations (CRUD).
- **Responsibility:** Executing calls to the SDKs (Firestore, DataStore), managing collections, and performing asynchronous operations (using `suspend functions` and Coroutines).

### Document Files (e.g., `BookingDocument.kt`)
These are Data Transfer Objects (DTOs) specifically designed for the Firestore format.
- **Responsibility:** Mapping exactly how data is stored within the database collections.
- **Mapping:**
    - `toEntity()`: A method that transforms a document from the database into a **Domain** entity.
    - `toDocument()`: A static method that transforms a **Domain** entity into a document ready to be persisted.

## 3. Services
Services handle business logic and the application's use cases.

- **What they do:** Act as an orchestration layer. They decide *when* to call a Model, how to validate data before saving, and how to process complex information involving multiple Models.
- **Example:** `DayService` might coordinate with `BookingModel` and `WeekDayAvailableTimesModel` to generate a day's availability.

## 4. Views & Controllers
This layer is responsible for the user interface and UI state.

- **Views (Composables):** Functions that draw the screen. They should be "dumb," focusing only on displaying state and forwarding events.
- **Controllers (ViewModels):** In modern Android, `ViewModels` take on the role of Controllers. They maintain the screen state (`UI State`), react to user interactions, and call **Services**.

### Shared View & Logic Pattern
To maximize reuse between the `:app` and `:admin` modules, the project follows a **Shared Flow** pattern:
1. **Shared Screens/ViewModels:** Located in the `:shared` module, these provide the core layout and logic for common user flows (like booking).
2. **Module-Specific Child Components:** Shared screens can accept Composable child functions (lambda parameters). This allows the `:app` module to use one type of visualization (e.g., `AppAvailableTimesHandler`) while the `:admin` module uses another (e.g., `AdminAvailableTimesHandler`), while keeping the surrounding logic identical.

## Common Data Flow
1. **View** detects a click -> calls the **ViewModel (Controller)**.
2. **ViewModel** calls the corresponding **Service**.
3. **Service** processes business logic and calls the **Model**.
4. **Model** converts the **Domain** data into a **Document** and saves it to Firestore.
