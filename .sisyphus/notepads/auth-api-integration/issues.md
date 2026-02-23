## 2026-02-21 Known Issues

### BLOCKER: ktlint error on SignUpCommonViewModel.kt line 97
- Error: "A multiline expression should start on a new line"
- The `nicknameFieldErrors = it.nicknameFieldErrors.filterNot { ... }` assignment needs reformatting
- This blocks ALL commits (pre-commit hook)

### Open Backend Questions
1. Phone brand/model list API exists? (`GET /cameras` only returns cameras)
2. Customer signup: how to pass `socialCode` (reuse from login response?)
3. Photographer signup `cameras` field: includes phone devices too?
