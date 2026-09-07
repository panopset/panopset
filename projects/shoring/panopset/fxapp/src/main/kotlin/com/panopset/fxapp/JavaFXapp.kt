package com.panopset.fxapp

import com.panopset.compat.*
import com.panopset.fxapp.AnchorFactory.addAnchor
import com.panopset.fxapp.AnchorFactory.findAnchor
import com.panopset.fxapp.AnchorFactory.getAnchors
import com.panopset.fxapp.AnchorFactory.removeAnchor
import com.panopset.fxapp.PanComponentFactory.licenseLinkText
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.io.StringWriter
import java.net.URI
import java.net.URISyntaxException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

object JavaFXapp {

    private fun findActiveStage(): Stage? {
        val fxDoc = findAnchor() as? FxDoc? ?: return null
        return fxDoc.stage
    }

    fun launch(stage: Stage) {
        val rawFiles = globalPropsGet(DeskApp4XFactory.panApplication.filesKey)
        if (Stringop.isPopulated(rawFiles)) {
            val files = rawFiles.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sortedFileSet = Collections.synchronizedSortedSet(TreeSet<String>())
            for (rawPath in files) {
                sortedFileSet.add(rawPath)
            }
            var firstTime = true
            for (rawPath in sortedFileSet) {
                val path = URLDecoder.decode(rawPath, StandardCharsets.UTF_8)
                val file = File(path)
                if (firstTime) {
                    val fxDoc = DeskApp4XFactory.assemblerFxDoc(stage, file)
                    show(fxDoc)
                    firstTime = false
                } else {
                    val fxDoc = DeskApp4XFactory.assemblerFxDoc(Stage(), file)
                    show(fxDoc)
                }
            }
            globalPropsPut(DeskApp4XFactory.panApplication.filesKey, "")
        } else {
            addAndShow(stage)
        }
    }

    fun doLaunch() {
        DeskApp4XFactory.deskApp4FX.doLaunch()
    }

    private fun addStage() {
        addAndShow(Stage())
    }

    private fun addAndShow(stage: Stage) {
        show(FxDoc(DeskApp4XFactory.panApplication, stage))
    }

    private fun show(fxDoc: FxDoc) {
        clearLogFileContents(fxDoc)
        addAnchor(fxDoc)
        StageManager.assembleAndShow(DeskApp4XFactory.deskApp4FX, fxDoc)
        fxDoc.setBoltValues()
        Thread {
            Thread.sleep(400)
            Platform.runLater {
                fxDoc.application.afterShow(fxDoc)
            }
        }.start()
    }

    private fun doSave(fxDoc: FxDoc) {
        fxDoc.saveDataToFile()
        Logz.green("Saved to ${fxDoc.getFileName()}.")
    }

    private fun doSaveAs(fxDoc: FxDoc) {
        val fileChooser = FileChooser()
        fileChooser.title = "Save as..."
        val lpd = globalPropsGet(GLOBAL_LAST_PARENT_DIR)
        if (Stringop.isPopulated(lpd)) {
            fileChooser.initialDirectory = File(lpd)
        }
        val file = fileChooser.showSaveDialog(findStage())
        if (file == null) {
            Logz.warn("New file not set.")
            return
        }
        fxDoc.setFile(file)
        doSave(fxDoc)
        fxDoc.updateTitle()
    }

    private fun doOpen() {
        val fileChooser = FileChooser()
        fileChooser.title = ("Open a previously saved "
                + DeskApp4XFactory.panApplication.applicationShortName
                + " properties file.")
        val lpd = globalPropsGet(GLOBAL_LAST_PARENT_DIR)
        if (Stringop.isPopulated(lpd)) {
            fileChooser.initialDirectory = File(lpd)
        }
        val file = fileChooser.showOpenDialog(findStage())
        if (file == null) {
            Logz.warn("No file selected.")
            return
        }
        if (!file.exists()) {
            Logz.warn("File ${Fileop.getCanonicalPath(file)} does not exist.")
            return
        }
        show(DeskApp4XFactory.assemblerFxDoc(Stage(), file))
    }

