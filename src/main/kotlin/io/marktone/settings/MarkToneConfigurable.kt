package io.marktone.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import io.marktone.services.CssTokenGenerator
import io.marktone.services.MarkdownRuleComposer
import io.marktone.services.ThemeSnapshotService
import io.marktone.services.ThemeSyncCoordinator
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
    private var previewPanel: MarkTonePreviewPanel? = null
    private var previewThemeComboBox: JComboBox<PreviewThemeOption>? = null

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
        profileComboBox = JComboBox(MarkToneProfile.entries.toTypedArray()).also {
            it.addActionListener { refreshPreview() }
        }
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
            addChangeListener {
                fontSizeLabel?.text = "Preview font size scaling: ${value}%"
                refreshPreview()
            }
        }
        panel.add(fontSizeSlider, gbc)

        gbc.gridy = 8
        panel.add(JLabel("Preview theme"), gbc)

        gbc.gridy = 9
        previewThemeComboBox = JComboBox<PreviewThemeOption>().also { combo ->
            combo.addItem(PreviewThemeOption.Current)
            EditorColorsManager.getInstance().allSchemes.forEach { scheme ->
                combo.addItem(PreviewThemeOption.Scheme(scheme))
            }
            combo.addActionListener { refreshPreview() }
        }
        panel.add(previewThemeComboBox, gbc)

        // Push controls to the top
        gbc.gridy = 10
        gbc.weighty = 1.0
        panel.add(JPanel(), gbc)

        rootPanel = panel
        reset()

        if (MarkTonePreviewPanel.isAvailable()) {
            val preview = MarkTonePreviewPanel()
            previewPanel = preview

            val scrollPane = JBScrollPane(panel).apply {
                border = null
                minimumSize = java.awt.Dimension(300, 0)
            }

            val splitter = JBSplitter(false, 0.4f).apply {
                firstComponent = scrollPane
                secondComponent = preview.component
            }

            refreshPreview()
            return splitter
        }

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
        refreshPreview()
    }

    override fun disposeUIResources() {
        previewPanel?.let { Disposer.dispose(it) }
        previewPanel = null
        rootPanel = null
        enabledCheckBox = null
        autoSyncCheckBox = null
        profileComboBox = null
        customOverridesField = null
        fontSizeSlider = null
        fontSizeLabel = null
        previewThemeComboBox = null
    }

    private fun generatePreviewCss(): String {
        val themeService = service<ThemeSnapshotService>()
        val selected = previewThemeComboBox?.selectedItem as? PreviewThemeOption
        val snapshot = when (selected) {
            is PreviewThemeOption.Scheme -> themeService.captureFrom(selected.scheme)
            else -> themeService.capture()
        }
        val scaling = fontSizeSlider?.value ?: 100
        val profile = profileComboBox?.selectedItem as? MarkToneProfile ?: MarkToneProfile.EXPRESSIVE
        val tokens = service<CssTokenGenerator>().generate(snapshot, scaling)
        return service<MarkdownRuleComposer>().compose(tokens, profile)
    }

    private fun refreshPreview() {
        previewPanel?.updateCss(generatePreviewCss())
    }
}

private sealed class PreviewThemeOption {
    data object Current : PreviewThemeOption() {
        override fun toString(): String = "Current IDE Theme"
    }

    data class Scheme(val scheme: EditorColorsScheme) : PreviewThemeOption() {
        override fun toString(): String = scheme.displayName
    }
}
