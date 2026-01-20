# PICPLZ PROJECT KNOWLEDGE BASE

**Generated:** 2026-01-20
**Commit:** f07e768
**Branch:** refactor/77

## OVERVIEW

Android 사진작가-고객 매칭 플랫폼. Jetpack Compose + Hilt + MVI 패턴. 멀티모듈 마이그레이션 완료.

## DOCUMENTATION INDEX

| Module | Description | Path |
|--------|-------------|------|
| **App** | Main entry, Navigation Host | [app/AGENTS.md](app/AGENTS.md) |
| **Core UI** | Common components, Theme, Resources | [core/ui/AGENTS.md](core/ui/AGENTS.md) |
| **Core Data** | API, Repository Impl, Network | [core/data/AGENTS.md](core/data/AGENTS.md) |
| **Core Domain** | UseCases, Models, Interfaces | [core/domain/AGENTS.md](core/domain/AGENTS.md) |
| **Feature Auth** | Login, Sign Up flows | [feature/auth/AGENTS.md](feature/auth/AGENTS.md) |
| **Feature Chat** | Chat rooms, Messaging | [feature/chat/AGENTS.md](feature/chat/AGENTS.md) |
| **Feature Photographer** | Search, Detail, Portfolio | [feature/photographer/AGENTS.md](feature/photographer/AGENTS.md) |
| **Feature Feed** | Feed tab | [feature/feed/AGENTS.md](feature/feed/AGENTS.md) |
| **Feature Main** | Home, Search, Reservation | [feature/main/AGENTS.md](feature/main/AGENTS.md) |

## PROJECT STRUCTURE

```
picplz/
├── app/                    # Application entry point
├── core/                   # Shared layers
│   ├── common/             # Utilities & Constants
│   ├── data/               # Data Layer
│   ├── domain/             # Domain Layer
│   └── ui/                 # UI Layer (Design System)
└── feature/                # Feature modules
    ├── auth/               # Authentication
    ├── chat/               # Chat functionality
    ├── photographer/       # Photographer discovery
    ├── mypage/             # User profile
    ├── feed/               # Feed tab
    └── main/               # Home, Search, Reservation
```

## GLOBAL CONVENTIONS

### MVI Pattern (Strict)
All features must follow this 5-file structure:
- `Screen`: Composable UI
- `ViewModel`: Hilt-injected state holder
- `State`: Data class + `idle()`
- `Intent`: User actions
- `SideEffect`: One-off events (Navigation, Toast)

### Module Dependencies
```
app → features
features → core:domain, core:ui, core:common
core:data → core:domain
core:ui → core:common
```

## COMMANDS

```bash
# Build Debug
./gradlew assembleDebug

# Build Specific Module
./gradlew :feature:auth:compileDebugKotlin

# Clean
./gradlew clean

# Code Quality (pre-commit hook runs these)
./gradlew ktlintCheck    # Lint check
./gradlew ktlintFormat   # Auto-fix lint issues
./gradlew detekt         # Static analysis
```

## CODE QUALITY

| Tool | Version | Config | pre-commit |
|------|---------|--------|------------|
| ktlint | 12.1.2 | `.editorconfig` | ✅ |
| detekt | 1.23.7 | `detekt.yml` | ✅ |

### Conventions
- **Composable 함수**: PascalCase 허용 (`ktlint_function_naming_ignore_when_annotated_with=Composable`)
- **MaxLineLength**: 120자
- **미사용 Route 파라미터**: `_` prefix + `@Suppress("UNUSED_PARAMETER")`
- **빈 catch 블록**: 의도적 무시 시 주석 필수 또는 `@Suppress("SwallowedException")`

## MIGRATION STATUS (refactor/77)

| Module | Status | Notes |
|--------|--------|-------|
| core:* | ✅ | All core modules migrated |
| feature:auth | ✅ | Complete |
| feature:chat | ✅ | Complete |
| feature:photographer | ✅ | Complete |
| feature:mypage | ✅ | Complete |
| feature:feed | ✅ | Complete |
| feature:main | ✅ | Complete |
| app | ✅ | Cleanup & Dependency linking done |

## NOTES
- **Android Studio**: Use "Android" view or `Shift+Shift` for navigation.
- **SDK**: AGP 8.7.0, Gradle 8.9, compileSdk 35.
- **Secrets**: `local.properties` required for API keys.
