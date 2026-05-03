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

### SideEffect Channel Rule (Boy Scout Rule)
**일회성 SideEffect는 `Channel` 사용** (SharedFlow 금지)

```kotlin
// ✅ 권장
private val _sideEffect = Channel<SideEffect>(Channel.BUFFERED)
val sideEffect = _sideEffect.receiveAsFlow()

// ❌ 금지 - 여러 화면이 백스택에 있으면 중복 실행됨
private val _sideEffect = MutableSharedFlow<SideEffect>()
```

| | SharedFlow | Channel |
|---|---|---|
| 소비자 | 모든 collector | 단일 collector |
| 용도 | 상태 브로드캐스트 | 일회성 이벤트 |

- **적용 시점**: 해당 ViewModel 수정 시 함께 변경 (Boy Scout Rule)
- **신규 작성 시**: 처음부터 Channel 사용

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
3. **커밋/푸시/PR 생성 전 반드시 사용자 확인** - 임의로 올리지 않는다
4. **공통 모듈(core/) 파일 생성/수정 전 동명 파일 존재 여부 + 사용처 확인 필수** - 기존 컴포넌트를 덮어쓰지 않는다

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

### String Resource Rule
- **UI에 표시되는 모든 텍스트는 `strings.xml`에 정의** — 하드코딩 금지
- 파일 위치: `core/ui/src/main/res/values/strings.xml` (공통) 또는 각 feature 모듈의 `res/values/strings.xml`
- 네이밍: `{화면}_{용도}` (예: `quick_shoot_permission_denied_title`, `cancel_reservation_button_home`)
- `contentDescription`도 string resource 사용
- Compose `Text`에서 줄바꿈이 필요한 문자열은 `\n` 사용 — `&#10;` 엔티티는 실제 UI에서 줄바꿈이 보장되지 않음

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

## BACKEND API

| Resource | URL |
|----------|-----|
| **Swagger UI** | http://43.203.62.97:8080/api/v1/swagger-ui/index.html |
| **OpenAPI Spec** | http://43.203.62.97:8080/api/v1/v3/api-docs |
| **Base URL** | `http://43.203.62.97:8080/api/v1` |

### API Integration Status

| Endpoint | Method | Status | Android Layer |
|----------|--------|--------|---------------|
| `/auth/kakao` | POST | ✅ Connected | AuthApi → AuthSource → AuthService → AuthRepository |
| `/members/nickname` | GET | ✅ Connected | MemberApi → MemberSource → MemberService |
| `/cameras` | GET | ✅ Connected | CameraApi → CameraSource → CameraService |
| `/s3/presigned-upload-url` | GET | ✅ Connected | S3Api → S3Source → S3Service |
| `/areas/search` | GET | ✅ Connected | AddressApi → AddressSource → AddressService |
| `/areas/nearby` | GET | ✅ Connected | AddressApi → AddressSource → AddressService |
| `/customers` | POST | ❌ Not yet | Needs CustomerApi + signup flow |
| `/photographers` | POST | ❌ Not yet | PhotographerApi is empty stub |
| `/photographers/login` | POST | ❌ Not yet | — |
| `/members/info` | PATCH | ❌ Not yet | — |
| `/members/location` | POST | ❌ Not yet | — |
| `/chat/rooms` | GET/POST | ❌ Not yet | — |
| `/portfolios` | POST | ❌ Not yet | — |
| `/products` | POST | ❌ Not yet | — |
| `/reviews` | POST/PUT/DELETE | ❌ Not yet | — |

### Data Layer Patterns

**Simple (no domain logic)**: `Api → Source (runCatching) → Service → ViewModel`
- Used by: MemberApi, CameraApi, S3Api

**Full (with domain logic)**: `Api → Source → Service → Repository → UseCase → ViewModel`
- Used by: AuthApi (login flow)

**Source Rule**: All Sources wrap API calls in `runCatching` returning `Result<T>`

## DATA LAYER FILES

```
core/data/src/main/kotlin/com/hm/picplz/data/
├── api/
│   ├── AuthApi.kt          # POST /auth/kakao
│   ├── MemberApi.kt        # GET /members/nickname
│   ├── CameraApi.kt        # GET /cameras
│   ├── S3Api.kt            # GET /s3/presigned-upload-url
│   ├── AddressApi.kt       # GET /areas/search, /areas/nearby
│   ├── PhotographerApi.kt  # ❌ EMPTY STUB — needs POST /photographers
│   └── KakaoMapApi.kt      # Kakao Map SDK (external)
├── model/
│   ├── KakaoModel.kt       # Login DTOs
│   ├── CameraModel.kt      # CameraInfoDto + CameraListData
│   ├── S3Model.kt          # UploadUrlResponseDto
│   └── Device.kt           # DeviceBrand + DeviceData (hardcoded phone list)
├── source/                  # Interface + Impl pairs
├── service/                 # Interface + Impl pairs
├── repository/              # Domain repo implementations
└── provider/
    ├── TokenManager.kt      # JWT + social info storage
    ├── AuthInterceptor.kt   # OkHttp token injection
    └── ConfigProvider.kt    # API keys interface
```

## DESIGN

**Figma**: [픽플즈 디자인](https://www.figma.com/design/Lf9FSdH8jJeIeDxdTEHLbL/%ED%94%BD%ED%94%8C%EC%A6%88)

## NOTES
- **Android Studio**: Use "Android" view or `Shift+Shift` for navigation.
- **SDK**: AGP 8.7.0, Gradle 8.9, compileSdk 35.
- **Secrets**: `local.properties` required for API keys.
