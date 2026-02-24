package io.marktone.widget

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class MarkToneStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = MarkToneStatusBarWidget.ID

    override fun getDisplayName(): String = "MarkTone Profile"

    override fun createWidget(project: Project): StatusBarWidget = MarkToneStatusBarWidget(project)
}
