package io.marktone.settings

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class MarkToneSettingsStateTest {

    // --- Defaults ---

    @Test
    fun defaultStateIsEnabled() {
        val state = MarkToneSettingsState.State()
        assertTrue(state.enabled)
    }

    @Test
    fun defaultStateHasAutoSync() {
        val state = MarkToneSettingsState.State()
        assertTrue(state.autoSync)
    }

    @Test
    fun defaultProfileIsExpressive() {
        val state = MarkToneSettingsState.State()
        assertEquals(MarkToneProfile.EXPRESSIVE, state.profile)
    }

    @Test
    fun defaultCustomOverridesPathIsEmpty() {
        val state = MarkToneSettingsState.State()
        assertEquals("", state.customOverridesPath)
    }

    @Test
    fun defaultFontSizeScalingIs100() {
        val state = MarkToneSettingsState.State()
        assertEquals(100, state.fontSizeScaling)
    }

    // --- Copy behavior ---

    @Test
    fun copyPreservesAllFields() {
        val state = MarkToneSettingsState.State(
            enabled = false,
            autoSync = false,
            profile = MarkToneProfile.MINIMAL,
            customOverridesPath = "/tmp/overrides.css",
            fontSizeScaling = 120,
        )
        val copy = state.copy()
        assertEquals(state, copy)
        assertNotSame(state, copy)
    }

    @Test
    fun copyWithProfileChangePreservesOtherFields() {
        val state = MarkToneSettingsState.State(fontSizeScaling = 110)
        val changed = state.copy(profile = MarkToneProfile.MINIMAL)

        assertEquals(MarkToneProfile.MINIMAL, changed.profile)
        assertEquals(110, changed.fontSizeScaling)
        assertTrue(changed.enabled)
        assertTrue(changed.autoSync)
    }

    @Test
    fun copyWithScalingChangePreservesOtherFields() {
        val state = MarkToneSettingsState.State(profile = MarkToneProfile.MINIMAL)
        val changed = state.copy(fontSizeScaling = 130)

        assertEquals(130, changed.fontSizeScaling)
        assertEquals(MarkToneProfile.MINIMAL, changed.profile)
    }

    // --- Persistence round-trip ---

    @Test
    fun loadStateReplacesCurrentState() {
        val service = MarkToneSettingsState()
        val newState = MarkToneSettingsState.State(
            enabled = false,
            autoSync = false,
            profile = MarkToneProfile.MINIMAL,
            customOverridesPath = "/home/user/custom.css",
            fontSizeScaling = 90,
        )

        service.loadState(newState)

        assertEquals(newState, service.state)
    }

    @Test
    fun getStateReturnsLoadedState() {
        val service = MarkToneSettingsState()
        val newState = MarkToneSettingsState.State(fontSizeScaling = 115)

        service.loadState(newState)

        assertEquals(115, service.state.fontSizeScaling)
    }

    @Test
    fun currentReturnsCopyNotReference() {
        val service = MarkToneSettingsState()
        val current = service.current()
        assertNotSame(service.state, current)
        assertEquals(service.state, current)
    }
}
