package com.panopset.fxapp

import com.panopset.compat.*
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight

object FontManagerFX {
    var fontSize: FontSize = FontSize.DEFAULT_SIZE

    private var monospace: Font = Font(MONOSPACE, fontSize.value.toDouble())
    private var boldArial: Font = Font.font(ARIAL, FontWeight.BOLD, fontSize.value.toDouble())
    private var plainArial: Font = Font(ARIAL, fontSize.value.toDouble())
    private var boldSerif: Font = Font.font(SERIF, FontWeight.BOLD, fontSize.value.toDouble())
    private var plainSerif: Font = Font(SERIF, fontSize.value.toDouble())
    private var borderTitle: Font = Font.font(ARIAL, FontPosture.ITALIC, fontSize.value.toDouble())
//    private var logEntry: LogEntry = LogEntry(AlertColor.GREEN, Level.INFO, "")

    init {
        fontSize = try {
            val savedFontSizeValue = globalPropsGet(FONT_SIZE_KEY).replace("\"", "")
            if (savedFontSizeValue.isEmpty()) {
                FontSize.DEFAULT_SIZE
            }
            FontSize.findFromValue(savedFontSizeValue)
        } catch (ex: NumberFormatException) {
            Logz.errorEx(ex)
            FontSize.DEFAULT_SIZE
        }
    }

    fun getCurrentFontSizeName(): String {
        return fontSize.name
    }

    private fun getMonoStyle(i: Int): String {
        return String.format("-fx-font-size: %dpx; -fx-font-family: 'monospaced';", i)
    }

    fun updateAllFontSizes(fxDoc: FxDoc) {
        val style = getStyleFor(fontSize.value)
        val stylem = getMonoStyle(fontSize.value)
        for (mb in fxDoc.mbs) {
            mb.style = style
        }
        for (node in fxDoc.nodes) {
            if (node is TextInputControl) {
                if (MBSM == node.id) {
                    Platform.runLater {
                        fxDoc.fxDocMessage.refresh()
                    }
                } else {
                    node.style = stylem
                }
            } else {
                node.style = style
            }
        }
        for (tabPane in fxDoc.tabPanes) {
            tabPane.style = style
        }
        for (tab in fxDoc.tabs) {
            tab.style = style
        }
        globalPropsPut( FONT_SIZE_KEY, String.format("%d", fontSize.value))
        monospace = Font(MONOSPACE, fontSize.value.toDouble())
        boldArial = Font.font(ARIAL, FontWeight.BOLD, fontSize.value.toDouble())
        plainArial = Font(ARIAL, fontSize.value.toDouble())
        boldSerif = Font.font(SERIF, FontWeight.BOLD, fontSize.value.toDouble())
        plainSerif = Font(SERIF, fontSize.value.toDouble())
        borderTitle = Font.font(ARIAL, FontPosture.ITALIC, fontSize.value.toDouble())
    }

    fun getMonospace(): Font {
        return monospace
    }

    fun getBoldArial(): Font {
        return boldArial
    }

    fun getBoldSerif(): Font {
        return boldSerif
    }

    fun getPlainArial(): Font {
        return plainArial
    }

    fun getPlainSerif(): Font {
        return plainSerif
    }

    fun getBorderTitle(): Font {
        return borderTitle
    }

    fun register(fxDoc: FxDoc, control: Node) {
        if (!fxDoc.nodes.contains(control)) {
            fxDoc.nodes.add(control)
            if (control is TextInputControl) {
                control.style = getMonoStyle(fontSize.value)
            } else {
                control.style = getStyleFor(fontSize.value)
            }
        }
    }

    fun registerTab(fxDoc: FxDoc, tab: Tab): Tab {
        if (fxDoc.tabs.contains(tab)) {
            Logz.debug("Ignoring duplicate FontManagerFX registration of tab " + tab.id)
        } else {
            fxDoc.tabs.add(tab)
        }
        return tab
    }

    fun registerMenubar(fxDoc: FxDoc, menuBar: MenuBar) {
        if (fxDoc.mbs.contains(menuBar)) {
            Logz.debug("Ignoring duplicate FontManagerFX registration of menubar " + menuBar.id)
        } else {
            fxDoc.mbs.add(menuBar)
        }
    }

    fun getSize(): Int {
        return fontSize.value
    }

    fun setFontSize(fxDoc: FxDoc, fontSize: FontSize) {
        this.fontSize = fontSize
        updateAllFontSizes(fxDoc)
    }

    val imgRatio: Double = fontSize.imgRatio
    val svgRatio: Double = fontSize.svgRatio

//    private fun getCurrentMessageStyle(logEntry: LogEntry): String {
//        var colorStyle = FONT_GREEN
//        if (logEntry.alert == AlertColor.PURPLE) {
//            colorStyle = FONT_PURPLE
//        } else if (logEntry.alert == AlertColor.BLUE) {
//            colorStyle = FONT_BLUE
//        } else if (logEntry.alert == AlertColor.RED) {
//            colorStyle = FONT_RED
//        } else if (logEntry.alert == AlertColor.ORANGE) {
//            colorStyle = FONT_ORANGE
//        } else if (logEntry.alert == AlertColor.YELLOW) {
//            colorStyle = FONT_YELLOW
//        } else if (logEntry.alert == AlertColor.GREEN) {
//            colorStyle = FONT_GREEN
//        }
//        return colorStyle + getStyleFor(size)
//    }

    fun getStyleFor(fontSizeValue: Int): String {
        return String.format("-fx-font-size: %dpx", fontSizeValue)
    }

//    fun setMenubarLogRecord(logEntry: LogEntry, menubarStatusMessage: TextField) {
//        this.logEntry = logEntry
//        Platform.runLater {
//            menubarStatusMessage.style = getCurrentMessageStyle(logEntry)
//        }
//    }
}

const val LUCIDIA = "Lucida Sans Regular"
const val LUCIDIA_BOLD = "Lucida Sans Bold"
const val LUCIDIA_OBLIQUE = "Lucida Sans Oblique"
const val LUCIDIA_BOLD_OB = "Lucida Sans Bold Oblique"
const val L_BRIGHT = "Lucida Bright Regular"
const val L_BRIGHT_BOLD = "Lucida Bright Bold"
const val L_BRIGHT_OBLIQUE = "Lucida Bright Italic"
const val L_BRIGHT_BOLD_OB = "Lucida Bright Bold Italic"
const val MONOSPACE = "Monospace"
const val MONOSPACE_BOLD = "Monospace Bold"
const val MONOSPACE_OBLIQUE = "Lucida Sans Typewriter Oblique"
const val MONOSPACE_BOLD_OB = "Lucida Sans Typewriter Bold Oblique"
const val ARIAL = "Arial"
const val SERIF = "Serif"
const val DIALOG = "Dialog"

//const val FONT_GREEN = "-fx-background-color: #000000; -fx-text-fill: #99ff99;"
//const val FONT_YELLOW = "-fx-background-color: #000000;-fx-text-fill: #ffff66;"
//const val FONT_ORANGE = "-fx-background-color: #000000;-fx-text-fill: #ffaa00;"
//const val FONT_BLUE = "-fx-background-color: #000000;-fx-text-fill: #0000ff;"
//const val FONT_RED = "-fx-background-color: #000000;-fx-text-fill: #ff3333;"
//const val FONT_PURPLE = "-fx-background-color: #000000;-fx-text-fill: #dd33dd;"
const val MBSM = "menubarStatusMessage"
