package io.marktone.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.ProjectManager
import org.intellij.plugins.markdown.settings.MarkdownSettings

@Service(Service.Level.APP)
class PreviewSyncService {

    private val logger = thisLogger()

    fun applyGeneratedCss() {
        val cssPath = service<CssOutputService>().outputPath().toString()
        val projects = ProjectManager.getInstance().openProjects

        if (projects.isEmpty()) {
            logger.info("MarkTone generated CSS, but no open projects were found to apply settings.")
            return
        }

        projects.filterNot { it.isDisposed }.forEach { project ->
            try {
                val markdownSettings = MarkdownSettings.getInstance(project)
                markdownSettings.update {
                    it.useCustomStylesheetPath = true
                    it.customStylesheetPath = cssPath
                    it.useCustomStylesheetText = false
                    it.customStylesheetText = ""
                }
                logger.info("MarkTone applied Markdown custom stylesheet for project: ${project.name}")
            } catch (ex: Throwable) {
                logger.warn("Failed applying MarkTone stylesheet for project: ${project.name}", ex)
            }
        }
    }
}
