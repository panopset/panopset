package com.panopset.fxapp

import javafx.stage.Stage
import java.io.File

object DeskApp4XFactory {
    lateinit var panApplication: PanApplication
    lateinit var appDDSFX: AppDDSFX
    lateinit var brandedApp: BrandedApp
    val deskApp4FX = DeskApp4FX()

    fun withAppDDSFX(appDDSFX: AppDDSFX): DeskApp4XFactory {
        this.appDDSFX = appDDSFX
        return this
    }

    fun withBrandedApp(brandedApp: BrandedApp): DeskApp4XFactory {
        this.brandedApp = brandedApp
        return this
    }

    fun withPanApplication(panApplication: PanApplication): DeskApp4XFactory {
        this.panApplication = panApplication
        return this
    }

    fun assemblerFxDoc(stage: Stage, file: File): FxDoc {
        return FxDoc(panApplication, stage, file)
    }
}
