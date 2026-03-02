package io.marktone.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.util.concurrency.AppExecutorUtil
import io.marktone.services.ThemeSyncCoordinator
import io.marktone.settings.MarkToneProfile
import io.marktone.settings.MarkToneSettingsState
import java.util.concurrent.TimeUnit

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

        val notification = NotificationGroupManager.getInstance()
            .getNotificationGroup("MarkTone")
            .createNotification("Switched to ${next.displayName}", NotificationType.INFORMATION)
        notification.notify(e.project)
        AppExecutorUtil.getAppScheduledExecutorService()
            .schedule({ notification.expire() }, 3, TimeUnit.SECONDS)
    }
}
