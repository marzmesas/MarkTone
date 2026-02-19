# MarkTone

MarkTone is a JetBrains plugin that generates and applies Markdown preview CSS from the active IDE theme.

## Goal

Make Markdown preview look native to the currently active JetBrains theme without manual CSS maintenance.

## Core Value

- Auto-sync styles when theme changes.
- Keep markdown readable while matching IDE visual language.
- Remove manual custom CSS setup for users.

## Current Status

- Project scaffold created (Gradle Kotlin DSL + plugin sources + tests).
- Core CSS generation pipeline implemented.
- Theme-change sync pipeline implemented.
- Markdown preview integration implemented via `MarkdownSettings` custom stylesheet path API.

## Prerequisites

1. JDK 17+
2. Use the included Gradle wrapper (`./gradlew`)

## Local Setup

From project root:

1. (Optional) ensure shell uses Java 17:
   - `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`
   - `export PATH="$JAVA_HOME/bin:$PATH"`
2. Build checks:
   - `./gradlew tasks`
   - `./gradlew test`
3. Run plugin in sandbox IDE:
   - `./gradlew runIde`

## MVP Scope

- Read active theme colors/fonts.
- Generate deterministic CSS tokens and markdown rules.
- Apply CSS to Markdown preview automatically.
- React to theme changes and refresh preview styles.
- Expose a small settings panel with `Exact Match` and `Readable Docs` modes.

## Non-Goals (MVP)

- Full WYSIWYG markdown editor.
- Theme marketplace.
- Per-project design systems.

## Planning Docs

- `ARCHITECTURE.md`
- `IMPLEMENTATION_PLAN.md`
- `DISCOVERY_NOTES.md`
