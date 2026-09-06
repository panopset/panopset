package com.panopset.flywheel

import com.panopset.compat.Stringop.getEol
import java.io.StringWriter
import java.util.*

/**
 * Map wrapper with a map name.
 *
 * @param <K>
 * Key type.
 * @param <V>
 * Value type.
</V></K> */
class NamedMap<K, V> (
    private val name: String,
    val map: MutableMap<K, V> = Collections.synchronizedMap(HashMap())
) {

    fun put(key: K, value: V) {
        keyReporter.reportDefinedKey(name, key.toString())
        map[key] = value
    }

    operator fun get(key: K): V? {
        keyReporter.reportUsedKey(name, key.toString())
        return map[key]
    }

    val size: Int
        get() = map.size

    /**
     * Key Reporter is used to help see any un-used variables quickly.
     */
    class KeyReporter {
        fun getKeyReport(source: String): KeyReport {
            var rtn = keyReports[source]
            if (rtn == null) {
                rtn = KeyReport()
                keyReports[source] = rtn
            }
            return rtn
        }

        fun reportUsedKey(source: String, key: String) {
            val keyReport = getKeyReport(source)
            if (!keyReport.usedKeys.contains(key)) {
                keyReport.usedKeys.add(key)
            }
        }

        fun reportDefinedKey(source: String, key: String) {
            val keyReport = getKeyReport(source)
            if (!keyReport.definedKeys.contains(key)) {
                keyReport.definedKeys.add(key)
            }
        }

        fun reportUnusedKeys() {
            val sw = StringWriter()
            for (source in keyReports.keys) {
                val hdr = StringWriter()
                hdr.append("*********").append(source).append(":")
                hdr.append(getEol())
                val keyReport = getKeyReport(source)
                for (s in keyReport.usedKeys) {
                    keyReport.definedKeys.remove(s)
                }
                for (i in 0 until MAXIMUM_NUMERICS) {
                    keyReport.definedKeys.remove("" + i)
                }
                keyReport.definedKeys.remove(ReservedWords.FILE)
                keyReport.definedKeys.remove(ReservedWords.SCRIPT)
                keyReport.definedKeys.remove(ReservedWords.SPLITS)
                keyReport.definedKeys.remove(ReservedWords.TARGET)
                keyReport.definedKeys.remove(ReservedWords.TEMPLATE)
                keyReport.definedKeys.remove(ReservedWords.TOKENS)
                var firstTime = true
                for (s in keyReport.definedKeys) {
                    if (firstTime) {
                        sw.append(hdr.toString())
                        firstTime = false
                    }
                    sw.append(s)
                    sw.append(getEol())
                }
            }
            //Fileop.write(sw.toString(), new File(file));
        }

        private val keyReports: MutableMap<String, KeyReport> = Collections
            .synchronizedSortedMap(TreeMap())

        class KeyReport {
            val usedKeys: MutableList<String> = ArrayList()

            val definedKeys: MutableSet<String> = Collections
                .synchronizedSortedSet(TreeSet())
        }

        companion object {
            const val MAXIMUM_NUMERICS: Int = 100
        }
    }

    companion object {
        /**
         * Report unused keys. Numeric keys are left out because they are generally
         * reserved for lists.
         *
         * @param file
         * File to write report to.
         */
        fun reportUnusedKeys() {
            keyReporter.reportUnusedKeys()
        }

        val keyReporter: KeyReporter = KeyReporter()
    }
}
