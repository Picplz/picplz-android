---
name: verification-loop
description: Comprehensive verification system for code changes - build, types, lint, tests, security.
---

# Verification Loop Skill

코드 변경 후 종합 검증 시스템.

## When to Use

- 기능 완료 후
- PR 생성 전
- 리팩토링 후

## Verification Phases

### Phase 1: Build
```bash
./gradlew assembleDebug 2>&1 | tail -20
# OR
npm run build 2>&1 | tail -20
```

### Phase 2: Type Check
```bash
# Kotlin/Android
./gradlew compileDebugKotlin 2>&1 | tail -30

# TypeScript
npx tsc --noEmit 2>&1 | head -30
```

### Phase 3: Lint
```bash
# Android
./gradlew ktlintCheck detekt 2>&1 | tail -30

# JS/TS
npm run lint 2>&1 | head -30
```

### Phase 4: Tests
```bash
./gradlew test 2>&1 | tail -50
```

### Phase 5: Security Scan
```bash
# Check for secrets
grep -rn "api_key\|secret\|password" --include="*.kt" . 2>/dev/null | head -10

# Check for debug code
grep -rn "println\|Log.d" --include="*.kt" src/ 2>/dev/null | head -10
```

### Phase 6: Diff Review
```bash
git diff --stat
git diff HEAD~1 --name-only
```

## Output Format

```
VERIFICATION REPORT
==================

Build:     [PASS/FAIL]
Types:     [PASS/FAIL] (X errors)
Lint:      [PASS/FAIL] (X warnings)
Tests:     [PASS/FAIL] (X/Y passed)
Security:  [PASS/FAIL] (X issues)
Diff:      [X files changed]

Overall:   [READY/NOT READY] for PR

Issues to Fix:
1. ...
```

## Usage

세션 중 검증 요청:
```
/verify 또는 "검증 돌려줘"
```
