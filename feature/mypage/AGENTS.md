# Feature: MyPage

**Module:** `feature:mypage`
**Namespace:** `com.hm.picplz.feature.mypage`

## OVERVIEW

User profile management module. Handles profile viewing/editing, shooting history, and account settings. Displays followed photographers, scrapped portfolios, and ongoing reservations.

**Dependencies:** `core:domain`, `core:ui`, `core:common`

## STRUCTURE

```
ui/screen/my_page/
├── MyPageScreen.kt              # Main profile dashboard
├── MyPageModifyProfileScreen.kt # Profile editing form
├── MyPageShootingHistoryScreen.kt # Shooting history list
├── MypageOrderSheetScreen.kt    # Order detail sheet
├── shootingHistoryCard/
│   ├── ShootingHistoryCard.kt   # History item card
│   └── SwipeableShootingHistoryCard.kt
└── toggleSwitch/
    └── ToggleSwitch.kt          # User/Photographer mode toggle
```

## KEY SCREENS

| Screen | Route | Description |
|--------|-------|-------------|
| `MyPageScreen` | `MY_PAGE` | Dashboard with profile, reservations, followed photographers |
| `MyPageModifyProfileScreen` | `MY_PAGE_MODIFY_PROFILE` | Edit nickname, bio, Instagram |
| `MyPageShootingHistoryScreen` | `MY_PAGE_SHOOTING_HISTORY` | List of completed/cancelled shoots |

## COMPONENTS

### ShootingHistoryCard
Displays shooting session info with status badge (`COMPLETED`/`CANCELLED`), date, location, and action button.

### ToggleSwitch
Animated toggle for user/photographer mode switch. Spring animation with shadow effect.

## ENUMS

- `ShootingStatus`: `COMPLETED`, `CANCELLED`
- `ReservationType`: `ASAP`, `NORMAL`
- `PackageType`: `PROFILE`, `KAKAO`, `INSTAGRAM`
