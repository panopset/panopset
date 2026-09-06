package com.panopset.compat

import java.io.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.file.FileSystems
import java.text.NumberFormat
import java.util.*
import java.util.Locale
import java.util.Locale.getDefault

object Stringop {
    private var lineTerminator: String? = null

    /**
     * StandardCharsets.UTF_8.name();
     */
    const val TAB = "\t"
    const val JAVA_RTN = "\n"
    val LINE_SEPARATOR: String = System.lineSeparator()
    const val LINE_FEED_VALUE = 10
    const val LINE_FEED = "\n"
    const val CARRIAGE_RETURN_VALUE = 13
    const val CARRIAGE_RETURN = "\r"
    const val DOS_RTN = "\r\n"
    val OS = System.getProperty("os.name").lowercase(getDefault())
    val USN: String = System.getProperty("user.name")
    val USH: String = System.getProperty("user.home")
 val FSP: String = FileSystems.getDefault().separator
 val TEMP_DIR_PATH = USH + FSP + "tmp"
    const val LAST_MODIFIED_FORMAT = "EEEE, MMMM dd, yyyy, h:mm a(zz)"
    const val TEST = "test"
    const val FOO = "foo"
    const val BAR = "bar"
    const val BAT = "bat"
    const val CS = "%s:%s"
    fun isRegexFound(regex: String, value: String): Boolean {
        return RegexValidator(regex).matches(value)
    }

    /**
     * Check for match.
     *
     * @param s1 String one.
     * @param s2 String two.
     * @param r1 Return value if one = two.
     * @param r2 Return value if one &lt;&gt; two.
     * @return r1 if s1 = s2, otherwise r2.
     */
    fun check4match(s1: String?, s2: String?, r1: String?, r2: String?): String {
        if (s1 == null || s2 == null || r1 == null || r2 == null) {
            return ""
        }
        return if (s1 == s2) {
            r1
        } else r2
    }

    fun capund(str: String?): String {
        if (!str.isNullOrEmpty()) {
            var rtn = str.replace("()([A-Z])".toRegex(), "$1_$2").uppercase(Locale.getDefault())
            if (rtn[0] == '_') {
                rtn = rtn.substring(1)
            }
            return rtn
        }
        return ""
    }

    fun arrayToString(strArray: ArrayList<String>): String {
        val sw = StringWriter()
        for (s in strArray) {
            sw.append(s)
            sw.append("\n")
        }
        return sw.toString()
    }

    fun arrayToList(strArray: Array<String>): List<String> {
        val rtn = ArrayList<String>()
        for (s in strArray) {
            rtn.add(s)
        }
        return rtn
    }

    fun listToArrayList(list: List<String>): ArrayList<String> {
        val rtn = ArrayList<String>()
        for (s in list) {
            rtn.add(s)
        }
        return rtn
    }

    fun upund(str: String): String {
        return str.uppercase(Locale.getDefault()).replace(" ".toRegex(), "_")
    }

    fun allcaps(str: String?): String {
        return str?.uppercase() ?: ""
    }

    fun capitalize(str: String?): String {
        return if (str != null && str.length > 0) {
            if (str.length > 1) {
                ("" + str[0]).uppercase(Locale.getDefault()) + str.substring(1)
            } else str.uppercase(Locale.getDefault())
        } else ""
    }

    fun toMaxLength(value: String?, max: Int): String {
        if (value == null) {
            return ""
        }
        if (max < 1) {
            return ""
        }
        return if (value.length > max) {
            value.substring(0, max)
        } else value
    }

    fun toUpperCase(value: String): String {
        return value.uppercase(Locale.getDefault())
    }

    fun toLowerCase(value: String?): String {
        return value?.lowercase(Locale.getDefault()) ?: ""
    }

 fun isPopulated(str: String?): Boolean {
        return !isEmpty(str)
    }

 fun isEmpty(str: String?): Boolean {
        return str == null || "" == str.trim { it <= ' ' }
    }

