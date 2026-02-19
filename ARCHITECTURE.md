# MarkTone Architecture

## 1. Context

MarkTone runs inside JetBrains IDEs and bridges three domains:

1. IDE theme state (colors, fonts, contrast).
2. CSS generation (tokens + markdown component rules).
3. Markdown preview integration (auto-apply and refresh).

## 2. High-Level Components

1. `ThemeSnapshotService`
- Responsibility: extract active theme values (foreground/background, editor font family/size, hyperlink colors, code block colors, borders, selection tones).
- Triggered on startup and on theme/look-and-feel changes.

2. `CssTokenGenerator`
- Responsibility: normalize theme snapshot into stable CSS variables.
- Example token set: `--mt-bg`, `--mt-fg`, `--mt-muted`, `--mt-link`, `--mt-code-bg`, `--mt-code-fg`, `--mt-border`.

3. `MarkdownRuleComposer`
- Responsibility: compose markdown-specific rules using tokens.
- Targets: body text, headings, links, lists, blockquote, code, pre, table, hr, task lists.
- Supports rendering profiles:
  - `Exact Match`
  - `Readable Docs`

4. `CssOutputService`
- Responsibility: persist generated CSS to plugin-managed path.
- Handles atomic writes and version stamping.

5. `PreviewSyncService`
- Responsibility: connect generated CSS to markdown preview settings and trigger refresh.
- Handles auto-sync lifecycle and safe fallback if markdown integration APIs differ by IDE version.

6. `MarkToneSettingsState`
- Responsibility: plugin settings persistence.
- Fields (MVP): enabled, profile, autoSync, customOverridesPath (optional).

7. `MarkToneSettingsUI`
- Responsibility: settings screen + manual "Regenerate now" action + diagnostics.

## 3. Runtime Flow

1. IDE starts.
2. `ThemeSnapshotService` captures current theme.
3. `CssTokenGenerator` builds token map.
4. `MarkdownRuleComposer` generates CSS string.
5. `CssOutputService` writes CSS file.
6. `PreviewSyncService` applies/refreshes markdown preview.
7. Theme changes trigger the same pipeline.

## 4. Data and File Strategy

- Generate CSS in plugin-managed app config directory.
- Keep output deterministic to avoid noisy diffs.
- Include metadata header:
  - generator version
  - generation timestamp
  - active profile

## 5. Compatibility Strategy

- Primary target: IntelliJ IDEA and WebStorm with Markdown plugin enabled.
- Add compatibility checks at startup:
  - markdown plugin availability
  - supported integration path for preview CSS
- If unsupported, show a clear in-IDE warning and fallback instructions.

## 6. Performance Considerations

- Debounce repeated theme events.
- Cache last snapshot hash; skip regeneration if unchanged.
- Keep CSS generation lightweight (<10ms typical path).

## 7. Risks and Unknowns

1. Markdown preview integration API may differ across IDEs/versions.
2. Some themes may not expose all desired semantic colors.
3. Custom font stacks may degrade cross-platform consistency.

## 8. Early Technical Decisions

1. Language: Kotlin.
2. Build: Gradle with IntelliJ Platform plugin.
3. Architecture style: small service graph + event-driven updates.
4. Testing split:
- pure unit tests for token/rule generation
- integration tests for settings and event flow
