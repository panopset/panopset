package com.panopset.fxapp

import javafx.stage.Stage

interface StageIcon {
    fun setFavIcon(stage: Stage, dds: AppDDSFX) {
        stage.icons.add(dds.createFaviconImage())
    }
}
