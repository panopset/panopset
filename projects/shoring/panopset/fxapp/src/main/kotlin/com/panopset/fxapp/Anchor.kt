package com.panopset.fxapp

import com.panopset.compat.*
import com.panopset.compat.Stringop.FSP
import javafx.application.Platform
import javafx.scene.control.*
import java.io.File
import java.util.*
import kotlin.DoubleArray
import kotlin.NumberFormatException
import kotlin.String

abstract class Anchor(val application: PanApplication) {
    constructor(anchorManager: PanApplication, file: File) : this(anchorManager) {
        setPersistentMapFile(file)
    }

    fun getStringValue(key: String): String {
        val boltBox = boltBoxes[key] ?: return pmf.getMapValue(key)
        val value = boltBox.getValue()
        return value
    }

    fun setStringValue(key: String, value: String) {
        if (boltBoxes.containsKey(key)) {
            boltBoxes[key]?.setValue(value)
        } else {
            pmf.put(key, value)
        }
    }

    fun getIntValue(key: String): Int {
        val value = getStringValue(key)
        if (value.isEmpty()) {
            return 0
        }
        val intValue = value.toInt()
        return intValue
    }

    fun getBooleanValue(key: String): Boolean {
        val value = getStringValue(key)
        val booleanValue = value.toBoolean()
        return booleanValue
    }

    fun toggle(key: String) {
        val value = getStringValue(key)
        val booleanValue = !value.toBoolean()
        if (boltBoxes.containsKey(key)) {
            boltBoxes[key]?.setValue(booleanValue.toString())
        }
    }

    fun getArrayListValue(key: String): ArrayList<String> {
        val value = getStringValue(key)
        val arrayListValue = Stringop.stringToList(value)
        return arrayListValue
    }

    fun setDefaultValue(key: String, value: String) {
        boltDefaults[key] = value
    }

    fun createWindowTitle(): String {
        return String.format("%s ~ %s", application.getApplicationDisplayName(), getPath())
    }

    protected abstract fun updateTitle()
    fun loadDataFromFile() {
        pmf.load()
    }

    fun setBoltValues() {
        if (pmf.file.exists()) {
            for ((key, value1) in boltBoxes) {
                if (pmf.containsKey(key)) {
                    val value: String = pmf.getMapValue(key)
                    value1.setValue(value)
                }
            }
        }
        updateTitle()
    }

    fun saveDataToFile() {
        for ((key, value1) in boltBoxes) {
            val value = value1.getValue()
            pmf.put(key, value)
        }
        pmf.flush()
        globalPropsPut(JavaFXapp.GLOBAL_LAST_PARENT_DIR, pmf.file.parent)
    }

    fun setFile(file: File) {
        pmf.setNewFile(file)
    }

    fun getPath(): String {
        return pmf.file.canonicalPath
    }

    fun getFileName(): String {
        return pmf.file.name
    }

    private val boltBoxes: MutableMap<String, BoltBox> = HashMap()
    private val boltDefaults: MutableMap<String, String> = HashMap()
    var pmf = PersistentMapFile(File(createDefaultPath()))

    private fun setPersistentMapFile(file: File) {
        pmf = PersistentMapFile(file)
    }

    fun getTheFilePath(): String {
        return pmf.file.canonicalPath
    }

    private fun createDefaultPath(): String {
        var path = ""
        while (path.isEmpty() || File(path).exists()) {
            val untitledFileName = createNextUntitledPath()
            path = HiddenFolder.getFullPathRelativeTo("temp${FSP}$untitledFileName")
        }
        return path
    }

    private fun createNextUntitledPath(): String {
        return "${application.applicationShortName}_Untitled${application.getNextUniqueID()}.properties"
    }

    fun registerChoiceBox(keyChain: String, panChoiceBox: PanChoiceBox, defaultValue: String, choices: ArrayList<String>) {
         panChoiceBox.register(this, keyChain, defaultValue, choices)
    }

