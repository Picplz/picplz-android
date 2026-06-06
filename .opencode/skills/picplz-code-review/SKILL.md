---
name: picplz-code-review
description: Picplz Android 코드 리뷰. Jetpack Compose + Hilt + MVI + 멀티모듈 규칙, SideEffect Channel 규칙, API 레이어 패턴, ktlint/detekt 검증을 중심으로 리뷰한다.
---

# picplz-code-review

Picplz Android 프로젝트 전용 코드 리뷰 스킬.

## When To Use

- 사용자가 "리뷰", "코드리뷰", "QA", "검증"을 요청할 때
- PR 전 변경사항을 점검할 때
- Auth, signup, reservation, photographer 등 feature 흐름을 실제 사용 플로우 관점에서 확인할 때
- MVI 상태/이벤트 구조가 바뀐 뒤 일회성 이벤트 중복 실행 위험을 확인할 때

## Review Order

1. `git diff --name-status origin/develop...HEAD`와 `git status --short --branch`로 리뷰 범위를 확정한다.
2. 변경 파일에 적용되는 `AGENTS.md`를 먼저 읽는다.
3. 변경 화면의 실제 사용자 흐름을 한 문장으로 요약한다.
4. 아래 체크리스트를 P0/P1/P2로 리뷰한다.
5. 가능하면 관련 모듈 compile/test/ktlint를 실행한다.

## P0 Checklist

- MVI 5파일 구조가 유지되는가: `Screen`, `ViewModel`, `State`, `Intent`, `SideEffect`
- 신규 또는 수정 ViewModel의 일회성 SideEffect가 `Channel(Channel.BUFFERED)` + `receiveAsFlow()`인가
- `MutableSharedFlow`가 navigation/toast/dialog one-shot event에 쓰이지 않았는가
- Compose 화면이 ViewModel을 직접 우회하지 않고 Intent로 사용자 액션을 전달하는가
- feature module이 `core:data` 구현체에 직접 의존하지 않는가
- module dependency가 `features -> core:domain/core:ui/core:common`, `core:data -> core:domain` 방향을 깨지 않는가
- API Source가 `runCatching`으로 `Result<T>`를 반환하는가
- 로그인/회원가입은 토큰/소셜 정보 저장 시점과 실패 경로가 명확한가
- UI 문자열은 하드코딩하지 않고 feature `strings.xml` 또는 core resource에 정의했는가
- navigation side effect가 back stack에 남은 collector에서 중복 실행될 구조가 아닌가

## P1 Checklist

- State 필드는 화면에서 실제로 쓰이며, 중복 submit/loading guard가 필요한 흐름에 반영됐는가
- Intent 이름은 구현이 아니라 사용자 의도/도메인 행동을 드러내는가
- Handler/mapper/service 이름이 역할 기반이며 내부 구현체 이름을 노출하지 않는가
- API DTO와 domain model 변환 경계가 일관적인가
- nullable/default 값이 "임시 성공"을 만들지 않고 명시적 실패/대기 상태를 표현하는가
- Compose에서 불필요한 recomposition을 만들 수 있는 inline allocation이 과도하지 않은가
- preview/dummy 데이터가 실제 API 연동 TODO와 명확히 분리되어 있는가
- `@Suppress`는 필요 최소 범위이며 이유가 설명 가능한가

## P2 Checklist

- 파일/함수 길이가 주변 코드보다 과도하게 커지지 않았는가
- 디자인 시스템의 색상/타이포/공통 컴포넌트를 재사용했는가
- 라벨, 버튼, 다이얼로그 문구가 실제 사용자 플로우와 맞는가
- 테스트가 ViewModel 상태 전이, SideEffect, API 실패 경로를 커버하는가

## Auth Signup Flow Focus

- 카카오 로그인 성공 후 토큰과 social info 저장 순서가 일관적인가
- 닉네임 중복 체크 실패/409/네트워크 실패가 사용자에게 구분 가능하게 표현되는가
- 고객 회원가입과 작가 회원가입의 API payload가 각 플로우의 State에서 추적 가능하게 조립되는가
- 프로필 이미지 업로드는 S3 object key와 화면 표시 URI를 혼동하지 않는가
- 완료 화면 navigation은 API 성공 이후 한 번만 발생하는가
- duplicate submit guard가 loading state와 함께 동작하는가

## Output Format

Findings first. Severity order: P0, P1, P2.

```text
P0
- file:line: 문제. 영향. 제안 수정.

P1
- file:line: 문제. 영향. 제안 수정.

P2
- file:line: 문제. 영향. 제안 수정.

Open Questions
- 확인 필요한 정책/API 질문.

Verification
- 실행한 명령과 결과.
- 실행하지 못한 검증과 이유.
```

이슈가 없으면 "발견된 blocking issue 없음"을 먼저 말하고, 남은 검증 공백을 적는다.
