---
name: mvi-review
description: MVI 패턴 준수, 일관성, 데드코드, 단일책임, 관심사 분리를 검사하는 Android 코드 리뷰. GPT-5.4가 수행.
---

# mvi-review — MVI 아키텍처 코드 리뷰

Android MVI 코드베이스의 구조적 품질을 검사한다.

## 검사 항목
- MVI 5파일 구조 준수 (Screen/ViewModel/State/Intent/SideEffect)
- 데드코드 (미사용 Intent, State 필드, 파라미터)
- 단일 책임 원칙 위반
- 관심사 분리 (UI/Data/Domain 경계)
- 일관성 (DRY, 매직넘버, 네이밍 컨벤션)
- Compose 안티패턴 (불필요 recomposition, remember 누락)

## 사용법

```
/mvi-review                    # git diff 기준 변경 파일 리뷰
/mvi-review QuickShoot         # 특정 기능 리뷰
```

## 실행 방법

subagent로 `mvi-reviewer` 에이전트(GPT-5.4)에 위임한다.

1. 리뷰 대상 파일을 결정한다:
   - 인자가 없으면: `git diff --name-only develop` 또는 `git diff --cached --name-only`로 변경 파일 목록
   - 인자가 있으면: 해당 키워드가 포함된 파일들

2. subagent로 위임:
   ```
   subagent single:
     agent: mvi-reviewer
     task: |
       아래 파일들을 MVI 아키텍처 관점에서 리뷰해주세요.

       리뷰 대상 파일:
       {파일 목록}

       프로젝트 컨텍스트:
       - Android + Jetpack Compose + Hilt + MVI
       - Channel(BUFFERED) for SideEffect
       - Handler 패턴으로 Intent 처리 위임
       - 프로젝트 규칙: {AGENTS.md 핵심 규칙}

       각 파일을 read 도구로 전체 읽고 검사해주세요.
   ```

3. 리뷰 결과를 사용자에게 전달한다.
