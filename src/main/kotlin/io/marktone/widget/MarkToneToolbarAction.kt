package io.marktone.widget

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.util.IconLoader
import io.marktone.services.ThemeSyncCoordinator
import io.marktone.settings.MarkToneProfile
import io.marktone.settings.MarkToneSettingsState

class MarkToneToolbarAction : ActionGroup() {

    companion object {
        private val ICON = IconLoader.getIcon("/icons/statusBarWidget.svg", MarkToneToolbarAction::class.java)
    }

    init {
        isPopup = true
        templatePresentation.icon = ICON
        templatePresentation.text = "MarkTone Profile"
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val profileName = service<MarkToneSettingsState>().current().profile.displayName
        e.presentation.icon = ICON
        e.presentation.text = "MarkTone: $profileName"
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return MarkToneProfile.entries.map { profile ->
            object : AnAction(profile.displayName) {
                override fun actionPerformed(e: AnActionEvent) {
                    val current = service<MarkToneSettingsState>().current()
                    service<MarkToneSettingsState>().update(current.copy(profile = profile))
                    service<ThemeSyncCoordinator>().regenerateAndApply("settings-change")
                }
            }
        }.toTypedArray()
    }
}
