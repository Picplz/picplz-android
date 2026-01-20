# Feature: Main

**Module:** `feature:main`
**Namespace:** `com.hm.picplz.feature.main`

## OVERVIEW

Home screen and main navigation tabs. Includes Photographer Search (User side), Photographer Home (Photographer side), and Reservations.

**Dependencies:** `core:domain`, `core:ui`, `core:common`, `feature:auth`, `feature:photographer`

## STRUCTURE

```
ui/screen/
├── main/                    # User Home Tab
│   ├── MainScreen.kt        # Dashboard
│   └── MainSearchScreen.kt  # Global Search
├── photographer_main/       # Photographer Home Tab
│   ├── PhotographerMainScreen.kt
│   └── composable/          # Equipment setting, etc.
└── reservation/             # Reservation Tab
    └── ReservationScreen.kt
```

## KEY SCREENS

| Screen | Route | Description |
|--------|-------|-------------|
| `MainScreen` | `MAIN` | User dashboard with popular photographers/portfolios |
| `MainSearchScreen` | `MAIN_SEARCH` | Keyword search for areas/tags |
| `PhotographerMainScreen` | `PHOTOGRAPHER_MAIN` | Photographer dashboard (reservation status) |
| `ReservationScreen` | `RESERVATION` | Booking history and status |

## NOTES

- **Legacy Structure**: Contains mixed responsibilities (User/Photographer home). Consider splitting `photographer_main` to separate module in future.
- **Search**: Uses `MainSearchScreen` for global search, distinct from `SearchPhotographerScreen` (Map-based).
