# Feature: Photographer

Photographer search and detail view module using MVI pattern.

## OVERVIEW

Location-based photographer discovery with map visualization and detailed profile view including portfolio and reviews.

**Dependencies:** `core:domain`, `core:data`, `core:ui`, `core:common`, Kakao Maps SDK

## STRUCTURE

```
ui/screen/
├── search_photographer/       # Map-based search
│   ├── SearchPhotographerScreen.kt
│   ├── SearchPhotographerViewModel.kt
│   ├── SearchPhotographerState.kt
│   ├── SearchPhotographerIntent.kt
│   ├── SearchPhotographerSideEffect.kt
│   ├── KakaoMapView.kt
│   ├── composable/            # UI components
│   │   ├── PhotographerCard.kt
│   │   ├── PhotographerSheet.kt
│   │   └── PhotographerListSheet.kt
│   └── handler/               # Business logic handlers
└── detail_photographer/       # Profile detail
    ├── DetailPhotographerScreen.kt
    ├── DetailPhotographerViewModel.kt
    ├── DetailPhotographerState.kt
    ├── DetailPhotographerIntent.kt
    ├── DetailPhotographerSideEffect.kt
    ├── DetailProfileSection.kt
    ├── PortfolioSection.kt
    ├── ReviewSection.kt
    ├── PhotoPriceSection.kt
    ├── portfolio/SinglePortfolio.kt
    └── review/SingleReview.kt, ReviewBars.kt
```

## KEY SCREENS

| Screen | Description | State |
|--------|-------------|-------|
| `SearchPhotographerScreen` | Map with photographer markers, bottom sheet list | `userLocation`, `nearbyPhotographers`, `selectedPhotographerId` |
| `DetailPhotographerScreen` | Profile, reviews, portfolios, pricing | `profileInfo`, `reviewSummary`, `reviews`, `portfolios` |
| `DetailPhotographerPortfoliosScreen` | Full portfolio gallery | - |
| `DetailPhotographerReviewScreen` | All reviews list | - |
