# Application Architecture (Modified MVC)

This project utilizes an adapted version of the **MVC (Model-View-Controller)** pattern, designed for the modern Android ecosystem with **Jetpack Compose** and **Firebase Firestore**.

The architecture is divided into four primary layers: **Domain**, **Models**, **Services**, and **Views**.

## 1. Domain
The Domain layer contains the system's **Entities**. These are pure Kotlin `data classes` that represent the core business concepts of the application.

- **What they are:** Objects such as `Booking`, `Service`, and `Day`.
- **Golden Rule:** These classes must not have dependencies on external frameworks (like Firebase or Android). They are the "heart" of the application and should remain stable.

## 2. Models
The Models layer is responsible for data persistence and access. In this project, it handles direct communication with **Google Cloud Firestore**. To maintain organization, this layer is divided into two types of files:

### Model Files (e.g., `BookingModel.kt`)
These are singleton objects (`object`) that act as the entry point for data operations (CRUD).
- **Responsibility:** Executing calls to the Firestore SDK, managing collections, and performing asynchronous operations (using `suspend functions` and Coroutines).
- **Workflow:** The Model receives a **Domain** object, converts it into a **Document**, and sends it to the database.

### Document Files (e.g., `BookingDocument.kt`)
These are Data Transfer Objects (DTOs) specifically designed for the Firestore format.
- **Responsibility:** Mapping exactly how data is stored within the database collections.
- **Why they exist:** Firestore uses specific data types (like `Timestamp` for dates or the `@DocumentId` annotation). By using `Document` files, we prevent these Firebase-specific types from "polluting" our Domain classes.
- **Mapping:**
    - `toEntity()`: A method that transforms a document from the database into a **Domain** entity that the rest of the app understands.
    - `toDocument()`: A static method (inside the `companion object`) that transforms a **Domain** entity into a document ready to be persisted.

## 3. Services
Services handle business logic and the application's use cases.

- **What they do:** Act as an orchestration layer. They decide *when* to call a Model, how to validate data before saving, and how to process complex information involving multiple Models.
- **Example:** A `BookingService` might check for time slot availability before calling `BookingModel.addBooking()`.

## 4. Views & Controllers
This layer is responsible for the user interface and UI state.

- **Views (Composables):** These are functions that draw the screen using Jetpack Compose. They should be "dumb," focusing only on displaying state and forwarding click events.
- **Controllers (ViewModels):** In modern Android, `ViewModels` take on the role of Controllers. They maintain the screen state (UI State), react to user interactions, and call **Services** to process the necessary actions.

## Common Data Flow
1. **View** detects a click -> calls the **ViewModel (Controller)**.
2. **ViewModel** calls the corresponding **Service**.
3. **Service** processes business logic and calls the **Model**.
4. **Model** converts the **Domain** data into a **Document** and saves it to Firestore.