    fun pullAfter(str: String?, searchStr: String?): String {
        if (str == null || searchStr == null) {
            return ""
        }
        if (str.length < searchStr.length) {
            return ""
        }
        val i = str.indexOf(searchStr)
        return if (i == -1) {
            ""
        } else str.substring(i + searchStr.length)
    }

 fun getEol(): String {
        if (lineTerminator == null) {
            lineTerminator = "\n"
        }
        return lineTerminator?: "\n"
    }


    fun setEol(value: String) {
        lineTerminator = value
    }

 fun wrapFixedWidth(str: String, width: Int): String {
        if (!isPopulated(str)) {
            return ""
        }
        if (width < 1) {
            return str
        }
        if (str.length < width) {
            return str
        }
        val sw = StringWriter()
        for (i in 0 until str.length) {
            sw.append(str[i])
            if ((i + 1) % width == 0) {
                sw.append(getEol())
            }
        }
        return sw.toString()
    }

    fun countReturns(str: String?): Int {
        if (str == null) {
            return 0
        }
        var count = 0
        for (i in 0 until str.length) {
            val ch = str[i]
            if (ch == '\n') {
                count++
            }
        }
        return count
    }

    /**
     * All trailing empty Strings are removed.
     *
     * @param sa String array.
     * @return String array that ends with the last populated sa entry.
     */
    fun trimTrailingEmptyLines(sa: Array<String>): Array<String> {
        val rtn = ArrayList<String>()
        val size = sa.size - 1
        for ((index, s) in sa.withIndex()) {
            if (index < size || isPopulated(s)) {
                rtn.add(s)
            }
        }
        return rtn.toTypedArray()
    }

    /**
     * Empty String is returned if args is null. Otherwise all non-null element in
     * arg is concatenated in order.
     *
     * @param args Strings to join.
     * @return Concatenated args in one String.
     */
    fun concat(vararg args: String?): String {
        return concat(Arrays.asList(*args))
    }

    fun concat(pts: List<String?>?): String {
        if (pts == null) {
            return ""
        }
        val sw = StringWriter()
        for (s in pts) {
            if (s != null) {
                sw.append(s)
            }
        }
        return sw.toString()
    }

    fun stringToList(str: String): ArrayList<String> {
        return Streamop.getLinesFromReader(StringReader(str))
    }

    fun stringToListWithEol(str: String): ArrayList<String> {
        return Streamop.getLinesFromReaderWithEol( StringReader(str))
    }

