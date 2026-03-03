package io.marktone.settings

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MarkTonePreviewPanelTest {

    private val html: String by lazy {
        val url = javaClass.getResource("/preview/sample.html")
        assertNotNull(url, "sample.html must be on the classpath")
        url.readText()
    }

    @Test
    fun `sample html is loadable from classpath`() {
        assertTrue(html.isNotBlank())
    }

    @Test
    fun `sample html contains marktone-css style tag`() {
        assertContains(html, """id="marktone-css"""")
    }

    @Test
    fun `sample html contains updateCss function`() {
        assertContains(html, "function updateCss(css)")
    }

    @Test
    fun `sample html contains heading elements`() {
        assertContains(html, "<h1>")
        assertContains(html, "<h2>")
        assertContains(html, "<h3>")
    }

    @Test
    fun `sample html contains paragraph with inline elements`() {
        assertContains(html, "<strong>")
        assertContains(html, "<a href=")
        assertContains(html, "<code>")
    }

    @Test
    fun `sample html contains blockquote`() {
        assertContains(html, "<blockquote>")
    }

    @Test
    fun `sample html contains fenced code block with syntax spans`() {
        assertContains(html, "<pre><code>")
        assertContains(html, "hljs-keyword")
        assertContains(html, "hljs-string")
        assertContains(html, "hljs-number")
        assertContains(html, "hljs-comment")
        assertContains(html, "hljs-title function_")
    }

    @Test
    fun `sample html contains ordered list`() {
        assertContains(html, "<ol>")
        assertContains(html, "<li>")
    }

    @Test
    fun `sample html contains unordered list`() {
        assertContains(html, "<ul>")
    }

    @Test
    fun `sample html contains table`() {
        assertContains(html, "<table>")
        assertContains(html, "<th>")
        assertContains(html, "<td>")
    }

    @Test
    fun `sample html contains horizontal rule`() {
        assertContains(html, "<hr>")
    }

    @Test
    fun `sample html is valid document structure`() {
        assertContains(html, "<!DOCTYPE html>")
        assertContains(html, "<html")
        assertContains(html, "<head>")
        assertContains(html, "<body>")
        assertContains(html, "</html>")
    }

    @Test
    fun `sample html wraps content in preview container`() {
        assertContains(html, """class="preview-container"""")
    }

    @Test
    fun `sample html places marktone-css before layout overrides`() {
        val marktoneIdx = html.indexOf("""id="marktone-css"""")
        val containerIdx = html.indexOf("preview-container")
        assertTrue(marktoneIdx < containerIdx, "marktone-css style must precede layout overrides")
    }
}
