package io.marktone.startup

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.marktone.services.ThemeSyncCoordinator

class MarkToneStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        service<ThemeSyncCoordinator>().regenerateAndApply("startup")
    }
}
