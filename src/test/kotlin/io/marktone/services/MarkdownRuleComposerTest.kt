package io.marktone.services

import io.marktone.settings.MarkToneProfile
import kotlin.test.Test
import kotlin.test.assertTrue

class MarkdownRuleComposerTest {

    @Test
    fun includesProfileVariablesAndCoreSelectors() {
        val tokens = linkedMapOf(
            "--mt-bg" to "#111111",
            "--mt-fg" to "#eeeeee",
            "--mt-font-family" to "\"JetBrains Mono\", sans-serif",
            "--mt-font-size" to "14px",
            "--mt-muted" to "#888888",
            "--mt-link" to "#00aaff",
            "--mt-border" to "#333333",
            "--mt-selection" to "#444444",
            "--mt-code-bg" to "#202020",
            "--mt-code-fg" to "#ce9178",
            "--mt-keyword" to "#569cd6",
            "--mt-string" to "#ce9178",
            "--mt-accent" to "#b5cea8",
        )

        val css = MarkdownRuleComposer().compose(tokens, MarkToneProfile.READABLE_DOCS)

        assertTrue(css.contains(":root"))
        assertTrue(css.contains("--mt-line-height"))
        assertTrue(css.contains("--mt-keyword"))
        assertTrue(css.contains("--mt-string"))
        assertTrue(css.contains("blockquote"))
        assertTrue(css.contains("pre"))
    }
}
