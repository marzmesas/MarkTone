package io.marktone.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import io.marktone.services.ThemeSyncCoordinator
import com.intellij.ui.components.JBTextField
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.util.Hashtable
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider

class MarkToneConfigurable : Configurable {
    private val settingsService = service<MarkToneSettingsState>()

    private var rootPanel: JPanel? = null
    private var enabledCheckBox: JCheckBox? = null
    private var autoSyncCheckBox: JCheckBox? = null
    private var profileComboBox: JComboBox<MarkToneProfile>? = null
    private var customOverridesField: JBTextField? = null
    private var fontSizeSlider: JSlider? = null
    private var fontSizeLabel: JLabel? = null

    override fun getDisplayName(): String = "MarkTone"

    override fun createComponent(): JComponent {
        val panel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints().apply {
            anchor = GridBagConstraints.WEST
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(4, 4, 4, 4)
            weightx = 1.0
            gridx = 0
        }

        enabledCheckBox = JCheckBox("Enable MarkTone")
        panel.add(enabledCheckBox, gbc)

        gbc.gridy = 1
        autoSyncCheckBox = JCheckBox("Auto-sync when theme changes")
        panel.add(autoSyncCheckBox, gbc)

        gbc.gridy = 2
        panel.add(JLabel("Profile"), gbc)

        gbc.gridy = 3
        profileComboBox = JComboBox(MarkToneProfile.entries.toTypedArray())
        panel.add(profileComboBox, gbc)

        gbc.gridy = 4
        panel.add(JLabel("Custom CSS overrides path (optional)"), gbc)

        gbc.gridy = 5
        customOverridesField = JBTextField()
        panel.add(customOverridesField, gbc)

        gbc.gridy = 6
        fontSizeLabel = JLabel("Preview font size scaling: 100%")
        panel.add(fontSizeLabel, gbc)

        gbc.gridy = 7
        fontSizeSlider = JSlider(90, 180, 100).apply {
            majorTickSpacing = 30
            minorTickSpacing = 10
            paintTicks = true
            paintLabels = true
            labelTable = Hashtable<Int, JLabel>().apply {
                put(90, JLabel("90%"))
                put(120, JLabel("120%"))
                put(150, JLabel("150%"))
                put(180, JLabel("180%"))
            }
            addChangeListener { fontSizeLabel?.text = "Preview font size scaling: ${value}%" }
        }
        panel.add(fontSizeSlider, gbc)

        rootPanel = panel
        reset()
        return panel
    }

    override fun isModified(): Boolean {
        val persisted = settingsService.current()

        val currentEnabled = enabledCheckBox?.isSelected ?: persisted.enabled
        val currentAutoSync = autoSyncCheckBox?.isSelected ?: persisted.autoSync
        val currentProfile = profileComboBox?.selectedItem as? MarkToneProfile ?: persisted.profile
        val currentOverrides = customOverridesField?.text.orEmpty()
        val currentScaling = fontSizeSlider?.value ?: persisted.fontSizeScaling

        return currentEnabled != persisted.enabled ||
            currentAutoSync != persisted.autoSync ||
            currentProfile != persisted.profile ||
            currentOverrides != persisted.customOverridesPath ||
            currentScaling != persisted.fontSizeScaling
    }

    override fun apply() {
        settingsService.update(
            MarkToneSettingsState.State(
                enabled = enabledCheckBox?.isSelected ?: true,
                autoSync = autoSyncCheckBox?.isSelected ?: true,
                profile = profileComboBox?.selectedItem as? MarkToneProfile ?: MarkToneProfile.EXPRESSIVE,
                customOverridesPath = customOverridesField?.text.orEmpty(),
                fontSizeScaling = fontSizeSlider?.value ?: 100,
            ),
        )
        service<ThemeSyncCoordinator>().regenerateAndApply("settings-change")
    }

    override fun reset() {
        val persisted = settingsService.current()
        enabledCheckBox?.isSelected = persisted.enabled
        autoSyncCheckBox?.isSelected = persisted.autoSync
        profileComboBox?.selectedItem = persisted.profile
        customOverridesField?.text = persisted.customOverridesPath
        fontSizeSlider?.value = persisted.fontSizeScaling
        fontSizeLabel?.text = "Preview font size scaling: ${persisted.fontSizeScaling}%"
    }

    override fun disposeUIResources() {
        rootPanel = null
        enabledCheckBox = null
        autoSyncCheckBox = null
        profileComboBox = null
        customOverridesField = null
        fontSizeSlider = null
        fontSizeLabel = null
    }
}
