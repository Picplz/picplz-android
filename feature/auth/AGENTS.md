# Feature: Auth Module

## OVERVIEW

Authentication feature handling Login and SignUp flows. Uses Kakao OAuth for login. Multi-step SignUp with internal NavHosts for client/photographer flows.

**Package:** `com.hm.picplz.feature.auth`

## SCREEN FLOW

```
LoginIntroScreen (Kakao OAuth)
    │
    ├─→ [Existing User] → MainScreen
    │
    └─→ [New User] → SignUpCommonNavHost
                        ├─→ SelectType → Nickname → ProfileImage
                        │       │
                        │       ├─→ [Client] → Completion
                        │       │
                        │       └─→ [Photographer] → SignUpPhotographerNavHost
                        │               └─→ Location → Experience → DetailExp
                        │                    → CareerPeriod → Vibe → Device → Completion
```

## KEY COMPONENTS

| Component | Path | Description |
|-----------|------|-------------|
| **LoginIntroScreen** | `ui/screen/login/` | Entry point with intro pager + Kakao login |
| **SignUpCommonNavHost** | `navigation/` | Internal nav for shared signup steps |
| **SignUpPhotographerNavHost** | `navigation/` | Internal nav for photographer-specific steps |
| **SignUpCommonViewModel** | `ui/screen/sign_up/sign_up_common/` | Shared state for nickname, profile, user type |
| **SignUpPhotographerViewModel** | `ui/screen/sign_up/sign_up_photographer/` | Photographer profile: location, vibe, device |

### MVI Structure (per flow)
- `*Screen.kt` - Composable UI
- `*ViewModel.kt` - Hilt-injected state holder
- `*State.kt` - Data class with `idle()` factory
- `*Intent.kt` - User actions sealed interface
- `*SideEffect.kt` - Navigation, Toast events

### Handlers & Validators
- `NicknameValidator` - Nickname validation rules
- `UserInfoHandler`, `UserTypeInfoHandler` - Common signup handlers
- `VibeChipHandler`, `CareerHandler`, `AreaSearchHandler`, `DeviceHandler` - Photographer handlers

## API INTEGRATION STATUS

### Login Flow
| Screen | API | Status | Notes |
|--------|-----|--------|-------|
| LoginIntroScreen | `POST /auth/kakao` | ✅ | Saves JWT + social info to TokenManager |

### Common Signup Flow (SignUpCommonViewModel)
| Screen | API | Status | Notes |
|--------|-----|--------|-------|
| SignUpNicknameScreen | `GET /members/nickname` | ✅ | 200=available, 409=duplicate |
| SignUpProfileImageScreen | `GET /s3/presigned-upload-url` + S3 PUT | ✅ | Uploads profile image, stores objectKey in state |
| SelectUserTypeScreen → Client | `POST /customers` | ✅ | Called before navigation to completion |
| SelectUserTypeScreen → Photographer | (navigation only) | ✅ | Navigates to photographer signup flow |

### Photographer Signup Flow (SignUpPhotographerViewModel)
| Screen | API | Status | Notes |
|--------|-----|--------|-------|
| SignUpAddDeviceScreen | `GET /cameras` | ✅ | Loaded on init, fallback to DeviceData |
| SignUpLocationScreen | `GET /areas/nearby` + `GET /areas/search` | ✅ | Nearby on init, keyword search |
| Submit (NavigateWithSubmit) | `POST /photographers` | ✅ | Maps vibes, areas, cameras to request body |

### Data Flow Pattern
```
SignUpCommonViewModel
├── MemberService → nickname check
├── S3Service → profile image upload
├── CustomerService → POST /customers (User type)
└── TokenManager → social info for signup requests

SignUpPhotographerViewModel
├── CameraService → camera list (init)
├── AddressService → area search/nearby
├── PhotographerService → POST /photographers (submit)
└── TokenManager → social info for signup requests
```

### SideEffect Channel Status
| ViewModel | Status |
|-----------|--------|
| LoginViewModel | ✅ Channel |
| SignUpCommonViewModel | ✅ Channel |
| SignUpPhotographerViewModel | ✅ Channel |
| SignUpClientViewModel | ✅ Channel (Boy Scout migrated) |
