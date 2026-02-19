# MarkTone Implementation Plan

## Phase 0: Discovery Spike (1-2 days)

Goal: eliminate platform unknowns before coding full plugin behavior.

Tasks:
1. Confirm Markdown preview CSS integration points for target IDE versions.
2. Confirm stable theme/listener APIs for theme changes.
3. Define minimum supported IDE build range.

Exit criteria:
- short technical note with validated integration approach.

## Phase 1: Plugin Skeleton (Day 1)

Tasks:
1. Initialize IntelliJ plugin project (Kotlin + Gradle).
2. Add plugin metadata and settings scaffold.
3. Register application/project services.

Deliverables:
- buildable plugin skeleton.

## Phase 2: Theme Snapshot + Tokens (Days 2-3)

Tasks:
1. Implement `ThemeSnapshotService`.
2. Implement `CssTokenGenerator` with deterministic output.
3. Add unit tests for token generation.

Deliverables:
- tested theme-to-token pipeline.

## Phase 3: Markdown CSS Composer (Days 3-4)

Tasks:
1. Implement `MarkdownRuleComposer`.
2. Add two profiles: `Exact Match`, `Readable Docs`.
3. Add unit tests for rule composition and regression snapshots.

Deliverables:
- stable CSS output with profile support.

## Phase 4: Output + Preview Sync (Days 4-6)

Tasks:
1. Implement `CssOutputService` with atomic writes.
2. Implement `PreviewSyncService` to apply CSS and refresh preview.
3. Add event handling for theme change updates.

Deliverables:
- working end-to-end auto-sync on theme changes.

## Phase 5: Settings UX + Diagnostics (Days 6-7)

Tasks:
1. Build settings UI.
2. Add manual "Regenerate now" action.
3. Add diagnostics panel (last generation, output path, last error).

Deliverables:
- user-visible controls and troubleshooting surface.

## Phase 6: QA + Packaging (Days 8-10)

Tasks:
1. Compatibility test matrix (at least 2 IDEs, 2 themes each).
2. Test fallback behavior when markdown integration is unavailable.
3. Package and sign plugin for local install/distribution.

Deliverables:
- release candidate build.

## MVP Backlog (Prioritized)

1. Theme snapshot extraction.
2. Token + markdown CSS generation.
3. Markdown preview auto-apply.
4. Theme change live refresh.
5. Settings toggle + profile mode.
6. Diagnostics and logs.

## Success Metrics

1. Time-to-setup under 1 minute.
2. Markdown preview updates within 1 second after theme change.
3. No manual CSS edits required in standard flow.
4. Fewer readability complaints vs baseline preview.

## Post-MVP Ideas

1. Per-language markdown tweaks (e.g., docs-heavy code blocks).
2. Export/shareable theme packs.
3. Live preview diff panel before applying generated CSS.

## Progress Checkpoint (2026-02-18)

Completed or substantially implemented:

1. Phase 0: local API validation for markdown settings integration (see `DISCOVERY_NOTES.md`).
2. Phase 1: plugin skeleton (Gradle files, plugin descriptor, source layout).
3. Phase 2: theme snapshot + token generation + unit tests.
4. Phase 3: markdown rule composer + profile support + unit tests.
5. Phase 4: output + preview sync wired through `MarkdownSettings` APIs.
6. Phase 5: initial settings UI and persistent state.

Remaining before release candidate:

1. Build/test execution after environment setup (JDK 17 + Gradle wrapper).
2. Compatibility matrix across IDE versions.
3. Fallback adapter behavior for markdown API differences.
4. Packaging and signing workflow.
