package io.marktone.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame

class ThemeSnapshotTest {

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

    @Test
    fun copyPreservesAllFields() {
        val copy = darkSnapshot.copy()
        assertEquals(darkSnapshot, copy)
        assertNotSame(darkSnapshot, copy)
    }

    @Test
    fun copyWithSingleFieldChange() {
        val modified = darkSnapshot.copy(editorFontSize = 18)
        assertEquals(18, modified.editorFontSize)
        assertEquals(darkSnapshot.editorFontFamily, modified.editorFontFamily)
        assertEquals(darkSnapshot.backgroundHex, modified.backgroundHex)
    }

    @Test
    fun differentSnapshotsAreNotEqual() {
        val other = darkSnapshot.copy(backgroundHex = "ffffff")
        assertNotEquals(darkSnapshot, other)
    }

    @Test
    fun snapshotsWithSameValuesAreEqual() {
        val duplicate = ThemeSnapshot(
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
        assertEquals(darkSnapshot, duplicate)
    }
}
