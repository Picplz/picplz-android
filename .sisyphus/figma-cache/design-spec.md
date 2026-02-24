# QuickShoot Figma Design Spec Cache
Generated: 2026-02-23
Source: Figma file `Lf9FSdH8jJeIeDxdTEHLbL`, section "고객 - 지도" (node 5371-198775)

## Images (local cache)
| Frame | File | Node ID | Description |
|-------|------|---------|-------------|
| base-no-people.png | 750x1624 | 5371:199158 | 기본 (사람없음) - Empty state |
| bar-raised-1.png | 750x1624 | 5373:27995 | 바 1번 올림 - Bottom sheet partially raised |
| bar-raised-2.png | 750x1624 | 5373:28228 | 바 올림 - Bottom sheet fully expanded |
| map-click-landing.png | 750x1624 | 5371:200032 | 지도에서 클릭시 - 랜딩 - Photographer selected |
| order-bottom-sheet.png | 750x500 | 5711:17151 | 순서 바텀시트 - Sort options |
| location-permission.png | 750x1624 | 6384:16296 | 위치 접근 동의 - Location permission (DONE) |

---

## Frame 1: 기본 (사람없음) — Empty State
**When**: No photographers found nearby

### Header
- Left: Pin icon + "마포구 서교동" (bold, ~16px, black)
- Right: "내 위치 새로고침 ↻" (pill button, outlined, gray text)

### Map Area (center, ~60% height)
- Background: Light gray with concentric circle patterns
- Empty state centered content:
  - Text: "주변에 바로 촬영 중인 / 작가가 없습니다" (bold, ~18px, black)
  - Illustration: Cute character with sunglasses (~60px)
  - Subtext: "다른 지역으로 / 이동해 보는 건 어때요?" (regular, ~14px, gray)

### Filter Chips (above bottom nav)
- "✓ 필로우" (outlined, light gray)
- "🟢 빠른촬영 가능" (green background, white text)

### Bottom Nav
- 4 tabs: 홈 | 빠른촬영(active, black) | 채팅 | 마이페이지

---

## Frame 2: 바 1번 올림 — Bottom Sheet Partially Raised
**When**: Photographers found, sheet at peek height

### Header (same as Frame 1)

### Map Area
- 5 circular photographer avatars distributed on map
- Each avatar: circular frame with dark border + green dot indicator + name label
- Center shows "주변 작가 찾는 중... 나" character

### Bottom Sheet (peek state)
- Drag handle bar at top
- Filter tags: "✓ 필로우" + "🟢 빠른촬영 가능"
- Content partially visible

### Bottom Nav (same)

---

## Frame 3: 바 올림 — Bottom Sheet Fully Expanded
**When**: User drags bottom sheet up

### Header (same)

### Bottom Sheet Content (expanded)
- Drag handle
- Filter pills: "필로우" (checkmark) + "빠른촬영 가능" (green checkmark)
- Sort dropdown: "거리순 ▼"
- Photographer card list (scrollable):

#### Photographer Card Layout
- **Left**: Square thumbnail image (~100px, rounded corners)
- **Right**:
  - Name: "유가영 작가" (bold, large)
  - Location: "마포구, 구로구, 노원구 외 3개" (gray subtitle)
  - Status: Green dot + "빠른촬영" badge (top-right of card)
  - Tags: "#을지로 감성" "#MZ 감성" etc.
- Card height: ~140px
- Divider: light gray horizontal line between cards
- 4+ cards visible

### Bottom Nav (same)

---

## Frame 4: 지도에서 클릭시 — Photographer Selected on Map
**When**: User taps a photographer avatar on the map

### Map Area
- Same photographer markers, one highlighted/selected

### Bottom Sheet Content (single photographer)
- Profile: Small circular avatar (32px) + "유가영 작가 @Gayoung"
- Status: Green dot + "바로촬영 마포구, 구로구, 노원구 외 3개"
- Equipment tags: "캐주얼" + "아이폰 16 Pro" (pill badges)
- Portfolio gallery: 3-column grid of portfolio images

### Bottom Nav (same)

---

## Frame 5: 순서 바텀시트 — Sort Options
**When**: User taps the "거리순 ▼" dropdown

### Options (vertical stack, center-aligned)
1. "거리순" (Distance)
2. "리뷰 많은 순" (Most reviews)
3. "별점순" (Star rating)

- Separated by horizontal dividers
- Bold, dark gray/black text
- Full-width tap areas
- No selection indicator visible in default state

---

## Frame 6: 위치 접근 동의 — Location Permission (ALREADY IMPLEMENTED)
**Status**: ✅ Implemented in Commit 2

### Key Design Details (for verification)
- Icon: Shield with checkmark on black circle background
- Title Line 1: "앱 서비스 이용을 위해" (BLACK)
- Title Line 2: "위치 접근 권한이 필요해요" (GREEN — ⚠️ our implementation uses all black!)
- Description: gray text, centered
- Button: "다음" — blue-gray background, white text, full-width rounded

### ⚠️ DISCREPANCY FOUND
Our implementation has BOTH title lines in black. Figma shows Line 2 in GREEN (#5FBB46 or similar).
This needs to be fixed.

---

## Design Tokens (observed)
| Token | Value |
|-------|-------|
| Primary text | #000000 (black) |
| Secondary text | #999999 (gray) |
| Accent green | #5FBB46 (approximate) |
| Background | #F5F5F5 (light gray) |
| Card divider | #E0E0E0 (light gray) |
| Sheet background | #FFFFFF (white) |
| Active tab | black icon + text |
| Inactive tab | gray icon + text |
