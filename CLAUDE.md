# PicPlz - 사진작가 매칭 플랫폼

## 기술 스택
- **UI**: Jetpack Compose + Kotlin
- **DI**: Hilt (`@HiltViewModel` + `@Inject constructor()`)
- **네트워크**: Retrofit + OkHttp
- **인증**: Kakao SDK
- **아키텍처**: MVI 패턴 (State/Intent/SideEffect)
- **버전**: 1.0.4-dev

## Figma 연동
- **fileKey**: `Lf9FSdH8jJeIeDxdTEHLbL`
- **주요 노드**: `5371-202340`
- Figma MCP 도구: `get_figma_data`, `download_figma_images`

## 프로젝트 구조
```
app/src/main/kotlin/com/hm/picplz/
├── ui/
│   ├── main/                      # MainActivity 관련
│   │   └── MainActivityViewModel.kt
│   ├── screen/
│   │   ├── login/                 # 로그인
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   ├── LoginState.kt
│   │   │   ├── LoginIntent.kt
│   │   │   └── LoginSideEffect.kt
│   │   ├── chat/                  # 채팅 목록
│   │   ├── chat_room/             # 채팅방
│   │   ├── sign_up/
│   │   │   ├── sign_up_common/    # 회원가입 공통
│   │   │   ├── sign_up_client/    # 고객 회원가입
│   │   │   └── sign_up_photographer/  # 작가 회원가입
│   │   ├── search_photographer/   # 작가 검색 (지도)
│   │   ├── detail_photographer/   # 작가 상세
│   │   ├── photographer_main/     # 작가 메인
│   │   ├── my_page/               # 마이페이지
│   │   └── common/                # 공통 컴포넌트
│   │       └── common_chip/
│   ├── theme/                     # 테마, 색상, 폰트
│   └── components/                # 재사용 컴포넌트
├── data/
│   ├── api/                       # Retrofit API 인터페이스
│   ├── model/                     # DTO
│   ├── repository/                # Repository 구현체
│   └── service/                   # 서비스 (Auth, Location 등)
├── domain/
│   ├── model/                     # 도메인 엔티티
│   ├── repository/                # Repository 인터페이스
│   └── usecase/                   # UseCase
├── di/                            # Hilt 모듈
└── navigation/                    # Navigation 그래프
```

## MVI 패턴 규칙 (Feature-based)

**각 feature 폴더에 모든 MVI 파일이 함께 위치:**
```
ui/screen/{feature}/
├── {Feature}Screen.kt       # Composable UI
├── {Feature}ViewModel.kt    # 상태 관리 (@HiltViewModel)
├── {Feature}State.kt        # UI 상태 (data class)
├── {Feature}Intent.kt       # 사용자 액션 (sealed interface)
├── {Feature}SideEffect.kt   # 일회성 이벤트 (sealed interface)
└── composable/              # 화면 전용 하위 컴포넌트
```

## 코딩 컨벤션

### ViewModel
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState.idle())
    val state: StateFlow<LoginState> get() = _state
    
    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect: SharedFlow<LoginSideEffect> get() = _sideEffect
    
    fun handleIntent(intent: LoginIntent) { ... }
}
```

### State
```kotlin
data class LoginState(
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    companion object {
        fun idle() = LoginState()
    }
}
```

### Intent & SideEffect
```kotlin
sealed interface LoginIntent {
    data object NavigateToKaKao : LoginIntent
    data class LoginWithKaKao(val accessToken: String) : LoginIntent
}

sealed interface LoginSideEffect {
    data object LoginSuccess : LoginSideEffect
    data class NavigateToSignUp(val profileImageUrl: String?) : LoginSideEffect
}
```

## 공통 컴포넌트 (ui/screen/common)
- `CommonTopBar` - 상단 앱바
- `CommonBottomButton` - 하단 버튼
- `CommonChip` - 칩 컴포넌트
- `CommonTextField` - 텍스트 필드
- `CommonDialog` - 다이얼로그
- `CommonScrollPicker` - 스크롤 피커