    fun registerCheckBox(keyChain: String, cb: CheckBox) {
        val defaultValue = "" + cb.isSelected

        registerData(keyChain, BoltBox(object : Bolt {
            override fun setBoltValue(value: String) {
                if (Stringop.isPopulated(value)) {
                    val bv = value.toBoolean()
                    cb.isSelected = bv
                }
            }

            override fun getBoltValue(): String {
                return "" + cb.isSelected
            }

            override fun getBoltDefault(): String {
                return defaultValue
            }
        }))
    }

    fun registerToggleButton(keyChain: String, tb: ToggleButton) {
        val defaultValue = "" + tb.isSelected

        registerData(keyChain, BoltBox(object : Bolt {
            override fun setBoltValue(value: String) {
                if (Stringop.isPopulated(value)) {
                    val bv = value.toBoolean()
                    tb.isSelected = bv
                }
            }

            override fun getBoltValue(): String {
                return "" + tb.isSelected
            }

            override fun getBoltDefault(): String {
                return defaultValue
            }
        }))
    }

    fun registerTextInputControlDft(keyChain: String, tf: TextInputControl, dft: String) {
        if (isPasswordField(tf)) {
            return
        }
        if (tf.id == null) {
            tf.id = keyChain
        }
        val currentTextValue = tf.text
        val b = object : Bolt {
            override fun setBoltValue(value: String) {
                Platform.runLater { tf.text = value }
            }

            override fun getBoltValue(): String {
                return tf.text
            }

            override fun getBoltDefault(): String {
                val dft = boltDefaults[tf.id]
                return if (Stringop.isBlank(dft)) {
                    currentTextValue
                } else dft!!
            }
        }
        val bb = BoltBox(b)
        registerData(keyChain, bb)
    }

    fun registerTextInputControl(keyChain: String, tf: TextInputControl) {
        if (isPasswordField(tf)) {
            return
        }
        if (tf.id == null) {
            tf.id = keyChain
        }
        val currentTextValue = tf.text
        registerData(keyChain, BoltBox(object : Bolt {
            override fun setBoltValue(value: String) {
                Platform.runLater { tf.text = value }
            }

            override fun getBoltValue(): String {
                return tf.text
            }

            override fun getBoltDefault(): String {
                val dft = boltDefaults[tf.id]
                return if (Stringop.isBlank(dft)) {
                    currentTextValue
                } else dft!!
            }
        }))
    }

    fun registerSplitPaneLocations(keyChain: String, splitPane: SplitPane) {
        val currentValue = splitPane.dividerPositions.contentToString()
        registerData(keyChain, BoltBox(object : Bolt {
            override fun setBoltValue(value: String) {
                if (!Stringop.isPopulated(value)) {
                    return
                }
                if ("[]".equals(value, ignoreCase = true)) {
                    return
                }
                val str = value.replace("[", "").replace("]", "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val positions = DoubleArray(str.size)
                for (i in str.indices) {
                    positions[i] = str[i].toDouble()
                }
                splitPane.setDividerPositions(*positions)
            }

            override fun getBoltValue(): String {
                return splitPane.dividerPositions.contentToString()
            }

            override fun getBoltDefault(): String {
                return currentValue
            }
        }))
    }

    fun registerTabSelected(keyChain: String, tabPane: TabPane) {
        val currentValue = tabPane.selectionModel.selectedIndex
        registerData(keyChain, BoltBox(object : Bolt {
            override fun getBoltValue(): String {
                return "" + tabPane.selectionModel.selectedIndex
            }

            override fun setBoltValue(value: String) {
                if (Stringop.isPopulated(value)) {
                    try {
                        val i = value.toInt()
                        tabPane.selectionModel.select(i)
                    } catch (ex: NumberFormatException) {
                        Logz.errorEx(ex)
                    }
                }
            }

            override fun getBoltDefault(): String {
                return "" + currentValue
            }
        }))
    }

    fun registerData(key: String, boltBox: BoltBox) {
        boltBoxes.computeIfAbsent(key) { boltBox }
    }
}
