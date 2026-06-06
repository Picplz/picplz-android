---
name: test-spec
description: "Seed 스펙(ACCEPTANCE_CRITERIA + ONTOLOGY)에서 테스트 케이스를 도출. /spec 다음 단계."
---

# test-spec — 스펙 기반 테스트 케이스 도출

`/spec`의 출력(Acceptance Criteria + Ontology)을 입력으로 받아, 구현 전에 검증 기준이 되는 테스트 케이스를 생성한다.

## 사용법

```
/test-spec
```

> `/spec` 실행 후 사용. Seed 스펙이 없으면 `/spec-interview` → `/spec` 먼저 실행.

---

## 흐름

```
spec-interview → spec → test-spec → 구현 (ralph)
                         ↑ 여기
```

---

## 입력 → 출력

| 입력 (from `/spec`) | 출력 |
|---|---|
| ACCEPTANCE_CRITERIA | Happy path 테스트 케이스 |
| ONTOLOGY_FIELDS | Schema 검증 + 경계값 테스트 |
| CONSTRAINTS | 제약 조건 검증 테스트 |
| CONTRARIAN_DETAILS | 선택의 전제 조건 검증 테스트 |

---

## 테스트 패턴 매트릭스

스펙의 내용에 따라 적용할 패턴을 자동 매칭:

| 패턴 | 대상 | 검증 내용 | 트리거 |
|------|------|-----------|--------|
| **A: Schema 검증** | Ontology fields | 필수/선택 필드, 유효/무효 값 | ONTOLOGY_FIELDS 존재 |
| **B: Endpoint 구조** | API 경로 | 경로 정확성, 메서드 매칭 | CONSTRAINTS에 API 언급 |
| **C: Permission 매트릭스** | 권한 설정 | 역할→액션 매핑 | CONSTRAINTS에 권한 언급 |
| **D: 비즈니스 로직** | 순수 함수 | 입출력, happy/edge/error path | ACCEPTANCE_CRITERIA |
| **E: 경계값 분석** | 필드 타입 | 빈 문자열, 오버플로, 타입 경계 | ONTOLOGY_FIELDS 존재 |
| **F: 상태 전이** | 상태 머신 | 유효 전이 통과, 무효 전이 거부 | ONTOLOGY에 status/state 필드 |
| **G: 에러 경로** | API 핸들러 | 400/401/404/500 응답 | CONSTRAINTS에 API 언급 |

---

## 실행 방법

### Step 1: 스펙에서 테스트 요구사항 추출

Seed 스펙의 각 항목을 테스트 대상으로 변환:

```
ACCEPTANCE_CRITERIA: "할일 생성 가능 | 할일 목록 조회 | 할일 삭제 가능"
↓
- TC-001: 할일 생성 — 유효한 입력 → 성공
- TC-002: 할일 생성 — 빈 제목 → 실패
- TC-003: 할일 목록 조회 — 0건 → 빈 배열
- TC-004: 할일 목록 조회 — N건 → N개 반환
- TC-005: 할일 삭제 — 존재하는 ID → 성공
- TC-006: 할일 삭제 — 없는 ID → 404
```

### Step 2: Ontology에서 Schema 테스트 도출

```
ONTOLOGY_FIELDS: id:string:UUID | title:string:할일 제목 | done:boolean:완료 여부 | createdAt:string:생성 시각
↓
Schema 검증 (Pattern A):
- TC-S01: 유효한 전체 필드 → 파싱 성공
- TC-S02: id 누락 → 실패
- TC-S03: title 누락 → 실패
- TC-S04: done이 string → 실패

경계값 (Pattern E):
- TC-E01: title 빈 문자열 → 실패
- TC-E02: title 50000자 → 동작 확인
- TC-E03: id 잘못된 UUID 형식 → 실패
```

### Step 3: 엣지 케이스 자동 도출

필드 타입별 경계값 규칙:

| 필드 타입 | 테스트 경계값 |
|-----------|--------------|
| `string` | `""`, `" "`, `"a".repeat(50000)`, unicode, SQL injection |
| `string(min:N)` | `"a".repeat(N-1)` (미달), `"a".repeat(N)` (통과) |
| `number` | `0`, `-1`, `NaN`, `Infinity`, `MAX_SAFE_INTEGER` |
| `boolean` | `true`, `false`, `"true"` (string), `0`, `1` |
| `array` | `[]`, `[single]`, `Array(1000)` |
| `datetime` | epoch, far-future, invalid format |
| `uuid` | `""`, `"not-uuid"`, partial |
| nullable | `null`, `undefined`, 실제값 교차 |

### Step 4: Contrarian 전제 조건 테스트

`/spec`의 contrarian_details에서 선택의 전제가 되는 조건을 테스트로 변환:

```
contrarian_details:
  - decision: "localStorage 사용"
    alternative: "IndexedDB"
    defensibility: 0.8
    reason: "5MB 미만, 관계형 불필요"
↓
- TC-C01: 데이터 총량이 5MB를 넘지 않는지 확인
- TC-C02: 관계형 쿼리가 필요한 로직이 없는지 확인
```

**선택의 근거가 깨지면 아키텍처 재검토가 필요**하므로, 전제 조건 자체를 테스트한다.

---

## 출력 형식

