# MarkTone Discovery Notes (Phase 0)

Date: 2026-02-18

## Goal

Validate integration surfaces before full implementation:

1. Theme/listener APIs for reliable regeneration triggers.
2. Markdown preview CSS injection path compatibility across IDE versions.

## Validation Completed

## A. Theme Change Detection

Implemented using IntelliJ message bus subscriptions:

- `LafManagerListener.TOPIC`
- `EditorColorsManager.TOPIC`

This covers global look-and-feel changes and editor color scheme changes.

## B. Markdown Preview Integration API

Validated from local JetBrains Markdown plugin classes (`markdown.jar`) in GoLand installation.

Confirmed API surface:

- `org.intellij.plugins.markdown.settings.MarkdownSettings.getInstance(project)`
- `setUseCustomStylesheetPath(boolean)`
- `setCustomStylesheetPath(String)`
- `setUseCustomStylesheetText(boolean)`
- `setCustomStylesheetText(String)`
- `update(Function1<MarkdownSettings, Unit>)`

MarkTone now applies generated CSS by updating `MarkdownSettings` for each open project.

## Current Known Limits

1. MarkTone currently applies one generated stylesheet path globally per open project.
2. It intentionally overwrites markdown custom stylesheet settings while enabled.
3. IDE-version-specific fallback adapters are not yet implemented.

## Next Validation Steps

1. Run matrix testing on at least two IDE families and two version lines.
2. Verify whether additional refresh hooks are needed in older IDE versions.
3. Add graceful fallback when Markdown plugin APIs differ or are unavailable.

## Decision

Phase 0 is complete enough to proceed with implementation phases, with compatibility matrix testing remaining as a release gate.
