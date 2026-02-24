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
            stringHex = "ce9178",
            keywordHex = "569cd6",
            numberHex = "b5cea8",
            functionHex = "dcdcaa",
            borderHex = "3a3a3a",
        )

        val tokens = CssTokenGenerator().generate(snapshot)

        assertEquals("#1e1e1e", tokens["--mt-bg"])
        assertEquals("#d4d4d4", tokens["--mt-fg"])
        assertEquals("14px", tokens["--mt-font-size"])
        assertEquals("#2a2d2e", tokens["--mt-code-bg"])
        assertEquals("#ce9178", tokens["--mt-code-fg"])
        assertEquals("#569cd6", tokens["--mt-keyword"])
        assertEquals("#ce9178", tokens["--mt-string"])
        assertEquals("#b5cea8", tokens["--mt-accent"])
        assertEquals("#dcdcaa", tokens["--mt-function"])
        assertEquals("#3a3a3a", tokens["--mt-border"])
    }
}
