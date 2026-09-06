package com.panopset.flywheel

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.Nls
import com.panopset.compat.Propop
import com.panopset.compat.Stringop.isPopulated
import com.panopset.gp.textFileIterator
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.StringWriter
import java.util.*

class FlywheelBuilder() {
    private var inputStream: InputStream? = null
    private var isOutputEnabled = true
    private var lineFeedRules: LineFeedRules? = null
    private var writer: StringWriter? = null
    private var targetDirectory: File? = null
    private var array: Array<String?>? = null
    private var sls: TemplateSource? = null
    private var scriptFile: File? = null
    private var baseDirectoryPath: String? = null
    private val map: MutableMap<String, String> = HashMap()
    private val registeredObjects = HashMap<String, Any>()
    private var isValid = true

    /**
     * Create a script. Example usage:
     *
     * <pre>
     * new Script.Builder()
     *
     * .targetDirectory(&quot;publish/site&quot;).scriptFile(
     *
     * &quot;templates/index.txt&quot;)
     * .properties(
     *
     * &quot;my.properties&quot;) // Script
     * // variables.
     * .constructScript().exec();
    </pre> *
     *
     * @return Panopset Flywheel Script object.
     */
    fun construct(): Flywheel {
        val flywheel = Flywheel( getStringLineSupplier())
        flywheel.setBaseDirectoryPath(getBaseDirectoryPath())
        flywheel.mergeMap(map)
        flywheel.replacements = replacements
        flywheel.setTargetDirectory(Fileop.getCanonicalPath(getTargetDirectory()))
        flywheel.put(ReservedWords.TARGET, Fileop.getCanonicalPath(getTargetDirectory()))
        if (writer != null) {
            flywheel.writer = writer!!
        }
        registeredObjects[ReservedWords.FLYWHEEL] = flywheel
        flywheel.registeredObjects = registeredObjects
        flywheel.setFile(getScriptFile())
        flywheel.isOutputEnabled = isOutputEnabled
        flywheel.lineFeedRules = getLineFeedRules()
        if (!isValid) {
            flywheel.stop("See messages above, processing cancelled.")
        }
        return flywheel
    }

    /**
     * Set target directory. This is where your output is going. Directory
     * structures will be created as needed, and **existing files are wiped without
     * warning**.
     *
     * @param newTargetDirectory All output files specified by the ${&#064;f
     * fileName} command are relative paths based here.
     * @return Builder.
     */
    fun targetDirectory(newTargetDirectory: File): FlywheelBuilder {
        targetDirectory = newTargetDirectory.getAbsoluteFile()
        return this
    }

    /**
     * Set script file.
     *
     * @param value Specify a single controlling script file.
     * @return Builder.
     */
    fun file(value: File?): FlywheelBuilder {
        scriptFile = value
        return this
    }

    fun scriptFilePath(path: String): FlywheelBuilder {
        return file(File(path))
    }

    /**
     * You do not need to specify this if you are specifying a File in
     * Flywheel.Builder.file, as the parent directory of the main template file
     * would be used. This method would rarely be needed, but is available for
     * flexibility.
     *
     * @param path Base directory path.
     * @return Builder.
     */
    fun baseDirectoryPath(path: String?): FlywheelBuilder {
        baseDirectoryPath = path
        return this
    }

    /**
     * If you supply a String array here, the **file** Flywheel.Builder method
     * will be ignored.
     *
     * @param newStringArray Script commands in String array.
     * @return Builder.
     */
    fun inputArray(newStringArray: Array<String?>): FlywheelBuilder {
        array = newStringArray
        return this
    }

    /**
     * The **file** Flywheel.Builder method will be ignored if you supply a
     * String template here.
     *
     * @param template Simple single String template.
     * @return Builder.
     */
    fun inputString(template: String): FlywheelBuilder {
        return inputArray(arrayOf(template))
    }

    /**
     * If you supply a List of Strings here, do not use the **file**
     * Flywheel.Builder method.
     *
     * @param newInput Script commands in String List.
     * @return Builder.
     */
    fun inputList(newInput: List<String>): FlywheelBuilder {
        val sa = arrayOfNulls<String>(newInput.size)
        var incr = 0
        for (s in newInput) {
            sa[incr++] = s
        }
        return this.inputArray(sa)
    }

    fun input(clazz: Class<*>, resourcePath: String?): FlywheelBuilder {
        return inputInputStream(clazz.getResourceAsStream(resourcePath))
    }

    fun inputResourcePath(clazz: Class<*>, resourcePath: String?): FlywheelBuilder {
        return inputInputStream(clazz.getResourceAsStream(resourcePath))
    }

    fun inputInputStream(value: InputStream?): FlywheelBuilder {
        inputStream = value
        return this
    }

    fun withLineFeedRules(lineFeedRules: LineFeedRules): FlywheelBuilder {
        this.lineFeedRules = lineFeedRules
        return this
    }

    /**
     * Defaults to &quot;temp&quot;.
     *
     * @return target directory File.
     */
    private fun getTargetDirectory(): File {
        if (targetDirectory == null) {
            val targetDirName = map[ReservedWords.TARGET]?: ""
            return if (isPopulated(targetDirName)) {
                File(targetDirName)
            } else File("temp")
        }
        return targetDirectory?:File("temp")
    }

