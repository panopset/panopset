package com.panopset.flywheel

import com.panopset.compat.*
import com.panopset.compat.Rezop
import com.panopset.compat.Stringop.getEol
import com.panopset.compat.Stringop.isPopulated
import java.io.File
import java.io.IOException
import java.io.StringWriter
import java.util.*

/**
 * PankeyopStringsetString Flywheel.
 * <h3>Commands</h3> There are 7 commands that you may use in a Flywheel template. Commands have a
 * default syntax that starts with **${&#064;**, followed by the letter associated with the
 * command, followed by a space, followed by a parameter, followed by the default closing syntax of
 * **}**.
 * <h1>f - File</h1>
 *
 * <pre>
 * ${&#064;f somefile.txt}
</pre> *
 *
 *
 *
 * Output to the specified file, until the matching ${&#064;q} is found.
 *
 *
 * <h1>p -Push</h1>
 *
 * <pre>
 *
 * ${&#064;p variableName}
</pre> *
 *
 *
 *
 * Everything following this command is pushed into a String buffer, until a q command is reached.
 *
 * <h1>l - List</h1>
 *
 * <pre>
 * ${&#064;l someListFile.txt} or ${&#064;l someFile.txt,}
</pre> *
 *
 *
 *
 * Read the given file, and for each line execute the template from this list command up until its
 * matching q command. If no **token** is defined as a variable, then each line will be stored in
 * variable **1**. If there is a tokeStnextJvmUniqueIDstrriStringngmapn MutableMap<String, String>defined after a ~, the line will be split by that token,
 * and stored in variables named after integers, in order.
 *
 *
 * <h1>q - Quit</h1>
 *
 * <pre>
 * ${&#064;q}
</pre> *
 *
 *  * If the q command follows a p command, the String buffer is defined as the variable name that
 * was provided in the p command.
 *  * If the q command follows an l command, the String buffer is read for the each item in the
 * list.
 *
 * <h1>t - Template</h1>
 *
 * <pre>
 * ${&#064;t someTemplateFile.txt}
</pre> *
 *
 *
 *
 * Continue execution using the supplied template script file.
 *
 *
 * <h1>Variable</h1>
 *
 *
 * There is no command associated with a variable, so you drop the **&#064;** directive
 * indicator, and then you specify a variable just as you would in any ant script or unix shell. The
 * variable must have been defined either in a map provided to the script through
 * Script.Builder.mergeMap, or a Push command.
 *
 *
 * <pre>
 * ${variableName}
</pre> *
 *
 * <h1>e - Execute</h1> Excecute any static method. Parameters may be Flywheel defined variable
 * names only. For example:
 *
 * <pre>
 *
 * ${&#064;p name}panopset${q}
 *
 * ${&#064;e com.panopset.compat.Strings.capitalize(name)}
 *
</pre> *
 *
 *
 *
 * The above script will output:
 *
 *
 * <pre>
 *
 * Panopset
 *
</pre> *
 */
class Flywheel(val sls: TemplateSource?) : MapProvider {
    val mapHold = MapHold()
    private var daTemplate: Template? = null
    var targetDirectory: File? = null
        private set
    private var file: File? = null

    /**
     * Used for debugging.
     *
     * @return Currently resolving command.
     */
    /**
     * Currently resolving command.
     */
    val resolvingCommand: Command? = null

    /**
     * Writer for this Flywheel.
     */
    private var pwriter: StringWriter? = null


    var currentCommandFile: CommandFile? = null

    /**
     * Set replacements vector.
     *
     * @param replacementsList Replacements vector.
     */
    var replacements = ArrayList<Array<String>>()

    private var registeredObjs: MutableMap<String, Any>? = null
    var lineFeedRules: LineFeedRules? = null
        get() {
            if (field == null) {
                field = FULL_BREAKS
            }
            return field
        }