    fun getDollarString(pennies: Long): String {
        return NumberFormat.getCurrencyInstance().format(
            BigDecimal(pennies).setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal(100), RoundingMode.HALF_UP)
        )
    }

    fun getCommaString(value: Int): String {
        return NumberFormat.getNumberInstance().format(value)
    }

    fun getCommaString(value: Long): String {
        return NumberFormat.getNumberInstance().format(value)
    }

    fun parseInt(value: String): Int {
        return if (!isPopulated(value)) {
            0
        } else {
            parseInt( value, 0)
        }
    }

    fun parseInt(value: String, defaultValue: Int): Int {
        return if (value.isNotEmpty()) {
            val str = value.replace(",", "")
            try {
                str.toInt()
            } catch (nfe: NumberFormatException) {
                Logz.errorMsg(value, nfe)
                defaultValue
            }
        } else {
            defaultValue
        }
    }

    fun isNumber(value: String): Boolean {
        return value.matches("[0-9]*".toRegex())
    }

    fun isInteger(value: String): Boolean {
        return value.matches("-?\\d+".toRegex())
    }

 fun isBlank(value: String?): Boolean {
        return value == null || "".contentEquals(value.trim { it <= ' ' })
    }

    fun appendEol(value: String?): String {
        return String.format("%s%s", value, getEol())
    }

    fun notNull(value: String?): String {
        return value ?: ""
    }

    fun randomString(): String {
        return String.format("%d", nextLong())
    }

    fun parseBoolean(value: String?, defaultValue: Boolean = false): Boolean {
        if (value == null) {
            return defaultValue
        }
        if ("on".contentEquals(value.lowercase(Locale.getDefault()))) {
            return true
        }
        if ("yes".contentEquals(value.lowercase(Locale.getDefault()))) {
            return true
        }
        return if ("y".contentEquals(value.lowercase(Locale.getDefault()))) {
            true
        } else value.toBoolean()
    }

    fun skipComment(value: String): Boolean {
        if (!isPopulated(value)) {
            return true
        }
        return if ("#" == value.take(1)) {
            true
        } else value.length > 1 && "//" == value.take(2)
    }

    fun listToString(list: List<String?>): String {
        val sw = StringWriter()
        for (s in list) {
            sw.append(s)
            sw.append(getEol())
        }
        return sw.toString()
    }

    fun replace(source: String, target: String?, replacement: String?): String {
        return source.replace(target!!, replacement!!)
    }

    fun replaceAll(source: String, target: String, replacement: String?): String {
        return source.replace(target.toRegex(), replacement!!)
    }

    fun replaceFirstLinePreserveIndentation(
        source: List<String>, lineToReplaceContaining: String,
        fullReplacementLine: String
    ): List<String> {
        var firstTime = true
        var priorIndentation = ""
        val rtn: MutableList<String> = ArrayList()
        for (str in source) {
            if (firstTime) {
                if (str.contains(lineToReplaceContaining)) {
                    firstTime = false
                    rtn.add(String.format("%s%s", priorIndentation, fullReplacementLine))
                } else {
                    rtn.add(str)
                    priorIndentation = getIndentation(str)
                }
            } else {
                rtn.add(str)
            }
        }
        return rtn
    }

    fun getIndentation(str: String?): String {
        if (str == null) {
            return ""
        }
        val sw = StringWriter()
        for (i in 0 until str.length) {
            if (Character.isWhitespace(str[i])) {
                sw.append(str[i])
            } else {
                return sw.toString()
            }
        }
        return sw.toString()
    }

    fun appendFilePath(vararg pathElements: String): String {
        val sw = StringWriter()
        var firstTime = true
        for (pathElement in pathElements) {
            if (firstTime) {
                sw.append(pathElement)
                firstTime = false
            } else {
                if (pathElement.indexOf(FSP) != 0) {
                    sw.append(FSP)
                }
                sw.append(pathElement)
            }
        }
        return sw.toString()
    }

    @JvmStatic
 fun firstHexDiff(expected: String?, value: String?): String {
        if (value == null) {
            return "value is null"
        }
        if (expected == null) {
            return "expected is null"
        }
        if (value.length == 0) {
            return "value is empty"
        }
        if (expected.length == 0) {
            return "expected is empty"
        }
        println("Comparing expected, to value:")
        println(expected)
        println(value)
        if (value.length != expected.length) {
            return String.format("Expected length is %d and value length is %d", expected.length, value.length)
        }
        for (i in 0 until value.length) {
            val c0 = expected[i]
            val c1 = value[i]
            if (c0 == c1) {
            } else {
                return String.format(
                    "At position %d, expected %s, but was %s",
                    i,
                    String.format("%04x", c0.code),
                    String.format("%04x", c1.code)
                )
            }
        }
        return "values are identical"
    }

    var id = 0L
    fun getNextJvmUniqueID(): Long {
        return id++
    }

    fun getNextJvmUniqueIDstr(): String {
        return "${getNextJvmUniqueID()}"
    }

    fun trimSurroundingQuotes(s: String): String {
        if (s.length < 2) {
            return s
        }
        if (s.substring(0, 1) == "\"") {
            val t = s.substring(1)
            val i = t.length
            if (t.substring(i - 1, i) == "\"") {
                return t.substring(0, i - 1)
            }
        }
        return s
    }
}
