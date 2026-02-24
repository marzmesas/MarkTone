package io.marktone.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import io.marktone.services.ThemeSyncCoordinator

class MarkdownFileOpenListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        if (file.extension != "md" && file.extension != "markdown") return

        ApplicationManager.getApplication().executeOnPooledThread {
            service<ThemeSyncCoordinator>().regenerateAndApply("file-open")
        }
    }
}
