package io.marktone.settings

import kotlin.test.Test
import kotlin.test.assertEquals

class MarkToneProfileTest {

    @Test
    fun hasTwoProfiles() {
        assertEquals(2, MarkToneProfile.entries.size)
    }

    @Test
    fun minimalDisplayName() {
        assertEquals("Minimal", MarkToneProfile.MINIMAL.displayName)
    }

    @Test
    fun expressiveDisplayName() {
        assertEquals("Expressive", MarkToneProfile.EXPRESSIVE.displayName)
    }

    @Test
    fun entriesOrderIsMinimalThenExpressive() {
        val entries = MarkToneProfile.entries
        assertEquals(MarkToneProfile.MINIMAL, entries[0])
        assertEquals(MarkToneProfile.EXPRESSIVE, entries[1])
    }
}