    /**
     * Copy package resource.
     *
     * @param resourcePath Java source path resource.
     * @param targetPath Target path, relative to Flywheel target directory.
     * @throws IOException IO Exception.
     */
    fun copyPackageResource(template: Template, resourcePath: String, targetPath: String) {
        try {
            if (targetDirectory == null) {
                return
            }
            Rezop.copyTextResourceToFile(
                this.javaClass, resourcePath,
                Fileop.getCanonicalPath(targetDirectory!!) + "/" + targetPath
            )
        } catch (ex: IOException) {
            val sw = StringWriter()
            sw.append("baseDirectoryPath: $baseDirectoryPath")
            sw.append(getEol())
            sw.append("Template file: " + Fileop.getCanonicalPath(file!!))
            sw.append(getEol())
            sw.append("targetDirectory: $targetDirectory")
            sw.append(getEol())
            sw.append("resourcePath: $resourcePath")
            sw.append(getEol())
            sw.append("targetPath: $targetPath")
            sw.append(getEol())
            template.templateSource
            sw.append("executing from: " + template.templateSource.name)
            sw.append(getEol())
            sw.append("Line: ")
            sw.append("" + template.templateSource.line)
            sw.append(getEol())
            Logz.warn(sw.toString())
            throw ex
        }
    }

    val isTargetDirectoryValid: Boolean
        /**
         * Is target directory valid.
         *
         * @return true if a valid, writable target directory has been specified.
         */
        get() {
            if (targetDirectory != null && !targetDirectory!!.exists()) {
                Fileop.mkdirs( targetDirectory!!)
            }
            return (targetDirectory != null && targetDirectory!!.isDirectory() && targetDirectory!!.exists()
                    && targetDirectory!!.canWrite())
        }
    var writer = StringWriter()

    /**
     * Put map value, that will be available to future variable commands.
     *
     * @param key Variable name.
     * @param value Variable value.
     */
    fun put(key: String, value: String) {
        mapHold.put(key, value)
    }
    fun mergeMap(map: MutableMap<String, String>) {
        mapHold.mergeMap(map)
    }

    /**
     * Load properties.
     *
     * @param relativePath Path is relative to the parent of the main Flywheel script file.
     * @return empty String.
     */
    fun loadProperties(relativePath: String): String {
        val file = File(getBaseDirectoryPath() + "/" + relativePath)
        val props = Properties()
        if (!file.exists()) {
            try {
                Rezop.copyTextResourceToFile( this.javaClass, relativePath, file)
            } catch (ex: IOException) {
                Logz.warn("relativePath: $relativePath")
                Logz.errorEx(ex)
            }
        }
        Propop.load( props, file)
        for (k in props.keys) {
            put(k.toString(), props.getProperty(k.toString()))
        }
        return ""
    }

    /**
     * Suppress replacements.
     *
     * **Usage**
     *
     * <pre>
     * ${&#064;p foo}bar{&#064;q}
     * ${&#064;e script.suppressReplacements(true)}
     * A: ${&#064;v foo}
     * ${&#064;e script.suppressReplacements(false)}
     * B: ${&#064;v foo}
    </pre> *
     *
     * **Output**
     *
     * <pre>
     *
     * A:
     * B: bar
     *
    </pre> *
     *
     * @param value If true, all v commands will not output anything.
     */
    fun suppressReplacements(value: String?) {
        if (value == null) {
            control.isReplacementsSuppressed = false
            return
        }
        control.isReplacementsSuppressed = value.toBoolean()
    }

    override fun getEntry(key: String): String {
        return mapHold.get(key)
    }

    fun exec(): String {
        if (isStopped) {
            return ""
        }
        getTemplate().output()
        return writer.toString()
    }

    fun getTemplate(): Template {
        if (daTemplate == null) {
            daTemplate = Template(this, sls!!, lineFeedRules!!)
        }
        return daTemplate as Template
    }

    fun stop(message: String) {
        Logz.errorMsg(message)
        if (resolvingCommand != null) {
            Logz.warn(Nls.xlate("Stopped while executing line") + " $resolvingCommand")
            Logz.debug(" at: $resolvingCommand")
        }
        Logz.debug(mapHold.dump())
        Logz.warn(
            Nls
                .xlate("Template parsing stopped.  Check debug in help->Show log, for more information")
        )
        control.stop(message)
    }

    val isStopped = false
    private var baseDirectoryPath: String? = null

    /**
     * Get the directory in which the script top template file resides. This is used as a relative
     * path base for all subsequent template files created with the template command: **${&#064;t
     * templateRelativePathName}**.
     *
     * @return Full path of parent directory of the top script template file.
     */
    fun getBaseDirectoryPath(): String {
        if (baseDirectoryPath == null) {
            baseDirectoryPath = if (file != null && file!!.exists()) {
                try {
                    file!!.getParentFile().getCanonicalPath()
                } catch (e: IOException) {
                    Logz.errorEx(e)
                    return ""
                }
            } else {
                System.getProperty("user.home")
            }
        }
        return baseDirectoryPath?: ""
    }

