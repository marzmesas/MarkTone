package io.marktone.widget

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import io.marktone.services.ThemeSyncCoordinator
import io.marktone.settings.MarkToneProfile
import io.marktone.settings.MarkToneSettingsListener
import io.marktone.settings.MarkToneSettingsState

class MarkToneStatusBarWidget(project: Project) :
    EditorBasedWidget(project),
    StatusBarWidget.MultipleTextValuesPresentation {

    companion object {
        const val ID = "io.marktone.StatusBarWidget"
    }

    @Volatile
    private var currentProfileName: String = service<MarkToneSettingsState>().current().profile.displayName

    init {
        ApplicationManager.getApplication().messageBus.connect(this)
            .subscribe(MarkToneSettingsListener.TOPIC, MarkToneSettingsListener { state ->
                currentProfileName = state.profile.displayName
                myStatusBar?.updateWidget(ID)
            })
    }

    override fun ID(): String = ID

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun getTooltipText(): String = "MarkTone rendering profile"

    override fun getSelectedValue(): String = "MarkTone: $currentProfileName"

    override fun getPopup(): ListPopup {
        val group = object : ActionGroup() {
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

        return JBPopupFactory.getInstance().createActionGroupPopup(
            "MarkTone Profile",
            group,
            DataContext.EMPTY_CONTEXT,
            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
            false,
        )
    }
}
