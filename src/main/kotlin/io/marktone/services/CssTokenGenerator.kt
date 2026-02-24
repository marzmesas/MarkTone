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
            "--mt-border" to "#${snapshot.borderHex}",
            "--mt-selection" to "#${snapshot.selectionHex}",
            "--mt-code-bg" to "#${snapshot.caretRowHex}",
            "--mt-code-fg" to "#${snapshot.stringHex}",
            "--mt-keyword" to "#${snapshot.keywordHex}",
            "--mt-string" to "#${snapshot.stringHex}",
            "--mt-accent" to "#${snapshot.numberHex}",
            "--mt-function" to "#${snapshot.functionHex}",
        )
    }
}
