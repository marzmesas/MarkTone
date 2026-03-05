# MarkTone

[![JetBrains Marketplace](https://img.shields.io/jetbrains/plugin/v/30278-marktone?label=Marketplace&logo=jetbrains)](https://plugins.jetbrains.com/plugin/30278-marktone)
[![GitHub Release](https://img.shields.io/github/v/release/marzmesas/MarkTone?logo=github)](https://github.com/marzmesas/MarkTone/releases)
[![Build](https://img.shields.io/github/actions/workflow/status/marzmesas/MarkTone/build.yml?logo=github)](https://github.com/marzmesas/MarkTone/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/marzmesas/MarkTone)](LICENSE)

Automatically generates and applies Markdown preview styles that match your current IDE theme.

## Features

- Extracts colors and fonts from your active IDE theme
- Generates a complete Markdown preview stylesheet with proper typography
- Auto-syncs styles when you switch themes — no manual CSS editing needed
- Two rendering profiles: **Minimal** (compact, sober palette) and **Expressive** (spacious, rich colors)
- **Live preview in settings** — see how your profile, font scaling, and theme choices look before applying
- **Preview theme selector** — try any installed editor color scheme in the settings preview without switching your IDE theme
- Adjustable font size scaling (90%–180%) for the Markdown preview
- Optional custom CSS overrides for further tweaking

## Installation

**From the Marketplace** (recommended):
Settings > Plugins > Marketplace > search "MarkTone" > Install
Or install directly from the [plugin page](https://plugins.jetbrains.com/plugin/30278-marktone).

**Manual install:**
Download the latest `.zip` from [Releases](https://github.com/marzmesas/MarkTone/releases), then go to Settings > Plugins > gear icon > Install Plugin from Disk.

## Usage

MarkTone reads your current editor colors, font family, and font size, then composes a Markdown-specific stylesheet using CSS custom properties. The result is applied directly to the Markdown preview panel.

- **Auto-sync** — styles update automatically when you switch themes.
- **Profiles** — choose between *Minimal* and *Expressive* under Settings > Tools > MarkTone, or via the **Tools > MarkTone** menu.
- **Custom CSS** — add your own overrides in the MarkTone settings panel.
- **Regenerate** — trigger a manual refresh from **Tools > MarkTone > Regenerate CSS**.

## Requirements

- JetBrains IDE **2023.3** or later
- Markdown plugin (bundled with most JetBrains IDEs)

## Building from source

Prerequisites: JDK 17+

```bash
# Run tests
./gradlew test

# Build the plugin
./gradlew build

# Launch a sandbox IDE with the plugin loaded
./gradlew runIde
```

## License

[Apache License 2.0](LICENSE)
