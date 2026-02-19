package io.marktone.services

import com.intellij.openapi.components.Service
import io.marktone.model.ThemeSnapshot

@Service(Service.Level.APP)
class CssTokenGenerator {

    fun generate(snapshot: ThemeSnapshot): Map<String, String> {
        return linkedMapOf(
            "--mt-font-family" to "\"${snapshot.editorFontFamily}\", sans-serif",
            "--mt-font-size" to "${snapshot.editorFontSize}px",
            "--mt-bg" to "#${snapshot.backgroundHex}",
            "--mt-fg" to "#${snapshot.foregroundHex}",
            "--mt-muted" to "#${snapshot.lineCommentHex}",
            "--mt-link" to "#${snapshot.hyperlinkHex}",
            "--mt-border" to "#${snapshot.caretRowHex}",
            "--mt-selection" to "#${snapshot.selectionHex}",
            "--mt-code-bg" to "#${snapshot.defaultCodeBackgroundHex}",
            "--mt-code-fg" to "#${snapshot.foregroundHex}",
        )
    }
}
