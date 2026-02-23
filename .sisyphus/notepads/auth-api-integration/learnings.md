## 2026-02-21 Initial Context

### Data Layer Patterns
- Simple flows: `Api → Source (runCatching) → Service (mapping) → ViewModel`
- Complex flows: `Api → Source → Service → Repository → UseCase → ViewModel`
- Phase 2 nickname check uses the simple pattern (no Repository/UseCase)

### Conventions
- SideEffect: `Channel<SideEffect>(Channel.BUFFERED)` + `receiveAsFlow()` (NOT SharedFlow)
- Pre-commit hooks: ktlint + detekt run on every commit
- Gitmoji commit convention
- `hiltViewModel()` instead of `viewModel()` for Hilt-injected ViewModels

### API Base URL
- Swagger: `http://43.203.62.97:8080/api/v1/swagger-ui/index.html`
- OpenAPI spec: `http://43.203.62.97:8080/api/v1/v3/api-docs`

### Known Issues
- ktlint strict about multiline expressions starting on new lines
- 4 reservation composable files needed `@file:Suppress("UnusedPrivateMember")` for detekt
