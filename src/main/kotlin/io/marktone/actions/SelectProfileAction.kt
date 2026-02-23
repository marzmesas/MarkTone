package io.marktone.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.components.service
import io.marktone.services.ThemeSyncCoordinator
import io.marktone.settings.MarkToneProfile
import io.marktone.settings.MarkToneSettingsState

class SelectProfileAction(private val profile: MarkToneProfile) : ToggleAction(
    profile.displayName,
    null,
    null,
) {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun isSelected(e: AnActionEvent): Boolean {
        return service<MarkToneSettingsState>().current().profile == profile
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        if (!state) return
        val current = service<MarkToneSettingsState>().current()
        service<MarkToneSettingsState>().update(current.copy(profile = profile))
        service<ThemeSyncCoordinator>().regenerateAndApply("settings-change")
    }
}
