package com.panopset.desk.utilities

import com.panopset.PanopsetBranding
import com.panopset.compat.*
import com.panopset.desk.utilities.skyscraper.*
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanSplitPane
import com.panopset.fxapp.PanComponentFactory.createPanVBoxHGrow
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.layout.*
import kotlin.collections.ArrayList

class Skyscraper: BrandedApp(
    object: ApplicationInfo {
        override fun getApplicationBranding(): ApplicationBranding {
            return PanopsetBranding()
        }

        override fun getApplicationDisplayName(): String {
            return "Skyscraper"
        }

        override fun getDescription(): String {
            return "API tester. Scrape info from the clouds."
        }
    }
) {
    private val floorMap = HashMap<FxDoc, Floor>()
    private fun getFloor(fxDoc: FxDoc): Floor {
        var rtn = floorMap[fxDoc]
        if (rtn == null) {
            rtn = Floor(fxDoc)
            floorMap[fxDoc] =  rtn
        }
        return rtn
    }

    override fun createDynapane(fxDoc: FxDoc): Pane {
        val mainBorderPane: BorderPane = createStandardMenubarBorderPane(fxDoc)
        mainBorderPane.center = createMainBorderPaneCenter(fxDoc)
        return mainBorderPane
    }

    override fun afterShow(fxDoc: FxDoc) {
        val floor = getFloor(fxDoc)
        updateEnvironmentWithProps(floor, floor.panCheckboxMenu.currentSelection)
    }

    override fun addAppMenus(fxDoc: FxDoc, menus: ObservableList<Menu>) {
        super.addAppMenus(fxDoc, menus)
        val floor = getFloor(fxDoc)
        loadEnvironments(floor.envMap)
        menus.add(createEnvMenu(floor))
    }

    private fun createEnvMenu(floor: Floor): Menu {
        floor.panCheckboxMenu = createPanEnvMenu(floor, "Env")
        if (floor.envMap.isEmpty()) {
            return createEnvHelpMenu(floor, floor.panCheckboxMenu.menu.text)
        }
        return floor.panCheckboxMenu.menu
    }

    private fun createPanEnvMenu(floor: Floor, menuName: String): PanCheckboxMenu {
        val cmf = object : PanCheckboxMenuFactory(menuName) {
            override fun assignAction(
                panCheckboxMenuItem: PanCheckboxMenuItem,
                panCheckboxMenu: PanCheckboxMenu
            ) {
                panCheckboxMenuItem.checkboxMenuItem.onAction = EventHandler {
                    panCheckboxMenu.setTheCurrentSelection(panCheckboxMenuItem.checkboxMenuItem.text)
                    updateEnvironmentWithProps(floor, panCheckboxMenuItem.checkboxMenuItem.text)
                }
            }

            override fun isThisMatch(panCheckboxMenuItem: PanCheckboxMenuItem, value: String): Boolean {
                return value == panCheckboxMenuItem.checkboxMenuItem.text
            }

            override fun populateListOfMenuItemNames(menuItemNames: ArrayList<String>) {
                for (entry in floor.envMap.entries) {
                    menuItemNames.add(entry.key)
                }
            }
        }
        return cmf.construct(floor.fxDoc)
    }

    //  value == panCheckboxMenuItem.checkboxMenuItem.text
// abstract report:
//updateEnvironmentWithProps(envValues)

    //updateEnvironmentWithProps(floor.envMap[rtn.currentSelection])
    //.envMap[panCheckboxMenuItem.checkboxMenuItem.text]

    private fun updateEnvironmentWithProps(floor: Floor, envMapKay: String) {
        floor.updateUrlOutTFfromUrlTextField(envMapKay)
    }

    private fun createEnvHelpMenu(floor: Floor, name: String): Menu {
        val envMenu = Menu(name)
        envMenu.items.add(createMenuItem({
            Logz.warn("No environments defined, please see instructions in the response field below.")
            floor.updateResponse("It looks like you have no environments defined.\n\n" +
                    "Please add properties files to your ${establishBaseDirectory()} directory.\n\n" +
                    "For example, if you want a DEV environment, you would name it DEV.properties.\n" +
                    "(it will also recognize and load .txt and .preferences file extensions.) \n\n" +
                    "Next, please define the variables you want available, as name value pairs,\n" +
                    "something like:\n\n" +
                    "protocol=http://\n" +
                    "server=localhost\n" +
                    "port=:8080\n" +
                    "context=beam\n\n" +
                    "In this example, your URL would then look like this: \n\n" +
                    "\${protocol}\${server}\${port}\${context}/af\n\n" +
                    "If you wish to change the environment\n" +
                    "definitions directory, create an environment variable on your system with the key:\n\n" +
                    PANOPSET_SKYSCRAPER_ENVS
            )
        }, "Help"))
        return envMenu
    }

    private fun createMainBorderPaneCenter(fxDoc: FxDoc): BorderPane {
        val floor = getFloor(fxDoc)
        val bp = BorderPane()
        val vBox = VBox()
        vBox.children.add(createTopControlPane(fxDoc, bp, floor));
        vBox.maxWidth = Double.MAX_VALUE;
        vBox.maxHeight = Double.MAX_VALUE;
        bp.top = vBox
        updateCenterBorderPaneCenterCenter(bp, floor)
        HBox.setHgrow(bp, Priority.ALWAYS)
        return bp
    }

    private fun updateCenterBorderPaneCenterCenter(bp: BorderPane, floor: Floor) {
        Platform.runLater {
            bp.center = selectCenterPane(floor)
        }
    }

    private fun selectCenterPane(floor: Floor): SplitPane {
        val splitPaneID = "bodyResponseSplitPane"
        return when (floor.postMethodChoiceBox.getCurrentChoice()) {
            HttpCommMethod.GET.name -> {
                createPanSplitPane(floor.fxDoc, splitPaneID, floor.headerTextArea, floor.responseTextArea)
            }
            HttpCommMethod.POST.name -> {
                createPanSplitPane(floor.fxDoc, splitPaneID, floor.headerTextArea, floor.bodyTextArea, floor.responseTextArea)
            }
            else -> {
                createPanSplitPane(floor.fxDoc, splitPaneID, floor.headerTextArea, floor.responseTextArea)
            }
        }
    }

    private fun createTopControlPane(fxDoc: FxDoc, bp: BorderPane, floor: Floor): Pane {
        val rtn = createPanHBox(
            createPanButton(fxDoc, { doSend(floor) }, "_send", true, "Send the request."),
            floor.postMethodChoiceBox.getChoiceBoxForDisplayOnly(),
            createPanVBoxHGrow(floor.urlTextField, floor.urlOutField)
        )
        floor.postMethodChoiceBox.setAction(EventHandler {
            updateCenterBorderPaneCenterCenter(bp, floor)
        })
        return rtn
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Skyscraper().go()
        }
    }
}

