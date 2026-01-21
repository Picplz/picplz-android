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

## GIT WORKFLOW

### Issue-Driven Development
1. **모든 작업은 GitHub Issue 기반** - 이슈 먼저 생성/확인 후 작업 시작
2. **1 Issue = 1 Branch = 1 PR** - 이슈와 브랜치, PR이 1:1 대응

### Branch Strategy
```
main ← develop ← feat/이슈번호
                 fix/이슈번호
                 refactor/이슈번호
                 docs/이슈번호
```

- **Base**: 항상 `develop` 브랜치에서 분기
- **Naming**: `{type}/{issue-number}` (예: `feat/12`, `refactor/86`)
- **PR Target**: `develop`으로 머지

### Commit Convention (Gitmoji)
| Emoji | Code | Description |
|-------|------|-------------|
| ✨ | `:sparkles:` | 새 기능 |
| 🐛 | `:bug:` | 버그 수정 |
| ♻️ | `:recycle:` | 리팩토링 |
| 🔥 | `:fire:` | 코드/파일 삭제 |
| 📝 | `:memo:` | 문서 |
| 🚚 | `:truck:` | 파일 이동/이름 변경 |

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

## DESIGN

**Figma**: [픽플즈 디자인](https://www.figma.com/design/Lf9FSdH8jJeIeDxdTEHLbL/%ED%94%BD%ED%94%8C%EC%A6%88)

## NOTES
- **Android Studio**: Use "Android" view or `Shift+Shift` for navigation.
- **SDK**: AGP 8.7.0, Gradle 8.9, compileSdk 35.
- **Secrets**: `local.properties` required for API keys.
