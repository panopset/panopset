package com.panopset.fxapp

import com.panopset.compat.HiddenFolder
import com.panopset.compat.Logz
import com.panopset.compat.Zombie
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage

class DeskApp4FX: Application() {
    override fun start(stage: Stage) {
        try {
            HiddenFolder.rootLogFileName = DeskApp4XFactory.panApplication.applicationShortName
            provideLogLocation()
            setUserAgentStylesheet(STYLESHEET_MODENA)
            JavaFXapp.launch(stage)
        } catch (eiie: ExceptionInInitializerError) {
            Logz.errorMsg(DeskApp4XFactory.panApplication.getApplicationDisplayName() + " Already running, exiting.")
            Platform.runLater {
                Zombie.stop()
                stage.close()
            }
            throw eiie
        }
    }

    override fun stop() {
        JavaFXapp.doExit()
    }

    fun doLaunch() {
        launch()
    }

    private fun provideLogLocation() {
//        try {
//            val tempDir = System.getProperty("java.io.tmpdir")
//            val logFileName = "panopset.log"
//            val logFilePath = Stringop.appendFilePath(tempDir, logFileName)
//            val fh: Handler = FileHandler(logFilePath)
//            fh.formatter = SimpleFormatter()
//            Logger.getLogger("").addHandler(fh)
//            Logger.getLogger("com.panopset").level = Level.WARNING
//        } catch (e: IOException) {
//            Logop.errorEx(e)
//        }
// TODO:
//    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//    FileAppender appender = (FileAppender) config.getAppender("file");
//    File file = new File(appender.getFileName());
//    Logop.green(String.format("Detailed logs can be found in %s", Fileop.getCanonicalPath(file)));
    }
}

