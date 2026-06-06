---
name: semantic-naming-over-implementation
description: 코드 리뷰 시 구현 노출 네이밍을 역할/의도 기반 네이밍으로 교정. 컴포넌트, props, hooks, 함수 이름이 구현체(라이브러리명, 내부 구조)를 드러내는지 검출하고 시맨틱한 대안을 제시한다.
---

# Semantic Naming over Implementation Details

이름은 **구현 방식**이 아니라 **역할/의도**를 드러내야 한다.

## 핵심 판단 기준

> "이 라이브러리를 다른 것으로 교체해도 이름이 여전히 말이 되는가?"

- `RequireAtoms` → Jotai 걷어내면 의미 없음 ❌
- `RouteGuard` → 상태 라이브러리 무관하게 역할 동일 ✅

## 검출 패턴

### 컴포넌트 이름에 구현체 노출
```tsx
// Bad
<RequireAtoms atom={searchCompleteAtom} />
<QuerySuspenseWrapper query={ticketsQuery} />
<ZustandProvider store={appStore} />

// Good
<SearchCompleteGuard />
<TicketListLoader />
<AppStateProvider />
```

### 제네릭 + 구체적 인스턴스 패턴
제네릭은 범용 이름, 사용처에서 역할로 래핑:
```tsx
// 제네릭 (shared)
function RouteGuard({ condition, redirectTo }) { ... }

// 사용처 — 역할 명시
const SearchCompleteGuard = () => (
  <RouteGuard condition={searchCompleteAtom} redirectTo="/search" />
);
```

### Props에 구현 용어
```tsx
// Bad
atom={searchCompleteAtom}
zustandSelector={selectUser}

// Good
condition={searchCompleteAtom}
selectUser={selectUser}
```

### Hooks
```tsx
// Bad: 구현 방식 노출
useAtomState()
useQueryFetch()
useJotaiDerived()

// Good: 도메인 의미
useSelectedStation()
useTicketSearch()
useTotalPassengers()
```

## 리뷰 시 체크

1. 이름에 라이브러리명이 포함되어 있는가? (Atom, Query, Zustand, Redux, Jotai...)
2. 이름이 "어떻게"를 설명하는가, "무엇을/왜"를 설명하는가?
3. 라이브러리를 교체해도 이름이 유효한가?
