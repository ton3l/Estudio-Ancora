# Database Architecture

## Technology Stack
- **Database:** Google Cloud Firestore
- **Type:** NoSQL Document Database

## Data Model

The database consists of two primary collections.

### 1. Collection: `week-available-times`
**Purpose:** Defines the **default templates** for business hours. Used as a blueprint to generate actual booking days.

- **Document ID:** Day of the week (lowercase English).
  - Valid IDs: `monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`, `sunday`.

**Schema:**
```json5
{
  "open": true, // Boolean: Indicates if the business is open on this weekday.
  // Map<Hour, Available>
  "time-slots": {
    "8": true,  // Key: Hour (String/Int), Value: Boolean (Is this slot valid/bookable?)
    "9": true,
    "10": true,
    // ...
    "20": true
  }
}
```

### 2. Collection: `booking-days`
**Purpose:** Stores the actual state of bookings for specific calendar dates.

- **Document ID:** Date in ISO 8601 format (`YYYY-MM-DD`).
  - Example: `2026-02-23`

**Schema:**
```json5
{
  // Map<Hour, SlotData>
  "time-slots": {
    "8": {
      "booked": false,    // Boolean: Is this specific slot taken?
      "booking-id": ""    // String: Reference ID to the booking (empty if not booked)
    },
    "9": {
      "booked": true,
      "booking-id": "uuid-booking-123"
    }
    // Note: Only slots defined as 'true' in 'week-available-times' appear here.
  }
}
```

## Business Logic & Workflow

### 1. Lazy Initialization (On-Demand Creation)
Booking documents are not pre-generated. They are created using a **Just-in-Time (JIT)** strategy:
1. A user attempts to fetch availability for a specific date (e.g., `2026-02-23`).
2. The system checks if the document `booking-days/2026-02-23` exists.
3. **If missing:**
   - The system reads the corresponding template from `week-available-times` (e.g., `monday`).
   - It creates a new document in `booking-days`.
   - **Filtering Rule:** The new document will ONLY contain keys for time slots where the template value was `true`.

### 2. Template Synchronization
When the business owner modifies the default schedule in `week-available-times`:
- The application **provides a manual trigger** (UI button).
- **Action:** This trigger forces an update on existing `booking-days` documents for the **next 7 days**, synchronizing them with the new default `time-slots` configuration.
