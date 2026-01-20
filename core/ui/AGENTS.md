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

## CONVENTIONS

1. **Prefix `Common`** - All reusable components must be prefixed
2. **Theme Colors** - Use `MainThemeColor.*` (Black, White, Gray1-4)
3. **Typography** - Use `pretendardTypography` from Theme.kt
4. **Composable Preview** - Include `@Preview` with `PicplzTheme` wrapper