    /**
     * Append simple file name to getBaseDirectoryPath.
     *
     * @param simpleFileName Simple file name (no path).
     * @return Full relative path of simpleFileName
     */
    fun getBaseRelativePath(simpleFileName: String): String {
        return getBaseDirectoryPath() + "/" + simpleFileName
    }

    val isReplacementsSuppressed: Boolean
        /**
         * Is replacements repressed.
         *
         * @return true if replacements suppressed flag is set.
         */
        get() = control.isReplacementsSuppressed

    var registeredObjects = HashMap<String, Any>()

    /**
     * Get control.
     *
     * @return Flywheel Control.
     */
    /**
     * Flywheel control.
     */
    val control = Control()
    /**
     * @return True iff output to files is enabled, default is true.
     */
    /**
     * Set whether or not Flywheel will write output to text files. Default is true, you might want to
     * set to false if all you are doing is String manipulation in memory.
     *
     * @param value Value to set.
     */
    /**
     * Is output to files enabled.
     */
    var isOutputEnabled = true

    /**
     * @param value New base directory path.
     */
    fun setBaseDirectoryPath(value: String?) {
        if (value != null) {
            baseDirectoryPath = value
        }
        if (file != null && baseDirectoryPath == null) {
            baseDirectoryPath = Fileop.getParentDirectory(file!!)
        }
    }

    /**
     * @param value Target directory.
     */
    fun setTargetDirectory(value: String?) {
        if (isPopulated(value)) {
            targetDirectory = File(value)
        }
        if (targetDirectory == null) {
            targetDirectory = targetDirectory
        }
    }

    /**
     * @param value New file.
     */
    fun setFile(value: File?) {
        if (value != null) {
            file = value
            mapHold.put(ReservedWords.TEMPLATE, file!!.getName())
        }
    }

    val findLines: MutableList<String> = ArrayList()
    fun setFindLine(value: String) {
        findLines.clear()
        findLines.add(value)
    }

    fun setFindLines(v0: String, v1: String) {
        findLines.clear()
        findLines.add(v0)
        findLines.add(v1)
    }

    var combine: String = ""

    /**
     * Flywheel constructor.
     *
     * @param stringLineSupplier line supplier.
     */
    init {
        ScriptSecurity.defineAllowedScriptCalls()
        mapHold.put(ReservedWords.FILE, sls?.name ?: "")
        mapHold.put(ReservedWords.MACAddress, NetworkInfo.maca)
    }

    companion object {

        /**
         * @param args
         *

        <pre>
        [0] = script file name, args[1] = target directory name.
        or
        [0] = properties file name where these properties are required:
        com.panopset.flywheel.script=&lt;your script file name&gt;
        com.panopset.flywheel.target=&lt;your target file name&gt;

        and then any other properties that are needed by the scripts.

        See site.properties in this project for details.
        </pre>

         */
        @JvmStatic
        fun main(vararg args: String) {
            if (args.isEmpty() || args.size > 2) {
                Logz.dspmsg("Params are script and target directory, or\nfw (properties file)\nor\nfw --help")
            } else {
                if (args.size == 1) {
                    if ("--help" == args[0]) {
                        FlywheelHelp.outputHelp()
                    } else {
                        FlywheelBuilder().properties(File(args[0])).construct().exec()
                    }
                } else {
                    val scriptFileName = args[0]
                    val targetDirectoryName = args[1]
                    val scriptFile = File(scriptFileName)
                    val targetDirectory = File(targetDirectoryName)
                    if (!scriptFile.exists()) {
                        Logz.errorMsg("File does not exist.", scriptFile)
                        return
                    }
                    if (!scriptFile.canRead()) {
                        Logz.errorMsg("Can not read.", scriptFile)
                        return
                    }
                    Logz.info(
                        String.format(
                            Stringop.CS,
                            "Script file",
                            Fileop.getCanonicalPath(scriptFile)
                        )
                    )
                    Logz.info(
                        String.format(
                            Stringop.CS, "Target directory",
                            Fileop.getCanonicalPath(targetDirectory)
                        )
                    )
                    FlywheelBuilder().file(scriptFile).targetDirectory(targetDirectory).construct().exec()
                }
            }
        }
    }
}
