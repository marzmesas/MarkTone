package io.marktone.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "MarkToneSettings", storages = [Storage("marktone.xml")])
@Service(Service.Level.APP)
class MarkToneSettingsState : PersistentStateComponent<MarkToneSettingsState.State> {

    data class State(
        var enabled: Boolean = true,
        var autoSync: Boolean = true,
        var profile: MarkToneProfile = MarkToneProfile.EXPRESSIVE,
        var customOverridesPath: String = "",
    )

    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun current(): State = state.copy()

    fun update(newState: State) {
        state = newState
    }
}
