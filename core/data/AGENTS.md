# CORE:DATA MODULE

## OVERVIEW

Data layer implementing `core:domain` repository interfaces. Handles all network communication via Retrofit/OkHttp with Gson serialization. Provides Kakao SDK integration for authentication and maps.

**Dependencies:** `core:domain`, `core:common`

## STRUCTURE

```
data/
├── api/              # Retrofit API interfaces
│   ├── AuthApi.kt
│   ├── PhotographerApi.kt
│   ├── AddressApi.kt
│   └── KakaoMapApi.kt
├── model/            # Network DTOs only
├── mapper/           # DTO → Domain transformers
├── repository/       # Repository implementations
├── service/          # Business logic wrappers
├── source/           # DataSource interfaces + impl
└── provider/         # Config, Token, Interceptor
```

## KEY FILES

| File | Purpose |
|------|---------|
| `api/*Api.kt` | Retrofit endpoint definitions |
| `model/*Dto.kt` | Network response DTOs |
| `repository/*Impl.kt` | Domain repository implementations |
| `source/*Source.kt` | API call wrappers with error handling |
| `provider/ConfigProvider.kt` | API keys interface (impl in `:app`) |
| `provider/AuthInterceptor.kt` | OkHttp token injection |
| `provider/TokenManager.kt` | JWT token storage |

## DATA FLOW

```
ViewModel → UseCase → Repository(Impl) → Source → Api → Network
                                              ↓
                            DTO → mapper → Domain/Feature Model
```

## BACKEND API

| Resource | URL |
|----------|-----|
| **Swagger UI** | http://43.203.62.97:8080/api/v1/swagger-ui/index.html |
| **OpenAPI Spec** | http://43.203.62.97:8080/api/v1/v3/api-docs |
| **Base URL** | `http://43.203.62.97:8080/api/v1` |

## NOTES

- **API Keys**: `ConfigProvider` implementation in `:app` module reads from `local.properties`
- **Required keys**: `kakaoRestApiKey`, `devGuestToken`, `devUserToken`
- **Error Handling**: All Sources wrap API calls in `AppResult<T>` using `runCatchingAppError` or `safeApiCall`. HTTP failures must use `toHttpAppError()` so backend error-envelope fields are preserved.
- **Model Boundary**: `core:data/model` contains backend DTOs only. Domain/UI-facing models live in `core:domain/model` or the owning feature.
- **Mappers**: use dedicated mapper files for DTO → domain/feature model conversion. Feature modules must not import `com.hm.picplz.data.model.*` except true DTO-only test fixtures.
