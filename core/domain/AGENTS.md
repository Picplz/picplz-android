# Core Domain Module

**Path:** `core/domain/`
**Namespace:** `com.hm.picplz.core.domain`

## OVERVIEW

Pure domain layer containing business logic, models, and repository interfaces. Framework-agnostic except for `@Inject` annotations for Hilt compatibility.

**Dependencies:** `core:common`, `javax.inject`, `kotlinx-coroutines-core`

## STRUCTURE

```
domain/src/main/kotlin/com/hm/picplz/domain/
├── model/              # Domain entities (data classes)
│   ├── AuthModel.kt    # KaKaoLoginResponse, KakaoUserInfo
│   ├── Chat.kt         # ChatMessage, ChatRoomInfo, MessageContent (sealed)
│   ├── Photographer.kt # Photographer, FilteredPhotographers
│   ├── Area.kt         # Area (location)
│   ├── Device.kt       # Device info
│   └── Equipment.kt    # Photography equipment
├── repository/         # Interfaces (implemented in core:data)
│   ├── AuthRepository.kt
│   └── PhotographerRepository.kt
└── usecase/            # Business operations
    ├── GetKakaoUserInfoUseCase.kt
    ├── LoginWithKakaoUseCase.kt
    └── UnlinkKakaoUseCase.kt
```

## CONVENTIONS

### UseCase Naming
- **Pattern:** `{Action}{Target}UseCase`
- **Examples:** `GetKakaoUserInfoUseCase`, `LoginWithKakaoUseCase`
- Always use `operator fun invoke()` for single-method execution
- Return `Result<T>` for error handling

### Repository Interfaces
- Define contracts only; implementations live in `core:data`
- Use `suspend` functions for async operations
- Return `Result<T>` to propagate errors cleanly

### Models
- Plain `data class` with immutable properties
- Sealed classes for polymorphic types (e.g., `MessageContent`)
- Avoid Android framework dependencies where possible
- Domain/UI-facing models that are consumed by feature modules belong here, not in `core:data/model`.
- Backend response/request shapes stay in `core:data/model` as DTOs and are converted through mappers.
