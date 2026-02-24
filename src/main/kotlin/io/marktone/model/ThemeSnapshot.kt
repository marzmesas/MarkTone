package io.marktone.model

data class ThemeSnapshot(
    val editorFontFamily: String,
    val editorFontSize: Int,
    val foregroundHex: String,
    val backgroundHex: String,
    val caretRowHex: String,
    val selectionHex: String,
    val hyperlinkHex: String,
    val lineCommentHex: String,
    val stringHex: String,
    val keywordHex: String,
    val numberHex: String,
    val functionHex: String,
    val borderHex: String,
)
