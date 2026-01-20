# APP MODULE

## OVERVIEW

Application entry point module. Contains `MainActivity`, `MyApplication` (Hilt), and `MainNavHost` for navigation orchestration. All screens have been migrated to feature modules.

**Status:** Migrated. Pure shell module.

## STRUCTURE

```
app/src/main/kotlin/com/hm/picplz/
├── Application.kt          # @HiltAndroidApp, Kakao SDK init
├── MainActivity.kt         # Single Activity, SplashScreen, NavHost setup
├── navigation/
│   └── MainNavHost.kt      # Central NavHost composable (all routes)
├── di/                     # Hilt modules (Api, Auth, Config, etc.)
└── ui/
    └── main/               # MainActivityViewModel, UiState
```

## WHERE TO LOOK

| Task | Location |
|------|----------|
| Add new navigation route | `navigation/MainNavHost.kt` |
| Modify app startup | `Application.kt`, `MainActivity.kt` |
| Add Hilt module | `di/` directory |

## NOTES

- **Routes** are defined in `core:common` (`Routes.kt`), not in app module.
- **DI Modules** here provide app-level dependencies (API, Auth, Config). Feature-specific DI should live in feature modules.
- **Screens**: App module contains NO screens. All screens are in `feature:*` modules.
- **SplashScreen** uses AndroidX SplashScreen API with custom fade-out animation.
- **Double-back-to-exit** is implemented in `MainActivity`.
