# OpenCode Workflow Bridge

This folder keeps portable OpenCode workflow references for this workspace.
It intentionally excludes auth files, local databases, logs, and generated
`node_modules` content from `~/.config/opencode`.

## Imported Commands

- `command/plannotator-annotate.md`: open the Plannotator annotation UI.
- `command/plannotator-archive.md`: browse saved plan decisions.
- `command/plannotator-last.md`: annotate the last assistant message.
- `command/plannotator-review.md`: open interactive code review for current changes.

## OpenCode Runtime Reference

Observed global OpenCode plugins:

- `oh-my-openagent`
- `@plannotator/opencode@latest`
- `vendange`

Observed oh-my-openagent agent keys:

- `atlas`
- `explore`
- `librarian`
- `metis`
- `momus`
- `multimodal-looker`
- `oracle`
- `prometheus`
- `sisyphus`

Observed categories:

- `artistry`
- `code-review`
- `deep`
- `quick`
- `ultrabrain`
- `unspecified-high`
- `unspecified-low`
- `visual-engineering`
- `writing`

Observed MCP integrations:

- `figma`
- `notion`

## Project-Carried Context

- Auth API integration notes live in `.sisyphus/notepads/auth-api-integration/`.
- OMX planning context imported from the other Picplz workspace lives in `.omx/plans/`.
- Planning/spec/UI/review workflows live in `.opencode/workflows/`.
- Portable skill bodies live in `.opencode/skills/`.

## Primary Project Workflows

- Planning: `codebase-scan -> plan -> spec-interview -> spec -> test-spec`
- UI input: `ui-ask-question-interview`
- Picplz review: `picplz-code-review -> mvi-review -> semantic-naming-over-implementation`
- Verification: `verification-loop`