    private fun doAbout(fxDoc: FxDoc) {
        val appDisplayName = DeskApp4XFactory.panApplication.getApplicationDisplayName()
        val companyName = DeskApp4XFactory.panApplication.getCompanyName()
        val alert = Alert(AlertType.INFORMATION)
        val bannerImageName = companyName.lowercase(Locale.getDefault())
        val bannerImage = Image(javaClass.getResourceAsStream("/$bannerImageName.png"))
        alert.graphic = ImageView(bannerImage)
        alert.title = String.format(
            "About %s %s", DeskApp4XFactory.panApplication.getCompanyName(),
            DeskApp4XFactory.panApplication.getApplicationDisplayName()
        )
        val contentHeader = Label(DeskApp4XFactory.panApplication.getDescription())

        val contentTextArea = TextArea("Version " + AppVersion.getFullVersion() +
                "\n\n " +
                "\u00a9 1996-2026 Karl Dinwiddie.\n\n" +

                "This program is free software for those that can't afford it: \n" +
                "you can redistribute it and/or modify it under the terms of the GNU \n" +
                "General Public License as published by the Free Software Foundation, \n" +
                "either version 3 of the License, or (at your option) any later \n" +
                "version. \n\n" +
                "This program is distributed in the hope that it will be useful, \n" +
                "but WITHOUT ANY WARRANTY; without even the implied warranty of \n" +
                "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. \n\n" +
                "Please click the link below for the full license, " +
                "it will open in your default browser.")

        val licenceLink = Hyperlink(licenseLinkText)
        licenceLink.onAction = EventHandler {
            if (Desktop.isDesktopSupported()) {
                Thread {
                    try {
                        Desktop.getDesktop().browse(URI(licenseLinkText))
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    } catch (e1: URISyntaxException) {
                        e1.printStackTrace()
                    }
                }.start()
            }
        }
        val contentBorderPane = BorderPane()
        contentBorderPane.top = contentHeader
        contentBorderPane.center = contentTextArea
        contentBorderPane.bottom = licenceLink
        contentTextArea.isEditable = false
        val stage = alert.dialogPane.scene.window as Stage
        stage.icons.add(dds!!.createFaviconImage())
        stage.scene.root.style = fxDoc.scene.root.style
        alert.dialogPane.setPrefSize(600.0, 400.0);
        alert.dialogPane.headerText = appDisplayName
        alert.dialogPane.content = contentBorderPane
        alert.showAndWait()
    }

    private fun doShowLog(fxDoc: FxDoc) {
        val logStage = findLogStage(fxDoc)
        updateLogTA(fxDoc)
        restoreStageAttributes(FxDocPropKeys.DIMS_LOG, logStage, fxDoc.pmf,
            600, 400)
        if (!logStage.isShowing) {
            logStage.scene.root.style = fxDoc.scene.root.style
            logStage.show()
        }
        logStage.toFront()
        logStage.requestFocus()
        logStage.onCloseRequest = EventHandler {
            saveStageAttributes(FxDocPropKeys.DIMS_LOG, logStage, fxDoc.pmf)
            fxDoc.saveDataToFile()
        }
    }

    @Synchronized
    private fun saveOpenWindowStateGlobally() {
        var firstTime = true
        val sw = StringWriter()
        for (anchor in getAnchors()) {
            val path = URLEncoder.encode(anchor.getPath(), StandardCharsets.UTF_8)
            if (firstTime) {
                firstTime = false
            } else {
                sw.append(",")
            }
            sw.append(path)
        }
        globalPropsPut(DeskApp4XFactory.panApplication.filesKey, sw.toString())
        globalPropsFlush()
    }

    fun doExit() {
        Zombie.stop()
        saveOpenWindowStateGlobally()
        for (anchor in getAnchors()) {
            (anchor as FxDoc).closeWindow()
        }
    }

    const val GLOBAL_LAST_PARENT_DIR = "com.panopset.global_last_parent_dir"

    fun findStage(): Stage? {
        return findActiveStage()
    }

    lateinit var dds: AppDDSFX

    fun newWindow() {
        addStage()
    }

    fun showAboutPanel(fxDoc: FxDoc) {
        doAbout(fxDoc)
    }

    fun showLogPanel(fxDoc: FxDoc) {
        doShowLog(fxDoc)
    }

    fun saveWindow(fxDoc: FxDoc) {
        doSave(fxDoc)
    }

    fun saveWindowAs(fxDoc: FxDoc) {
        doSaveAs(fxDoc)
    }

    fun openWindowFromFile() {
        doOpen()
    }

    fun panExit() {
        doExit()
    }

    fun closeWindow(fxDoc: FxDoc) {
        val size = getAnchors().size
        if (size == 1) {
            doExit()
            return
        }
        fxDoc.closeWindow()
        removeAnchor(fxDoc)
    }
}
