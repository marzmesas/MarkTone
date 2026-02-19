package io.marktone.services

import io.marktone.model.ThemeSnapshot
import kotlin.test.Test
import kotlin.test.assertEquals

class CssTokenGeneratorTest {

    @Test
    fun generatesExpectedTokenKeys() {
        val snapshot = ThemeSnapshot(
            editorFontFamily = "JetBrains Mono",
            editorFontSize = 14,
            foregroundHex = "d4d4d4",
            backgroundHex = "1e1e1e",
            caretRowHex = "2a2d2e",
            selectionHex = "264f78",
            hyperlinkHex = "4ea1ff",
            lineCommentHex = "6a9955",
            defaultCodeBackgroundHex = "252526",
        )

        val tokens = CssTokenGenerator().generate(snapshot)

        assertEquals("#1e1e1e", tokens["--mt-bg"])
        assertEquals("#d4d4d4", tokens["--mt-fg"])
        assertEquals("14px", tokens["--mt-font-size"])
    }
}