```yaml
# === Test Spec ===
test_cases:
  # --- From ACCEPTANCE_CRITERIA ---
  - id: TC-001
    title: "할일 생성 — 유효한 입력"
    pattern: D
    given: "유효한 title과 done=false"
    when: "createTodo 호출"
    then: "새 할일 반환, id 포함"
    priority: must

  - id: TC-002
    title: "할일 생성 — 빈 제목"
    pattern: D
    given: "title이 빈 문자열"
    when: "createTodo 호출"
    then: "validation error"
    priority: must

  # --- From ONTOLOGY (Schema) ---
  - id: TC-S01
    title: "TodoSchema — 유효 데이터 파싱"
    pattern: A
    given: "모든 필수 필드가 올바른 타입"
    when: "schema.parse(data)"
    then: "파싱 성공"
    priority: must

  # --- From ONTOLOGY (Boundary) ---
  - id: TC-E01
    title: "title 빈 문자열 거부"
    pattern: E
    given: "title: ''"
    when: "schema.parse(data)"
    then: "ZodError"
    priority: should

  # --- From CONTRARIAN (Precondition) ---
  - id: TC-C01
    title: "데이터 총량 5MB 미만 전제"
    pattern: contrarian
    given: "최대 사용 시나리오"
    when: "전체 데이터 크기 측정"
    then: "< 5MB"
    priority: could

# === Summary ===
total: 15
by_pattern: { A: 3, D: 5, E: 4, contrarian: 2, G: 1 }
by_priority: { must: 8, should: 5, could: 2 }
```

### Priority 기준

| Priority | 의미 |
|----------|------|
| **must** | Acceptance Criteria에서 직접 도출. 이것 없이 완료 불가 |
| **should** | 경계값/엣지 케이스. 품질을 위해 필요 |
| **could** | Contrarian 전제 조건. 아키텍처 검증용 |

---

## Step 5: vine .cases.json 생성

TC YAML을 vine이 실행할 수 있는 `.cases.json` 파일로 변환한다.
이 파일은 qa-harness Stage 1에서 `vine run --priority P0`으로 결정적 검증에 사용된다.

### Priority 매핑

| test-spec | vine | 의미 |
|---|---|---|
| `must` | `P0` | 완료 판정 게이트. vine run --priority P0 대상 |
| `should` | `P1` | 품질 게이트. vine run 전체 실행 대상 |
| `could` | `P2` | 아키텍처 검증. 선택적 실행 |

### Pattern → verify 슬롯 매핑

| test-spec pattern | vine verify 타입 | 설명 |
|---|---|---|
| A (Schema) | `verify.schema` | `{ target, input, expect: "pass"/"reject" }` |
| B (Endpoint) | `verify.api` | `{ method, path, expect_status }` |
| C (Permission) | `verify.permission` | `{ action, role, expect: "allow"/"deny" }` |
| D (비즈니스 로직) | `verify.behavior` | `{ module, function, args, expect_output }` |
| E (경계값) | `verify.schema` | schema와 동일, 경계 입력값 사용 |
| F (상태 전이) | `verify.state` | `{ entity, from, to, expect: "allow"/"reject" }` |
| G (에러 경로) | `verify.api` | `{ method, path, expect_status: 400/404/500 }` |
| contrarian | (verify 없음) | Given/When/Then만 기록, 수동 검증 |

### 출력 위치

```
{domain}.cases.json  — 프로젝트 루트 또는 specs/ 하위
```

### 예시

TC YAML:
```yaml
- id: TC-S01
  title: "TodoSchema — 유효 데이터 파싱"
  pattern: A
  given: "모든 필수 필드가 올바른 타입"
  when: "schema.parse(data)"
  then: "파싱 성공"
  priority: must
```

→ `.cases.json`:
```json
{
  "version": "1.0",
  "spec_ref": "Seed-Todo-Gen3",
  "domain": "todo",
  "cases": [
    {
      "id": "TC-S01",
      "title": "TodoSchema — 유효 데이터 파싱",
      "spec_ref": "Seed-Todo-Gen3",
      "priority": "P0",
      "depends_on": [],
      "tags": ["schema"],
      "requires_auth": false,
      "given": "모든 필수 필드가 올바른 타입",
      "when": "schema.parse(data)",
      "then": "파싱 성공",
      "verify": {
        "schema": {
          "target": "src/entities/todo/models/todo.schema.ts",
          "input": { "id": "uuid-1", "title": "할일", "done": false },
          "expect": "pass"
        }
      },
      "steps": []
    }
  ]
}
```

### verify 슬롯이 없는 케이스

`contrarian` 패턴이나 verify를 작성할 수 없는 케이스는 `verify` 없이 생성.
vine은 이를 `no-verify`로 처리하고 Stage 2B에서 LLM rubric으로 활용한다.

---

## 다음 단계

테스트 스펙 확정 후:
- `.cases.json` 파일을 프로젝트에 커밋
- `ralph`로 구현 시 `vine run --priority P0`으로 결정적 검증
- 각 TC의 `pattern`에 맞는 vitest 테스트 코드도 병행 작성
- 모든 `must` TC (P0) 통과 = Ralph 완료 선언 가능
- qa-harness Stage 1에서 `vine run` 자동 실행
