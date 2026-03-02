package io.marktone.services

import io.marktone.model.ThemeSnapshot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CssTokenGeneratorTest {

    private val darkSnapshot = ThemeSnapshot(
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

    private val lightSnapshot = ThemeSnapshot(
        editorFontFamily = "Fira Code",
        editorFontSize = 13,
        foregroundHex = "2b2b2b",
        backgroundHex = "f5f5f5",
        caretRowHex = "fffae3",
        selectionHex = "a6d2ff",
        hyperlinkHex = "006dcc",
        lineCommentHex = "808080",
        stringHex = "067d17",
        keywordHex = "0033b3",
        numberHex = "1750eb",
        functionHex = "7a7a43",
        borderHex = "d0d0d0",
    )

    private val generator = CssTokenGenerator()

    // --- Token completeness ---

    @Test
    fun generatesExactly14Tokens() {
        val tokens = generator.generate(darkSnapshot)
        assertEquals(14, tokens.size)
    }

    @Test
    fun allExpectedKeysArePresent() {
        val tokens = generator.generate(darkSnapshot)
        val expectedKeys = listOf(
            "--mt-font-family", "--mt-font-size", "--mt-bg", "--mt-fg",
            "--mt-muted", "--mt-link", "--mt-border", "--mt-selection",
            "--mt-code-bg", "--mt-code-fg", "--mt-keyword", "--mt-string",
            "--mt-accent", "--mt-function",
        )
        expectedKeys.forEach { key ->
            assertTrue(tokens.containsKey(key), "Missing token: $key")
        }
    }

    @Test
    fun tokenOrderIsStable() {
        val tokens = generator.generate(darkSnapshot)
        val keys = tokens.keys.toList()
        assertEquals("--mt-font-family", keys.first())
        assertEquals("--mt-function", keys.last())
    }

    // --- Dark theme token values ---

    @Test
    fun darkThemeColorTokenValues() {
        val tokens = generator.generate(darkSnapshot)

        assertEquals("#1e1e1e", tokens["--mt-bg"])
        assertEquals("#d4d4d4", tokens["--mt-fg"])
        assertEquals("#6a9955", tokens["--mt-muted"])
        assertEquals("#4ea1ff", tokens["--mt-link"])
        assertEquals("#3a3a3a", tokens["--mt-border"])
        assertEquals("#264f78", tokens["--mt-selection"])
        assertEquals("#2a2d2e", tokens["--mt-code-bg"])
        assertEquals("#ce9178", tokens["--mt-code-fg"])
        assertEquals("#569cd6", tokens["--mt-keyword"])
        assertEquals("#ce9178", tokens["--mt-string"])
        assertEquals("#b5cea8", tokens["--mt-accent"])
        assertEquals("#dcdcaa", tokens["--mt-function"])
    }

    @Test
    fun darkThemeFontTokens() {
        val tokens = generator.generate(darkSnapshot)

        assertEquals("\"JetBrains Mono\", sans-serif", tokens["--mt-font-family"])
        assertEquals("14px", tokens["--mt-font-size"])
    }

    // --- Light theme token values ---

    @Test
    fun lightThemeColorTokenValues() {
        val tokens = generator.generate(lightSnapshot)

        assertEquals("#f5f5f5", tokens["--mt-bg"])
        assertEquals("#2b2b2b", tokens["--mt-fg"])
        assertEquals("#808080", tokens["--mt-muted"])
        assertEquals("#006dcc", tokens["--mt-link"])
        assertEquals("#d0d0d0", tokens["--mt-border"])
        assertEquals("#a6d2ff", tokens["--mt-selection"])
        assertEquals("#fffae3", tokens["--mt-code-bg"])
        assertEquals("#067d17", tokens["--mt-code-fg"])
        assertEquals("#0033b3", tokens["--mt-keyword"])
        assertEquals("#067d17", tokens["--mt-string"])
        assertEquals("#1750eb", tokens["--mt-accent"])
        assertEquals("#7a7a43", tokens["--mt-function"])
    }

    @Test
    fun lightThemeFontTokens() {
        val tokens = generator.generate(lightSnapshot)

        assertEquals("\"Fira Code\", sans-serif", tokens["--mt-font-family"])
        assertEquals("13px", tokens["--mt-font-size"])
    }

    // --- Font family edge cases ---

    @Test
    fun fontFamilyWithSpacesIsQuoted() {
        val tokens = generator.generate(darkSnapshot)
        assertTrue(tokens["--mt-font-family"]!!.startsWith("\""))
    }

    @Test
    fun singleWordFontFamilyIsStillQuoted() {
        val snapshot = darkSnapshot.copy(editorFontFamily = "Menlo")
        val tokens = generator.generate(snapshot)
        assertEquals("\"Menlo\", sans-serif", tokens["--mt-font-family"])
    }

    // --- Font size scaling ---

    @Test
    fun defaultScalingProducesOriginalFontSize() {
        val tokens = generator.generate(darkSnapshot)
        assertEquals("14px", tokens["--mt-font-size"])
    }

    @Test
    fun explicitScaling100MatchesDefault() {
        val tokens = generator.generate(darkSnapshot, 100)
        assertEquals("14px", tokens["--mt-font-size"])
    }

    @Test
    fun scalingAt120ProducesLargerFontSize() {
        val tokens = generator.generate(darkSnapshot, 120)
        assertEquals("16px", tokens["--mt-font-size"])
    }

    @Test
    fun scalingAt90ProducesSmallerFontSize() {
        val tokens = generator.generate(darkSnapshot, 90)
        assertEquals("12px", tokens["--mt-font-size"])
    }

    @Test
    fun scalingAt130() {
        val tokens = generator.generate(darkSnapshot, 130)
        assertEquals("18px", tokens["--mt-font-size"])
    }

    @Test
    fun scalingAt180ProducesMaximumFontSize() {
        val tokens = generator.generate(darkSnapshot, 180)
        assertEquals("25px", tokens["--mt-font-size"])
    }

    @Test
    fun scalingAt110ProducesIntermediateFontSize() {
        val tokens = generator.generate(darkSnapshot, 110)
        assertEquals("15px", tokens["--mt-font-size"])
    }

    @Test
    fun scalingAppliesToDifferentBaseFontSizes() {
        val snapshot = darkSnapshot.copy(editorFontSize = 20)
        assertEquals("24px", generator.generate(snapshot, 120)["--mt-font-size"])
        assertEquals("18px", generator.generate(snapshot, 90)["--mt-font-size"])
        assertEquals("26px", generator.generate(snapshot, 130)["--mt-font-size"])
    }

    @Test
    fun scalingWithSmallBaseFontSize() {
        val snapshot = darkSnapshot.copy(editorFontSize = 10)
        assertEquals("12px", generator.generate(snapshot, 120)["--mt-font-size"])
        assertEquals("9px", generator.generate(snapshot, 90)["--mt-font-size"])
    }

    @Test
    fun scalingDoesNotAffectOtherTokens() {
        val defaultTokens = generator.generate(darkSnapshot)
        val scaledTokens = generator.generate(darkSnapshot, 130)

        defaultTokens.forEach { (key, value) ->
            if (key != "--mt-font-size") {
                assertEquals(value, scaledTokens[key], "Token $key should not change with scaling")
            }
        }
    }

    // --- Hex formatting ---

    @Test
    fun colorTokensArePrefixedWithHash() {
        val tokens = generator.generate(darkSnapshot)
        val colorKeys = listOf(
            "--mt-bg", "--mt-fg", "--mt-muted", "--mt-link", "--mt-border",
            "--mt-selection", "--mt-code-bg", "--mt-code-fg", "--mt-keyword",
            "--mt-string", "--mt-accent", "--mt-function",
        )
        colorKeys.forEach { key ->
            assertTrue(tokens[key]!!.startsWith("#"), "$key should start with #")
        }
    }

    @Test
    fun fontSizeTokenEndsWithPx() {
        val tokens = generator.generate(darkSnapshot)
        assertTrue(tokens["--mt-font-size"]!!.endsWith("px"))
    }

    @Test
    fun fontFamilyTokenEndsWithSansSerif() {
        val tokens = generator.generate(darkSnapshot)
        assertTrue(tokens["--mt-font-family"]!!.endsWith(", sans-serif"))
    }
}
