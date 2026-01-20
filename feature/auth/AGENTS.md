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
                        │       ├─→ [Client] → SignUpClientScreen → Completion
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
| **SignUpClientViewModel** | `ui/screen/sign_up/sign_up_client/` | Client-specific signup state |

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
