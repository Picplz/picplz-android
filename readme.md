# PicPlz

사진작가와 고객을 연결하는 Android 매칭 플랫폼

## Tech Stack

| Category | Stack |
|----------|-------|
| Language | Kotlin |
| UI | Jetpack Compose, Material3 |
| Architecture | MVI, Multi-module |
| DI | Hilt |
| Async | Coroutines, Flow |
| Map | Kakao Map SDK |
| Auth | Kakao Login |
| Distribution | Firebase App Distribution |

## Project Structure

```
picplz/
├── app/                    # Application entry point
├── core/
│   ├── common/             # Utilities, Constants
│   ├── data/               # Repository impl, API, Network
│   ├── domain/             # UseCases, Models, Interfaces
│   └── ui/                 # Design System, Theme, Common Components
└── feature/
    ├── auth/               # Login, Sign Up
    ├── chat/               # Chat rooms, Messaging
    ├── photographer/       # Search, Detail, Portfolio
    ├── mypage/             # User profile
    ├── feed/               # Feed tab
    └── main/               # Home, Search, Reservation
```

### Module Dependencies

```
app → features
features → core:domain, core:ui, core:common
core:data → core:domain
core:ui → core:common
```

## Getting Started

### Requirements

- Android Studio Ladybug (2024.2.1) 이상
- JDK 17
- Android SDK 35
- Gradle 8.9

### Setup

1. **Clone**
   ```bash
   git clone https://github.com/Picplz/picplz-aos.git
   cd picplz-aos
   ```

2. **local.properties 설정**
   ```properties
   sdk.dir=/path/to/android/sdk
   api_base_url=https://api.example.com
   kakao_native_app_key=your_kakao_native_app_key
   kakao_oauth_host=your_kakao_oauth_host
   kakao_rest_api_key=your_kakao_rest_api_key
   dev_guest_token=your_dev_guest_token      # 개발용 게스트 액세스 토큰
   dev_user_token=your_dev_user_token        # 개발용 유저 액세스 토큰
   ```

3. **Build**
   ```bash
   ./gradlew assembleDebug
   ```

## Development

### Code Quality

| Tool | Version | Command |
|------|---------|---------|
| ktlint | 12.1.2 | `./gradlew ktlintCheck` |
| detekt | 1.23.7 | `./gradlew detekt` |

Pre-commit hook이 설정되어 있어 커밋 시 자동으로 검사됩니다.

```bash
# 자동 포맷팅
./gradlew ktlintFormat
```

### Branch Strategy

```
main          ← production
  ↑
develop       ← integration
  ↑
feature/123   ← feature branches
fix/456       ← bug fix branches
```

**Naming Convention:**
- `feat/이슈번호` - 새 기능
- `fix/이슈번호` - 버그 수정
- `refactor/이슈번호` - 리팩토링
- `docs/이슈번호` - 문서

### Architecture (MVI)

각 feature는 5개 파일로 구성:

```
feature/
└── some_feature/
    ├── SomeScreen.kt           # Composable UI
    ├── SomeViewModel.kt        # State holder (Hilt)
    ├── SomeState.kt            # UI State (data class + idle())
    ├── SomeIntent.kt           # User actions (sealed interface)
    └── SomeSideEffect.kt       # One-off events (Navigation, Toast)
```

## Test Distribution

[Firebase Console](https://console.firebase.google.com/u/0/project/picplz-40a0c/)에서 App Distribution으로 테스트 배포 중

## Documentation

각 모듈별 상세 문서는 `AGENTS.md` 참조:
- [Root AGENTS.md](AGENTS.md)
- [App](app/AGENTS.md)
- [Core UI](core/ui/AGENTS.md)
- [Feature Auth](feature/auth/AGENTS.md)
