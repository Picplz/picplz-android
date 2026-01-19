# PicPlz - 사진작가 매칭 플랫폼

## 기술 스택
- **UI**: Jetpack Compose + Kotlin
- **DI**: Hilt
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
app/src/main/java/com/ssuspot/picplz/
├── ui/
│   ├── auth/          # 로그인/인증
│   ├── signup/        # 회원가입 (client/photographer)
│   ├── main/          # 메인 화면
│   ├── chat/          # 채팅
│   ├── mypage/        # 마이페이지
│   ├── photographer/  # 작가 상세/검색
│   └── common/        # 공통 컴포넌트
├── data/              # Repository, API, DTO
└── domain/            # UseCase, Entity
```

## 공통 컴포넌트 (ui/common)
- `CommonTopBar` - 상단 앱바
- `CommonBottomButton` - 하단 버튼
- `CommonChip` - 칩 컴포넌트
- `CommonTextField` - 텍스트 필드
- `CommonDialog` - 다이얼로그

## MVI 패턴 규칙
각 화면은 다음 구조를 따름:
- `*Screen.kt` - Composable UI
- `*ViewModel.kt` - 상태 관리
- `*State.kt` - UI 상태 데이터 클래스
- `*Intent.kt` - 사용자 액션
- `*SideEffect.kt` - 일회성 이벤트 (네비게이션, 토스트 등)

## 코딩 컨벤션
- Composable 함수명: PascalCase
- State 클래스: `data class *State`
- Intent: `sealed interface *Intent`
- SideEffect: `sealed interface *SideEffect`
