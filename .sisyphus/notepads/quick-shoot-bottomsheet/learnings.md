# QuickShoot Bottom Sheet Refactor — Learnings

## 2026-02-23 Initial Setup
- CommonBottomSheetScaffold는 QuickShoot에서만 사용됨 → 안전하게 수정 가능
- CommonModalBottomSheet는 5곳에서 사용 중 (feature/main 4곳 + feature/auth 1곳)
- CommonModalBottomSheet의 기본 skipPartiallyExpanded = true → State C용은 false로 해야 ~50% 높이에 멈춤
- PhotographerSheet가 hiltViewModel() 직접 호출 → ModalBottomSheet 안에서는 스코프 문제 가능 → params로 전환
- PhotographerListSheet에 이미 nestedScrollConnection 있음 → BottomSheetScaffold와 호환 확인 필요
- CustomDragHandle()이 CommonModalBottomSheet.kt에 이미 정의되어 있음 (40x4dp, black, RoundedCornerShape(6.dp))

## 2026-02-23 Refactor Decisions
- QuickShoot의 상태 관리는 `selectedPhotographerId` 단일 소스로 유지하고, State C는 `selectedPhotographer != null` 조건으로만 표시
- `CommonBottomSheetScaffold`는 QuickShoot 전용 요구에 맞춰 sheet min/max API와 내부 Surface/offset 해킹을 제거해 단순화
- 선택 해제 시(지도 빈 영역 클릭/모달 dismiss) `SetSelectedPhotographerId(null)`와 `CenterSelectedPhotographer(Offset.Zero)`를 함께 호출해 지도 중심 이동 상태를 초기화
- `PhotographerSheet`는 ViewModel 직접 참조를 제거하고 `Photographer` 파라미터 기반으로 재구성하여 ModalBottomSheet 스코프 분리 이슈를 회피

## 2026-02-24 Sort Bottom Sheet Integration
- QuickShoot 정렬 UI는 기존 `SortFilterModalBottomSheet` 패턴을 그대로 복제해 `QuickShootSortBottomSheet`로 분리 (57dp 옵션, 0.96dp divider, dragHandle 없음)
- 정렬 시트 상태는 ViewModel 수정 없이 `QuickShootState(showSortSheet, selectedSortType)` + `PhotographerSearchHandler` 인텐트 처리로 연결
- `PhotographerListSheet`의 "거리순" 텍스트를 `selectedSortType.label`로 치환하고 클릭 시 `ToggleSortSheet(true)`를 보내 바텀시트 트리거

## 2026-02-24 QuickShoot Empty State
- 빈 상태 분기는 `entirePhotographers.isEmpty() && !isSearchingPhotographer && userLocation != null` 조건으로 지도 렌더링과 분리
- 헤더(`AddressMarker`, `RefetchButton`)는 GPS 로딩 이후 항상 먼저 렌더링하고, 지도 영역만 Empty/Normal로 분기
- 슬픈 캐릭터 전용 drawable이 `core/ui/src/main/res/drawable/empty_character.xml`로 이미 존재해 `center_char` 대체 가능

## 2026-02-24 Photographer Card List UI Refactor
- `DistanceText` 시그니처를 `(distance: Long)` 단일 파라미터로 단순화 — duration 하드코딩 제거, `MainThemeFont.Body` 적용
- `PhotographerCard`에서 `NavHostController` 제거 → `onClick: () -> Unit` 콜백으로 전환 (프로젝트 컨벤션: composable은 navigation controller 미보유)
- `PhotographerCard`의 프로필 이미지를 `Image + rememberAsyncImagePainter` → `AsyncImage(contentScale = Crop, clip = RoundedCornerShape(8.dp))`로 변경
- `PhotographerCard`에서 `width(345.dp)` 하드코딩 → `fillMaxWidth()`로 변경
- `PhotographerCard` 이름 텍스트를 `TextStyle(fontWeight, fontSize...)` 인라인 → `MainThemeFont.BodyBold`로 통일
- `PhotographerListSheet`를 `hiltViewModel()` 의존에서 데이터 파라미터 기반으로 전환 (`photographers: FilteredPhotographers`, `selectedSortType`, `onSortClick`, `onPhotographerClick`)
- `PhotographerListSheet` LazyColumn에 `HorizontalDivider(color = Gray2, 1.dp)` 카드 간 구분선 추가
- `PhotographerListSheet`의 filter chips(statusTags + vibeTags)는 그대로 유지 — bottom sheet peek content 역할
- Preview 함수를 `private`으로 변경하고 `@Suppress("UnusedPrivateMember")` 추가 (팀 컨벤션)
