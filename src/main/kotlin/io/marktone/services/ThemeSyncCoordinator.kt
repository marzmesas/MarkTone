package io.marktone.services

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.colors.EditorColorsListener
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.util.Disposer
import io.marktone.settings.MarkToneSettingsState
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean

@Service(Service.Level.APP)
class ThemeSyncCoordinator {

    private val logger = thisLogger()
    private val inFlight = AtomicBoolean(false)

    init {
        val connection = ApplicationManager.getApplication().messageBus.connect()
        connection.subscribe(LafManagerListener.TOPIC, LafManagerListener { scheduleSync("laf-change") })
        connection.subscribe(EditorColorsManager.TOPIC, EditorColorsListener { _: EditorColorsScheme? -> scheduleSync("editor-scheme-change") })

        Disposer.register(ApplicationManager.getApplication(), connection)
    }

    fun regenerateAndApply(trigger: String) {
        val settings = service<MarkToneSettingsState>().current()
        if (!settings.enabled) {
            return
        }

        if (!settings.autoSync && trigger != "startup" && trigger != "settings-change" && trigger != "file-open") {
            return
        }

        val snapshot = service<ThemeSnapshotService>().capture()
        val tokens = service<CssTokenGenerator>().generate(snapshot)
        val baseCss = service<MarkdownRuleComposer>().compose(tokens, settings.profile)
        val css = appendCustomOverrides(baseCss, settings.customOverridesPath)
        service<CssOutputService>().write(css)
        service<PreviewSyncService>().applyGeneratedCss()
    }

    private fun appendCustomOverrides(baseCss: String, customOverridesPath: String): String {
        val trimmed = customOverridesPath.trim()
        if (trimmed.isEmpty()) {
            return baseCss
        }

        return try {
            val path = Path.of(trimmed)
            if (!Files.exists(path)) {
                logger.warn("MarkTone custom overrides path does not exist: $trimmed")
                baseCss
            } else {
                val customCss = Files.readString(path)
                buildString {
                    append(baseCss)
                    append("\n\n/* MarkTone custom overrides */\n")
                    append(customCss)
                }
            }
        } catch (ex: Throwable) {
            logger.warn("Unable to read custom overrides from path: $trimmed", ex)
            baseCss
        }
    }

    private fun scheduleSync(trigger: String) {
        if (!inFlight.compareAndSet(false, true)) {
            return
        }

        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                regenerateAndApply(trigger)
            } finally {
                inFlight.set(false)
            }
        }
    }
}
