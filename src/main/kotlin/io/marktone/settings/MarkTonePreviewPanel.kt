package io.marktone.settings

import com.intellij.openapi.Disposable
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandlerAdapter
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel

class MarkTonePreviewPanel : Disposable {

    private val browser: JBCefBrowser = JBCefBrowser()
    private var pendingCss: String? = null
    private var loaded = false

    val component: JComponent = object : JPanel(BorderLayout()) {
        init { add(browser.component, BorderLayout.CENTER) }
        override fun getPreferredSize(): Dimension = Dimension(0, 0)
        override fun getMinimumSize(): Dimension = Dimension(0, 0)
    }

    init {
        browser.jbCefClient.addLoadHandler(object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(cefBrowser: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
                loaded = true
                pendingCss?.let { css ->
                    pendingCss = null
                    executeUpdateCss(css)
                }
            }
        }, browser.cefBrowser)

        val html = javaClass.getResource("/preview/sample.html")?.readText()
        if (html != null) {
            browser.loadHTML(html)
        }
    }

    fun updateCss(css: String) {
        if (loaded) {
            executeUpdateCss(css)
        } else {
            pendingCss = css
        }
    }

    private fun executeUpdateCss(css: String) {
        val escaped = css
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\n", "\\n")
            .replace("\r", "")
        browser.cefBrowser.executeJavaScript("updateCss('$escaped')", "about:blank", 0)
    }

    override fun dispose() {
        browser.dispose()
    }

    companion object {
        fun isAvailable(): Boolean = JBCefApp.isSupported()
    }
}
