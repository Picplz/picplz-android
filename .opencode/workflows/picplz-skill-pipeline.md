# Picplz Skill Pipeline

Picplz 작업을 스킬 단위로 이어 붙인 표준 워크플로우.

## Default Flow

```text
0. context
   codebase-scan + rules + lsp

1. requirements
   spec-interview -> spec -> test-spec

2. planning
   plan or ulw-plan

3. implementation
   start-work or ulw-loop
   + programming/debugging/refactor as needed

4. UI adjustment
   ui-ask-question-interview
   + frontend-ui-ux when visual polish is the main task

5. review
   picplz-code-review
   -> mvi-review
   -> semantic-naming-over-implementation
   -> code-review
   -> review-work for significant implementation

6. verification
   verification-loop
   + lsp diagnostics
   + Gradle compile/test/ktlint/detekt
```

## Phase 0: Context Grounding

Use before asking the user broad questions.

- `rules`: load project rules and AGENTS scope.
- `codebase-scan`: map modules, patterns, similar implementations, and test infra.
- `lsp`: check definitions/references/diagnostics when changing Kotlin symbols.

Output:

- Current module and feature boundary.
- Existing MVI/API/navigation patterns to follow.
- Known risks and candidate files.

## Phase 1: Requirements Lock

Use when the task has ambiguous scope or user flow.

```text
spec-interview -> spec -> test-spec
```

- `spec-interview`: ask structured Socratic questions. Explore first, ask only unresolved intent/tradeoff questions.
- `spec`: crystallize the conversation into a Seed spec with goal, constraints, acceptance criteria, ontology, and ambiguity gate.
- `test-spec`: derive test cases from acceptance criteria and edge cases.

For simple bugs, this phase can collapse to a short plain-text confirmation plus `test-spec`-style checklist.

## Phase 2: Work Planning

Use after requirements are clear.

```text
plan
```

or, for large/ambiguous/multi-module work:

```text
ulw-plan
```

- `plan`: strategic plan with optional interview, consensus, and review modes.
- `ulw-plan`: LazyCodex/Prometheus style decision-complete plan. Use when implementers should not make judgment calls.

Plan must name:

- exact files/modules
- data/API contracts
- MVI state/intent/side-effect changes
- verification commands
- manual QA path

## Phase 3: Implementation

Use after a plan exists.

```text
start-work
```

or, for looser goal-loop execution:

```text
ulw-loop
```

Supporting LazyCodex skills:

- `programming`: Kotlin/typed-code discipline when editing production code.
- `debugging`: hypothesis-driven loop for runtime failures, flaky behavior, crashes, or wrong output.
- `refactor`: only when the request is explicitly cleanup/refactor.
- `remove-ai-slops`: after a messy branch needs behavior-preserving cleanup.
- `comment-checker`: respond to automatic inline feedback after edits.

Picplz implementation invariants:

- New/modified features keep the MVI 5-file structure.
- One-shot events use `Channel(Channel.BUFFERED)` + `receiveAsFlow()`.
- Feature modules do not depend directly on `core:data`.
- Source classes wrap API calls in `runCatching`.
- Strings live in resource XML, not hardcoded UI text.

## Phase 4: UI Application And Adjustment

Use after the core behavior is in place or when UI details are the main work.

```text
ui-ask-question-interview -> implementation -> visual/manual QA
```

- `ui-ask-question-interview`: locks block composition, grouping, alignment, spacing, size, radius, and typography through structured questions.
- `frontend-ui-ux`: use only when visual polish/design thinking is the main task.

For Picplz Compose UI:

- Ask structure before dp values.
- Confirm whether a change is screen-level, section-level, or reusable component-level.
- Preserve design-system colors, typography, and common components.
- Verify preview/manual path where possible.

## Phase 5: Review Stack

Run after meaningful implementation.

```text
picplz-code-review
-> mvi-review
-> semantic-naming-over-implementation
-> code-review
-> review-work
```

- `picplz-code-review`: Android/Picplz-specific architecture, auth/signup, API, navigation, and resource review.
- `mvi-review`: MVI structure, dead code, state/intent/side-effect consistency, Compose anti-patterns.
- `semantic-naming-over-implementation`: role/intent naming over implementation-exposing names.
- `code-review`: generic maintainability, security, performance, tests.
- `review-work`: LazyCodex 5-agent post-implementation review. Use for large or PR-ready changes.

Review order matters:

1. Picplz-specific correctness first.
2. MVI structure second.
3. Naming and generic code quality third.
4. Multi-agent QA/review for final confidence.

## Phase 6: Verification

Use before claiming done.

```text
verification-loop + lsp
```

Typical Picplz commands:

```bash
./gradlew :feature:auth:compileDebugKotlin
./gradlew :feature:mypage:compileDebugKotlin
./gradlew :app:compileDebugKotlin
./gradlew ktlintCheck
./gradlew detekt
```

Pick commands based on changed modules. Do not run the whole world by default if the change is narrow, but do run enough to cover touched module boundaries.

## Recommended Shortcuts

### New Feature

```text
codebase-scan
-> spec-interview
-> spec
-> test-spec
-> ulw-plan
-> start-work
-> ui-ask-question-interview if UI details are unresolved
-> picplz-code-review
-> code-review
-> verification-loop
```

### Clear Bug Fix

```text
codebase-scan
-> debugging
-> programming
-> mvi-review if MVI files changed
-> verification-loop
```

### UI Polish

```text
ui-ask-question-interview
-> frontend-ui-ux
-> programming
-> picplz-code-review
-> visual/manual QA
```

### PR Readiness

```text
picplz-code-review
-> mvi-review
-> semantic-naming-over-implementation
-> code-review
-> review-work
-> verification-loop
```

## Current Auth/Login Flow Recommendation

For the current `feat/186` signup/login workflow work:

```text
codebase-scan(auth + main + mypage)
-> spec-interview(actual signup/login user journey)
-> spec
-> test-spec
-> ulw-plan
-> start-work
-> ui-ask-question-interview(profile image / completion / main entry details)
-> picplz-code-review
-> mvi-review
-> code-review
-> verification-loop
```

The key review gates for this branch are:

- login token/social-info persistence
- customer vs photographer signup branching
- profile image upload/object key handling
- duplicate submit guard
- completion navigation fires once
- MyPage/Main screen behavior after signup/login
