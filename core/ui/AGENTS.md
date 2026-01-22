# Core UI Module

**Path:** `core/ui/`
**Namespace:** `com.hm.picplz.ui`

## OVERVIEW

Design System module providing reusable Compose components, theme, and drawable resources. All feature modules depend on this for consistent UI.

**Dependencies:** `core:common`

## KEY COMPONENTS

### Navigation
| Component | Description |
|-----------|-------------|
| `BottomNavigationBar` | Main nav with User/Photographer mode support |
| `BottomNavigationItem` | Sealed class defining nav destinations |

### Common Components (`ui/screen/common/`)
| Component | Purpose |
|-----------|---------|
| `CommonBottomButton` | Primary CTA button (60dp height) |
| `CommonTopBar` | Screen header with back navigation |
| `CommonDialog` | Modal dialog wrapper |
| `CommonBottomSheet` | Bottom sheet container |
| `CommonFilledTextField` | Filled text input |
| `CommonOutlinedTextField` | Outlined text input |
| `CommonSearchField` | Search input with icon |
| `CommonChip` | Selectable chip (has MVI: State/Intent/ViewModel) |
| `CommonCheckbox` | Checkbox with label |
| `CommonToggleSwitch` | Toggle switch |
| `CommonDropdownMenu` | Dropdown selector |

### Cards (`ui/screen/common/card/`)
- `PhotographerCard` - Photographer list item
- `PortfolioCard` - Portfolio grid item
- `ScheduleCardNone` - Empty schedule placeholder

## RESOURCES

**Drawables:** 78 assets in `res/drawable/`
- Navigation icons: `*_select.png`, `*_unselect.png`
- Status icons: `bookmark_*`, `like_*`, `star_*`
- UI elements: `close.xml`, `check.xml`, `arrow_*.xml`

## COMMON COMPONENT DESIGN PRINCIPLES

### 공통 컴포넌트의 본질

공통 컴포넌트는 **동일한 UI를 여러 곳에서 재사용**하기 위한 것이다.
스타일이 유동적이면 공통 컴포넌트의 의미가 없다.

### Props 최소화 원칙

**UI적 요소는 파라미터로 받지 않는다** - 피그마 기준으로 고정

| 받아야 할 것 | 예시 | 이유 |
|-------------|------|------|
| **도메인 텍스트** | `text: String` | 화면마다 다른 문구 |
| **도메인 동작** | `onClick: () -> Unit` | 화면마다 다른 비즈니스 로직 |
| **도메인 상태** | `enabled: Boolean` | 비즈니스 로직에 따른 활성화 |
| **레이아웃 통합** | `modifier: Modifier = Modifier` | 부모 레이아웃과의 통합, 접근성, 테스트 |

| 받으면 안 되는 것 | 예시 | 이유 |
|------------------|------|------|
| 색상 | `containerColor`, `contentColor` | 피그마 기준 고정 |
| 크기/여백 | `height`, `padding` | 피그마 기준 고정 |
| 폰트 스타일 | `textStyle`, `fontSize` | 디자인 시스템 고정 |
| 테두리 | `borderColor`, `borderWidth` | 피그마 기준 고정 |

### modifier는 왜 허용하는가?

`Modifier`는 스타일링이 아니라 **레이아웃 통합 도구**:
- 부모와의 배치: `padding`, `fillMaxWidth`, `weight`
- 시스템 통합: `imePadding`, `navigationBarsPadding`
- 접근성: `semantics`, `testTag`
- 애니메이션: `animateContentSize`

modifier 없으면 매번 `Box`/`Spacer`로 감싸는 보일러플레이트 발생.

### 스타일이 다르면 별도 컴포넌트

```
❌ CommonBottomButton(containerColor = KakaoYellow, ...)
✅ KakaoLoginButton(text, onClick)

❌ CommonTopBar(hasMenu = true, menuIcon = ...)
✅ CommonTopBarWithMenu(text, onClickBack, onClickMenu)
```

### Defaults Object 패턴 (Material3 컨벤션)

내부 상수는 `Defaults` object로 정의:

```kotlin
object CommonBottomButtonDefaults {
    val VerticalPadding = 14.dp
    val HorizontalPadding = 20.dp
    val CornerRadius = 8.dp
}
```

### 요약

> **공통 컴포넌트 = 도메인 정보만 받고, UI는 피그마대로 고정**

## CONVENTIONS

1. **Prefix `Common`** - All reusable components must be prefixed
2. **Theme Colors** - Use `MainThemeColor.*` (Black, White, Gray1-4)
3. **Typography** - Use `pretendardTypography` from Theme.kt
4. **Composable Preview** - Include `@Preview` with `PicplzTheme` wrapper
