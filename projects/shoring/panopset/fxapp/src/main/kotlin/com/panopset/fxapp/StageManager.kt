package com.panopset.fxapp

import com.panopset.fxapp.AnchorFactory.removeAnchor
import com.panopset.fxapp.PanComponentFactory.panDarkTheme
import javafx.event.EventHandler
import javafx.scene.Scene

object StageManager : StageIcon {
    var DEFAULT_WIDTH = 1000
    var DEFAULT_HEIGHT = 900
    fun assembleAndShow(deskApp4FX: DeskApp4FX, fxDoc: FxDoc) {
        val stage = fxDoc.stage
        restoreStageAttributes(FxDocPropKeys.DIMS_WIN, stage,
            fxDoc.pmf, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        val scene = Scene(DeskApp4XFactory.appDDSFX.createPane(fxDoc, deskApp4FX), stage.width, stage.height)
        fxDoc.scene = scene
        fxDoc.scene.root.style = panDarkTheme;
        stage.scene = scene
        FontManagerFX.updateAllFontSizes(fxDoc)
        fxDoc.loadDataFromFile()
        stage.onHiding = EventHandler { fxDoc.saveWindow() }
        stage.onCloseRequest = EventHandler { removeAnchor(fxDoc) }
        setFavIcon(stage, DeskApp4XFactory.appDDSFX)
        stage.show()
    }
}
