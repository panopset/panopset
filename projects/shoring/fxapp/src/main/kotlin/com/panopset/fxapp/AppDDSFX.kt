package com.panopset.fxapp

import javafx.scene.image.Image
import javafx.scene.layout.Pane

interface AppDDSFX {
    fun createFaviconImage(): Image?
    fun createPane(fxDoc: FxDoc, deskApp4FX: DeskApp4FX): Pane
}
