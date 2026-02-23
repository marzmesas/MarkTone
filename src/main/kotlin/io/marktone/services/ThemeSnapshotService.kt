package io.marktone.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.ui.ColorUtil
import io.marktone.model.ThemeSnapshot
import java.awt.Color
import javax.swing.UIManager

@Service(Service.Level.APP)
class ThemeSnapshotService {

    fun capture(): ThemeSnapshot {
        val scheme = EditorColorsManager.getInstance().globalScheme
        val bg = UIManager.getColor("Panel.background") ?: scheme.defaultBackground
        val fg = scheme.defaultForeground

        return ThemeSnapshot(
            editorFontFamily = scheme.editorFontName,
            editorFontSize = scheme.editorFontSize,
            foregroundHex = toHex(fg),
            backgroundHex = toHex(bg),
            caretRowHex = toHex(scheme.getColor(EditorColors.CARET_ROW_COLOR) ?: bg),
            selectionHex = toHex(scheme.getColor(EditorColors.SELECTION_BACKGROUND_COLOR) ?: bg),
            hyperlinkHex = toHex(
                scheme.getAttributes(EditorColors.REFERENCE_HYPERLINK_COLOR)?.foregroundColor ?: fg,
            ),
            lineCommentHex = toHex(
                scheme.getAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT)?.foregroundColor ?: fg,
            ),
            stringHex = toHex(
                scheme.getAttributes(DefaultLanguageHighlighterColors.STRING)?.foregroundColor ?: fg,
            ),
            keywordHex = toHex(
                scheme.getAttributes(DefaultLanguageHighlighterColors.KEYWORD)?.foregroundColor ?: fg,
            ),
            numberHex = toHex(
                scheme.getAttributes(DefaultLanguageHighlighterColors.NUMBER)?.foregroundColor ?: fg,
            ),
            borderHex = toHex(blend(bg, fg, 0.18)),
        )
    }

    private fun blend(c1: Color, c2: Color, ratio: Double): Color {
        return Color(
            (c1.red + (c2.red - c1.red) * ratio).toInt(),
            (c1.green + (c2.green - c1.green) * ratio).toInt(),
            (c1.blue + (c2.blue - c1.blue) * ratio).toInt(),
        )
    }

    private fun toHex(color: Color): String = ColorUtil.toHex(color, false)
}
