package io.marktone.settings

import com.intellij.util.messages.Topic

fun interface MarkToneSettingsListener {

    companion object {
        val TOPIC = Topic.create("MarkTone settings changed", MarkToneSettingsListener::class.java)
    }

    fun settingsChanged(state: MarkToneSettingsState.State)
}
