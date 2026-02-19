package io.marktone.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.ui.ColorUtil
import io.marktone.model.ThemeSnapshot

@Service(Service.Level.APP)
class ThemeSnapshotService {

    fun capture(): ThemeSnapshot {
        val scheme = EditorColorsManager.getInstance().globalScheme

        return ThemeSnapshot(
            editorFontFamily = scheme.editorFontName,
            editorFontSize = scheme.editorFontSize,
            foregroundHex = toHex(scheme.defaultForeground),
            backgroundHex = toHex(scheme.defaultBackground),
            caretRowHex = toHex(scheme.getColor(EditorColors.CARET_ROW_COLOR) ?: scheme.defaultBackground),
            selectionHex = toHex(scheme.getColor(EditorColors.SELECTION_BACKGROUND_COLOR) ?: scheme.defaultBackground),
            hyperlinkHex = toHex(
                scheme.getAttributes(EditorColors.REFERENCE_HYPERLINK_COLOR)?.foregroundColor
                    ?: scheme.defaultForeground,
            ),
            lineCommentHex = toHex(
                scheme.getAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT)?.foregroundColor
                    ?: scheme.defaultForeground,
            ),
            defaultCodeBackgroundHex = toHex(scheme.defaultBackground),
        )
    }

    private fun toHex(color: java.awt.Color): String = ColorUtil.toHex(color, false)
}
