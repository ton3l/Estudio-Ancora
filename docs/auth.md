# Authentication Architecture

This project uses **Firebase Anonymous Authentication** to provide a seamless, frictionless user experience while maintaining database security and individual user tracking.

## Overview

The authentication strategy is designed to be "transparent" to the end-user. Users are not required to provide emails, passwords, or social accounts to use the app. Instead, an anonymous identity is created automatically on the first run.

### Key Features
- **Zero Friction**: No login or sign-up screens.
- **Persistent Identity**: The anonymous UID persists across app sessions on the same device.
- **Lazy Security**: Read operations are public (allowing instant UI loading), while write operations are gated behind authentication.
- **Environment Aware**: Gracefully handles development environments where Firebase Auth might not be enabled.

## Implementation Details

The authentication logic is centralized in the `:core` module.

### Core Components

1.  **`Auth.kt` (`:core/libs`)**:
    Provides a centralized, lazy-loaded reference to `Firebase.auth`.

2.  **`AuthService.kt` (`:core/services`)**:
    Contains the business logic for ensuring an active session.
    - `ensureAuthenticated()`: Checks if `auth.currentUser` is present. If not, it triggers `signInAnonymously()`. It uses Kotlin Coroutines (`await()`) to suspend execution until the login is complete.
    - **Error Handling**: Wrapped in a `try-catch` block. If the auth provider is disabled (common in Development projects), it logs a warning but allows the app to proceed (allowing development via open Security Rules).

### Application Integration

Both the **Customer App** and **Admin App** trigger the authentication process in their respective `MainActivity.kt`:

```kotlin
// In onCreate
lifecycleScope.launch {
    AuthService.ensureAuthenticated()
}
```

This starts the login process immediately as the app opens. Because it's a coroutine, it doesn't block the UI thread, allowing the app to load and show data immediately (since Read access is public).

### Gated Write Operations

Any operation that modifies data (e.g., creating or deleting a booking) **must** call `AuthService.ensureAuthenticated()` before proceeding. This ensures that even if the initial background login hasn't finished, the write operation will wait for it.

Example in `BookingService.kt`:
```kotlin
suspend fun addBooking(context: Context, bookingInfo: BookingFormState) {
    AuthService.ensureAuthenticated() // Wait for identity confirmation
    // ... proceed with Firestore transaction
}
```

## Security Configuration

To complement this architecture, the following **Firestore Security Rules** are recommended in the Firebase Console:

```javascript
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow public reads for instant loading
    // Allow writes only for authenticated users (even anonymous ones)
    match /{document=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

## Setup Instructions

To enable this feature in a new Firebase project:
1.  Go to the **Firebase Console**.
2.  Navigate to **Build > Authentication**.
3.  Click **Get Started** (if it's a new project).
4.  Go to the **Sign-in method** tab.
5.  Click **Add new provider** and select **Anonymous**.
6.  Click **Enable** and save.
