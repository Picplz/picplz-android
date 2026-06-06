# Picplz OpenCode Workflows

This workspace carries the portable planning, spec, UI interview, and review
skills that were used from the OpenCode side.

## Feature Planning

Use when the request is broad, cross-module, or still vague.

```text
codebase-scan -> plan -> spec-interview -> spec -> test-spec -> implementation -> verification-loop
```

- `codebase-scan`: gather existing stack, files, conventions, and risks.
- `plan`: produce an implementation plan in `.omx/plans/`.
- `spec-interview`: ask structured requirement questions before coding.
- `spec`: crystallize the interview into a Seed spec.
- `test-spec`: derive test cases from acceptance criteria and ontology.
- `verification-loop`: run build, lint, tests, and focused QA.

## UI Adjustment Workflow

Use when the user says the UI feels off, wants Figma matching, asks for exact
spacing/sizing, or wants `askUserQuestion` style choices.

```text
ui-ask-question-interview -> implement selected UI changes -> visual/manual QA -> repeat if user says it still feels wrong
```

The interview locks structure first, then values:

- block composition
- grouping and alignment
- spacing, size, radius, typography
- visual audit after implementation

## Picplz Review Workflow

Use after feature work or before PR.

```text
picplz-code-review -> mvi-review -> semantic-naming-over-implementation -> verification-loop
```

- `picplz-code-review`: Picplz Android-specific checklist.
- `mvi-review`: MVI structure, dead code, Compose concerns.
- `semantic-naming-over-implementation`: role/intent naming review.
- `code-review`: generic security, maintainability, performance review.

## Imported Skill Index

- `plan`
- `codebase-scan`
- `spec-interview`
- `spec`
- `test-spec`
- `ui-ask-question-interview`
- `picplz-code-review`
- `mvi-review`
- `code-review`
- `semantic-naming-over-implementation`
- `verification-loop`

## Full Pipeline

See `picplz-skill-pipeline.md` for the combined OpenCode + LazyCodex + Picplz workflow.

## Tool Mapping Notes

OpenCode skills mention `askUserQuestion`. In Codex, use the available
structured user-input tool when the current mode exposes it; otherwise ask one
concise plain-text question and continue with conservative assumptions.
