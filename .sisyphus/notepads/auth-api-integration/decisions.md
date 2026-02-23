## 2026-02-21 Architecture Decisions

### Phase 2: Nickname Check
- Used simple pattern (Api → Source → Service → ViewModel) without Repository/UseCase
- Rationale: Simple GET endpoint, no complex domain logic needed
- MemberApi uses `Response<Unit>` — checks HTTP 200 vs 409 for availability

### Phase 1: Login Token
- Extended TokenManager with social info storage (socialCode, email, provider)
- JWT token saved immediately after login success in AuthRepositoryImpl
- LoginViewModel migrated to Channel (Boy Scout Rule)

### Phase 5: Customer Signup API Integration
- Added customer signup data path with simple pattern (CustomerApi -> CustomerSource -> CustomerService)
- CustomerSource keeps `runCatching` and treats non-2xx as failure with backend code/body details
- SignUpCommonViewModel now calls `POST /customers` only for `UserType.User` before completion navigation
- Request payload is assembled from signup state (`nickname`, `profileImageObjectKey`) + TokenManager social fields
- Added `isSubmitting` state and duplicate-submit guard to avoid repeated customer registration calls

### Phase 6: Photographer Signup API Integration
- Converted `PhotographerApi` from class stub to Retrofit interface with `POST /api/v1/photographers`
- Added photographer signup DTOs (`CreatePhotographerRequest`, `ActiveAreaRequest`, `PhotographerCameraRequest`)
- Extended existing photographer source/service with `createPhotographer` while keeping legacy dummy-list flow used by photographer discovery
- SignUpPhotographerViewModel now blocks duplicate submits, builds request from signup state + TokenManager social fields, and only navigates to completion after API success
