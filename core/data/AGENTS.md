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
├── model/            # DTOs with toDomain() mappers
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
                            DTO.toDomain() → Domain Model
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
- **Error Handling**: All Sources wrap API calls in `Result<T>` using `runCatching`
- **Mappers**: DTOs contain `toDomain()` extension or use dedicated mapper files