    private fun getScriptFile(): File {
        if (scriptFile == null) {
            val scriptFileName = map[ReservedWords.SCRIPT]?: ""
            if (isPopulated(scriptFileName)) {
                scriptFile = File(scriptFileName)
                return scriptFile!!
            }
            scriptFile = File("com.panopset.flywheel.EmptyFile.txt")
        }
        return scriptFile!!
    }

    private fun getBaseDirectoryPath(): String? {
        if (!isPopulated(baseDirectoryPath)) {
            baseDirectoryPath = Fileop.getCanonicalPath(getScriptFile().getAbsoluteFile().getParentFile())
        }
        return baseDirectoryPath
    }

    private fun getStringLineSupplier(): TemplateSource? {
        if (sls == null) {
            if (array != null) {
                sls = TemplateArray(array!!)
                return sls
            }
            if (inputStream != null) {
                sls = TemplateInputStream( inputStream!!)
                return sls
            }
            sls = TemplateFile( getScriptFile())
        }
        return sls
    }

    private fun getLineFeedRules(): LineFeedRules {
        if (lineFeedRules == null) {
            lineFeedRules = FULL_BREAKS
        }
        return lineFeedRules!!
    }

    /**
     * You may pre-define variables by supplying a map. Please remember to stay away
     * from the the numbers, as they are used in lists.
     *
     * @param additionalMap Additional map. In the case of duplicate keys, the most
     * recently added map takes precedence.
     * @return Builder.
     */
    fun map(additionalMap: Map<String, String>): FlywheelBuilder {
        Javop.copyMap(additionalMap, map)
        return this
    }

    /**
     * Add a key value pair to the map.
     *
     * @param key   Variable command name.
     * @param value Variable value.
     * @return Builder.
     */
    fun map(key: String, value: String): FlywheelBuilder {
        map[key] = value
        return this
    }

    /**
     * Convert Object values using toString, prior to applying to map.
     *
     * @param additionalMap Additional map. In the case of duplicate keys, the most
     * recently added map takes precedence.
     * @return Builder.
     */
    fun mapObjects(additionalMap: Map<String, Any>): FlywheelBuilder {
        Javop.copyMapObjects(additionalMap, map)
        return this
    }

    /**
     * Specify list line tokens, default is none ("").
     *
     * @param tokens List line tokens.
     * @return Builder.
     */
    fun tokens(tokens: String): FlywheelBuilder {
        map[ReservedWords.TOKENS] = tokens
        return this
    }

    /**
     * Register any instantiated objects you want available to the Execute command
     * here. You will be able to use any method of those objects that accept 0 or
     * more String parameters, and that return a single String result. Recommended,
     * but not required, to qualify objects with their path names.
     * com.panopset.flywheel.Flywheel is registered automatically.
     *
     * @param key Name object will be known by, in Execute commands.
     * @param obj Instance Execute commands will invoke methods on.
     * @return Builder.
     */
    fun registerObject(key: String, obj: Any): FlywheelBuilder {
        registeredObjects[key] = obj
        return this
    }

    /**
     * Load properties file into pre-defined variables.
     *
     * @see FlywheelBuilder.map
     *
     * @param propertiesFile Properties file.
     * @return Builder.
     */
    @Throws(IOException::class)
    fun properties(propertiesFile: File?): FlywheelBuilder {
        if (propertiesFile != null && propertiesFile.exists()) {
            return map(Propop.loadPropsFromFile( propertiesFile))
        }
        if (propertiesFile != null) {
            Logz.warn(Fileop.getCanonicalPath(propertiesFile) + " " + Nls.xlate("does not exist, skipping."))
        }
        return this
    }

    fun properties(properties: Properties): FlywheelBuilder {
        return map(Propop.loadMapFromProperties(properties))
    }

    /**
     * Define replacements in a file specified by the replacements parameter. First
     * line of that file contains a single character that is the separator for
     * subsequent lines that define replacements.
     *
     * @param replacementsFile file.
     * @return Builder.
     */
    fun replacements(replacementsFile: File): FlywheelBuilder {
        val v: MutableList<Array<String>> = ArrayList()
        try {
            val tfp = textFileIterator( replacementsFile)
            var separator: String? = null
            while (tfp.hasNext()) {
                val str = tfp.next()
                if (separator == null) {
                    separator = str
                } else {
                    val st = StringTokenizer(str, separator)
                    v.add(arrayOf(st.nextToken(), st.nextToken()))
                }
            }
            for (r in v) {
                replacement(r)
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
        return this
    }

    /**
     * Add a global replacement. Replacements are performed in the order added.
     *
     * @param sa String Array sa[0] = from sa[1] = to.
     * @return Builder.
     */
    fun replacement(sa: Array<String>): FlywheelBuilder {
        return replacement(sa[0], sa[1])
    }

    /**
     * Add a global replacement. Replacements are performed in the order added.
     *
     * @param from String to replace with the to String.
     * @param to   String to put in place of the from String.
     * @return Builder.
     */
    fun replacement(from: String, to: String): FlywheelBuilder {
        replacements.add(arrayOf(from, to))
        return this
    }

    var replacements = ArrayList<Array<String>>()

    /**
     * If you specify a writer, you need not specify a targetDirectory, as it would
     * be meaningless, and any File commands are ignored, as you are intending your
     * output to be a StringWriter.
     *
     * @param newWriter StringWriter to write output to.
     * @return Builder.
     */
    fun withWriter(newWriter: StringWriter?): FlywheelBuilder {
        writer = newWriter
        isOutputEnabled = false
        return this
    }

    private fun invalidate() {
        isValid = false
    }
}
