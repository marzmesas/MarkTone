package io.marktone.listeners

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import org.intellij.plugins.markdown.settings.MarkdownSettings

class MarkdownSettingsCleanupListener : ProjectManagerListener {

    private val logger = thisLogger()

    override fun projectClosing(project: Project) {
        try {
            val markdownSettings = MarkdownSettings.getInstance(project)
            markdownSettings.update {
                it.useCustomStylesheetText = false
                it.customStylesheetText = ""
            }
            logger.info("MarkTone cleared custom stylesheet for project: ${project.name}")
        } catch (ex: Throwable) {
            logger.warn("Failed to clear MarkTone stylesheet for project: ${project.name}", ex)
        }
    }
}
