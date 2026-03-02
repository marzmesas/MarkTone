package io.marktone.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import io.marktone.services.ThemeSyncCoordinator
import io.marktone.settings.MarkToneProfile
import io.marktone.settings.MarkToneSettingsState

class ToggleProfileAction : AnAction("Toggle MarkTone Profile") {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val settingsState = service<MarkToneSettingsState>()
        val current = settingsState.current()
        val next = when (current.profile) {
            MarkToneProfile.MINIMAL -> MarkToneProfile.EXPRESSIVE
            MarkToneProfile.EXPRESSIVE -> MarkToneProfile.MINIMAL
        }
        settingsState.update(current.copy(profile = next))
        service<ThemeSyncCoordinator>().regenerateAndApply("settings-change")
    }
}
