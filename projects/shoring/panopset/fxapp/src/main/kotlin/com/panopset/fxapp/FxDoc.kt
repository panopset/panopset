package com.panopset.fxapp

import com.panopset.compat.*
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.MenuBar
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.stage.Stage
import java.io.File

class FxDoc: Anchor, LogSink {
    var mbs: MutableList<MenuBar> = ArrayList()
    var nodes: MutableList<Node> = ArrayList()
    var tabPanes: MutableList<TabPane> = ArrayList()
    var tabs: MutableList<Tab> = ArrayList()
    val stage: Stage
    lateinit var scene: Scene
    private var closingSaveComplete = false
    val fxDocMessage = FxDocMessage(this)
    constructor(panApplication: PanApplication, stage: Stage, file: File) : super(panApplication, file) {
        this.stage = stage
    }
    constructor(panApplication: PanApplication, stage: Stage) : super(panApplication) {
        this.stage = stage
    }
    public override fun updateTitle() {
        stage.title = createWindowTitle()
    }
    fun closeWindow() {
        saveWindow()
        closingSaveComplete = true
        try {
            stage.close()
        } catch (e: Exception) {
            Logz.errorEx(e)
        }
    }
    fun saveWindow() {
        if (closingSaveComplete) {
            return
        }
        saveStageAttributes(FxDocPropKeys.DIMS_WIN, stage, pmf)
        saveDataToFile()
    }

    override fun green(msg: String) {
        fxDocMessage.setMsg(msg)
    }

    override fun yellow(msg: String) {
        fxDocMessage.setMsg(msg)
    }

    override fun red(msg: String, throwable: Throwable) {
        fxDocMessage.setMsg(msg)
    }

    override fun red(msg: String) {
        fxDocMessage.setErrorMsg(msg)
    }

    override fun red(throwable: Throwable) {
        fxDocMessage.setErrorMsg(throwable.message ?: "")
    }

    override fun clear() {
        fxDocMessage.setMsg("")
    }
}