private fun doSend(floor: Floor) {
    when (floor.postMethodChoiceBox.getCurrentChoice()) {
        HttpCommMethod.GET.name -> {
            doGet(floor)
        }
        HttpCommMethod.POST.name -> {
            doPost(floor)
        }
    }
}

private fun doPost(floor: Floor) {
    val actualURL = floor.urlOutField.text
    val headerTextField = floor.headerTextArea
    val responseTextArea = floor.responseTextArea
    val bodyTextField = floor.bodyTextArea
    clrmsg(responseTextArea)
    val startTime = System.currentTimeMillis()
    try {
        processResponse(doPostHttp(createHeaders(headerTextField.text), actualURL, bodyTextField.text), actualURL, responseTextArea, startTime)
    } catch (throwable: Throwable) {
        floor.fxDoc.errorEx(throwable)
    }
}

private fun doGet(floor: Floor) {
    val actualURL = floor.urlOutField.text
    val headerTextField = floor.headerTextArea
    val responseTextArea = floor.responseTextArea
    clrmsg(responseTextArea)
    val startTime = System.currentTimeMillis()
    try {
        processResponse(doGetHttp(createHeaders(headerTextField.text), actualURL), actualURL, responseTextArea, startTime)
    } catch (throwable: Throwable) {
        floor.fxDoc.errorEx(throwable)
    }
}

fun createHeaders(rawHeaders: String): MutableMap<String, String> {
    val rtn = HashMap<String, String>()
    val lines = Stringop.stringToList(rawHeaders)
    for (s in lines) {
        val i = s.indexOf(":")
        if (i > -1) {
            val key = s.substring(0, i)
            val value = s.substring(i + 1).trimStart()
            if (key.isNotEmpty() && value.isNotEmpty()) {
                rtn[key] = value
            }
        }
    }
    return rtn
}

private fun clrmsg(ta: TextArea) {
    Platform.runLater {
        ta.text = ""
    }
}

private fun processResponse(rp: HttpResponsePackage, urlString: String, responseTextArea: TextArea, startTime: Long) {
    if (rp.responseCode == 200) {
        val finishTime = System.currentTimeMillis()
        val totalTime = finishTime - startTime
        Logz.green("Completed in $totalTime ms. $urlString")
    } else {
        if (rp.responseCode == -1) {
            Logz.errorMsg("No response was received, ${rp.errorMessage}")
        } else {
            Logz.errorMsg("Response code was ${rp.responseCode}, ${rp.errorMessage}")
        }
    }
    Platform.runLater {
        responseTextArea.text = rp.text
    }
}
